package no.nav.pensjon.vtp.kafkaembedded

import no.nav.pensjon.vtp.felles.CryptoConfigurationParameters
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.security.JaasUtils
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.StreamsConfig
import org.slf4j.LoggerFactory
import java.util.*
import java.util.stream.Collectors

@Suppress("unused")
class LocalKafkaServer(
    val zookeperPort: Int,
    val kafkaBrokerPort: Int,
    private val bootstrapTopics: Collection<String>,
    private val cryptoConfigurationParameters: CryptoConfigurationParameters
) {
    private var kafka: KafkaLocal? = null
    var localProducer: LocalKafkaProducer? = null
        private set
    private var localConsumer: LocalKafkaConsumerStream? = null
    var kafkaAdminClient: AdminClient? = null
        private set

    private fun createAdminClientProps(boostrapServer: String): Properties {
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = boostrapServer
        props[ProducerConfig.RETRIES_CONFIG] = 15
        props[ProducerConfig.BATCH_SIZE_CONFIG] = 16384
        props[ProducerConfig.LINGER_MS_CONFIG] = 1
        props[ProducerConfig.BUFFER_MEMORY_CONFIG] = 33554432
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] =
            StringSerializer::class.java.name
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] =
            StringSerializer::class.java.name
        props[StreamsConfig.SECURITY_PROTOCOL_CONFIG] = "SASL_SSL"
        props[SaslConfigs.SASL_MECHANISM] = "PLAIN"
        props[SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG] = cryptoConfigurationParameters.truststorePath
        props[SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG] = cryptoConfigurationParameters.truststorePassword
        props[SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG] = cryptoConfigurationParameters.keystorePath
        props[SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG] = cryptoConfigurationParameters.keystorePassword
        props[SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG] = ""
        val jaasTemplate =
            "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";"
        props[SaslConfigs.SASL_JAAS_CONFIG] = String.format(jaasTemplate, "vtp", "vtp")
        return props
    }

    private fun setupZookeperProperties(zookeeperPort: Int): Properties {
        val zkProperties = Properties()
        zkProperties["dataDir"] = "target/zookeeper/" + zookeeperAndKafkaTempInstanceDataDir
        zkProperties["clientPort"] = "" + zookeeperPort
        zkProperties["admin.enableServer"] = "false"
        zkProperties["jaasLoginRenew"] = "3600000"
        zkProperties["authorizer.class.name"] = "kafka.security.auth.SimpleAclAuthorizer"
        zkProperties["allow.everyone.if.no.acl.found"] = "true"
        zkProperties["ssl.client.auth"] = "none"
        zkProperties["ssl.keystore.location"] = cryptoConfigurationParameters.keystorePath
        zkProperties["ssl.keystore.password"] = cryptoConfigurationParameters.keystorePassword
        zkProperties["ssl.truststore.location"] = cryptoConfigurationParameters.truststorePath
        zkProperties["ssl.truststore.password"] = cryptoConfigurationParameters.truststorePassword
        return zkProperties
    }

    private fun setupKafkaProperties(zookeeperPort: Int): Properties {
        val kafkaProperties = Properties()
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
*/kafkaProperties["listener.security.protocol.map"] =
            "INTERNAL:SASL_SSL,EXTERNAL:SASL_SSL" // TODO: Fjern når POC fungerer
        kafkaProperties["zookeeper.connect"] = "localhost:$zookeeperPort"
        kafkaProperties["offsets.topic.replication.factor"] = "1"
        kafkaProperties["log.dirs"] = "target/kafka-logs/" + zookeeperAndKafkaTempInstanceDataDir
        kafkaProperties["auto.create.topics.enable"] = "true"
        kafkaProperties["listeners"] = "INTERNAL://:9092,EXTERNAL://:9093"
        kafkaProperties["advertised.listeners"] = "INTERNAL://localhost:9092,EXTERNAL://vtp:9093"
        kafkaProperties["socket.request.max.bytes"] = "369296130"
        kafkaProperties["sasl.enabled.mechanisms"] = "DIGEST-MD5,PLAIN"
        kafkaProperties["sasl.mechanism.inter.broker.protocol"] = "PLAIN"
        kafkaProperties["inter.broker.listener.name"] = "INTERNAL"
        kafkaProperties[SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG] = ""
        val jaasTemplate =
            "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";"
        kafkaProperties["SASL_SSL.".toLowerCase() + SaslConfigs.SASL_JAAS_CONFIG] =
            String.format(jaasTemplate, "vtp", "vtp")
        kafkaProperties[SaslConfigs.SASL_MECHANISM] = "PLAIN"

        // SSL
        kafkaProperties["ssl.keystore.location"] = cryptoConfigurationParameters.keystorePath
        kafkaProperties["ssl.keystore.password"] = cryptoConfigurationParameters.keystorePassword
        kafkaProperties["ssl.truststore.location"] = cryptoConfigurationParameters.truststorePath
        kafkaProperties["ssl.truststore.password"] = cryptoConfigurationParameters.truststorePassword
        return kafkaProperties
    }

    fun start() {
        val bootstrapServers = String.format("%s:%s", "localhost", kafkaBrokerPort)
        val kafkaProperties = setupKafkaProperties(zookeperPort)
        val zkProperties = setupZookeperProperties(zookeperPort)
        val url = javaClass.getResource("/kafkasecurity.conf")
            ?: throw RuntimeException("Unable to locate kafkasecurity.conf")
        System.setProperty(JaasUtils.JAVA_LOGIN_CONFIG_PARAM, url.toString())
        log.info("Kafka startes med Jaas login config param: " + System.getProperty(JaasUtils.JAVA_LOGIN_CONFIG_PARAM))
        try {
            kafka = KafkaLocal(kafkaProperties, zkProperties)
        } catch (e: Exception) {
            log.error("Kunne ikke starte Kafka producer og/eller consumer", e)
        }
        kafkaAdminClient = AdminClient.create(createAdminClientProps(bootstrapServers)).apply {
            createTopics(
                bootstrapTopics.stream().map { name: String? -> NewTopic(name, 1, 1.toShort()) }
                    .collect(Collectors.toList())
            )
        }
        localConsumer = LocalKafkaConsumerStream(bootstrapServers, bootstrapTopics, cryptoConfigurationParameters)
        localProducer = LocalKafkaProducer(bootstrapServers, cryptoConfigurationParameters)
        localConsumer!!.start()
    }

    fun stop() {
        log.info("Stopper kafka server")
        localConsumer!!.stop()
        kafkaAdminClient!!.close()
        kafka!!.stop()
    }

    companion object {
        private val log = LoggerFactory.getLogger(LocalKafkaServer::class.java)
        private val zookeeperAndKafkaTempInstanceDataDir =
            "" + System.currentTimeMillis() // alltid ny zookeeper-node og ny kafka-cluster
    }
}
