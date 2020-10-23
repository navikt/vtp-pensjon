package no.nav.pensjon.vtp.kafkaembedded;

import static java.util.Optional.ofNullable;

import no.nav.pensjon.vtp.felles.KeystoreUtils;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.security.JaasUtils;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

public class LocalKafkaServer {

    final static public String VTP_KAFKA_HOST = null != System.getenv("VTP_KAFKA_HOST") ? System.getenv("VTP_KAFKA_HOST") : "localhost";
    final private static Logger log = LoggerFactory.getLogger(LocalKafkaServer.class);
    private static final String zookeeperAndKafkaTempInstanceDataDir = "" + System.currentTimeMillis(); //alltid ny zookeeper-node og ny kafka-cluster
    private final Collection<String> bootstrapTopics;
    private KafkaLocal kafka;
    private LocalKafkaProducer localProducer;
    private LocalKafkaConsumerStream localConsumer;
    private AdminClient kafkaAdminClient;
    private int zookeeperPort;
    private int kafkaBrokerPort;

    public LocalKafkaServer(final int zookeeperPort, final int kafkaBrokerPort, Collection<String> bootstrapTopics) {
        this.zookeeperPort = zookeeperPort;
        this.kafkaBrokerPort = kafkaBrokerPort;
        this.bootstrapTopics = bootstrapTopics;
    }

    private static Properties createAdminClientProps(String boostrapServer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServer);
        props.put(ProducerConfig.RETRIES_CONFIG, 15);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(StreamsConfig.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, KeystoreUtils.getTruststoreFilePath());
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, KeystoreUtils.getTruststorePassword());
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, KeystoreUtils.getKeystoreFilePath());
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, KeystoreUtils.getKeyStorePassword());
        props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, "vtp", "vtp"));

        return props;
    }

    private static Properties setupZookeperProperties(int zookeeperPort) {
        Properties zkProperties = new Properties();
        zkProperties.put("dataDir", "target/zookeeper/" + zookeeperAndKafkaTempInstanceDataDir);
        zkProperties.put("clientPort", "" + zookeeperPort);
        zkProperties.put("admin.enableServer", "false");
        zkProperties.put("jaasLoginRenew", "3600000");

        zkProperties.put("authorizer.class.name", "kafka.security.auth.SimpleAclAuthorizer");
        zkProperties.put("allow.everyone.if.no.acl.found", "true");
        zkProperties.put("ssl.client.auth", "none");

        zkProperties.put("ssl.keystore.location", KeystoreUtils.getKeystoreFilePath());
        zkProperties.put("ssl.keystore.password", KeystoreUtils.getKeyStorePassword());
        zkProperties.put("ssl.truststore.location", KeystoreUtils.getTruststoreFilePath());
        zkProperties.put("ssl.truststore.password", KeystoreUtils.getTruststorePassword());

        return zkProperties;
    }

    private static Properties setupKafkaProperties(int zookeeperPort, int kafkaBrokerPort) {
        Properties kafkaProperties = new Properties();
/*
        //TODO: Gjør dette om til kode når POC fungerer i VTP
        String listeners = "INTERNAL://localhost:"+kafkaBrokerPort;
        if(null != System.getenv("VTP_KAFKA_HOST")){
            listeners = listeners + String.format(",EXTERNAL://%s",VTP_KAFKA_HOST);
            kafkaProperties.put("listener.security.protocol.map","INTERNAL:SASL_SSL,EXTERNAL:SASL_SSL");
            LOG.info("VTP_KAFKA_HOST satt for miljø. Starter med følgende listeners: {}", listeners);
        } else {
            LOG.info("VTP_KAFKA_HOST ikke satt for miljø. Starter med følgende listeners: {}", listeners);
            kafkaProperties.put("listener.security.protocol.map","INTERNAL:SASL_SSL");
        }
*/
        kafkaProperties.put("listener.security.protocol.map", "INTERNAL:SASL_SSL,EXTERNAL:SASL_SSL"); //TODO: Fjern når POC fungerer
        kafkaProperties.put("zookeeper.connect", "localhost:" + zookeeperPort);
        kafkaProperties.put("offsets.topic.replication.factor", "1");
        kafkaProperties.put("log.dirs", "target/kafka-logs/" + zookeeperAndKafkaTempInstanceDataDir);
        kafkaProperties.put("auto.create.topics.enable", "true");
        kafkaProperties.put("listeners", "INTERNAL://:9092,EXTERNAL://:9093");
        kafkaProperties.put("advertised.listeners", "INTERNAL://localhost:9092,EXTERNAL://vtp:9093");
        kafkaProperties.put("socket.request.max.bytes", "369296130");
        kafkaProperties.put("sasl.enabled.mechanisms", "DIGEST-MD5,PLAIN");
        kafkaProperties.put("sasl.mechanism.inter.broker.protocol", "PLAIN");
        kafkaProperties.put("inter.broker.listener.name", "INTERNAL");
        kafkaProperties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        kafkaProperties.put("SASL_SSL.".toLowerCase() + SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, "vtp", "vtp"));
        kafkaProperties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");

        //SSL
        kafkaProperties.put("ssl.keystore.location", KeystoreUtils.getKeystoreFilePath());
        kafkaProperties.put("ssl.keystore.password", KeystoreUtils.getKeyStorePassword());
        kafkaProperties.put("ssl.truststore.location", KeystoreUtils.getTruststoreFilePath());
        kafkaProperties.put("ssl.truststore.password", KeystoreUtils.getTruststorePassword());
        return kafkaProperties;
    }

    public int getZookeperPort() {
        return zookeeperPort;
    }

    public int getKafkaBrokerPort() {
        return kafkaBrokerPort;
    }

    public AdminClient getKafkaAdminClient() {
        return kafkaAdminClient;
    }

    public void start() {
        final String bootstrapServers = String.format("%s:%s", "localhost", kafkaBrokerPort);

        Properties kafkaProperties = setupKafkaProperties(zookeeperPort, kafkaBrokerPort);
        Properties zkProperties = setupZookeperProperties(zookeeperPort);

        URL url = ofNullable(getClass().getResource("/kafkasecurity.conf")).orElseThrow(() -> new RuntimeException("Unable to locate kafkasecurity.conf"));
        System.setProperty(JaasUtils.JAVA_LOGIN_CONFIG_PARAM, url.toString());
        log.info("Kafka startes med Jaas login config param: " + System.getProperty(JaasUtils.JAVA_LOGIN_CONFIG_PARAM));


        try {
            kafka = new KafkaLocal(kafkaProperties, zkProperties);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Kunne ikke starte Kafka producer og/eller consumer");
        }


        kafkaAdminClient = AdminClient.create(createAdminClientProps(bootstrapServers));
        kafkaAdminClient.createTopics(
                bootstrapTopics.stream().map(
                        name -> new NewTopic(name, 1, (short) 1)).collect(Collectors.toList()));
        localConsumer = new LocalKafkaConsumerStream(bootstrapServers, bootstrapTopics);

        localProducer = new LocalKafkaProducer(bootstrapServers);
        localConsumer.start();
    }

    public void stop() {
        log.info("Stopper kafka server");
        localConsumer.stop();
        kafkaAdminClient.close();
        kafka.stop();
    }

    public LocalKafkaProducer getLocalProducer() {
        return localProducer;
    }
}
