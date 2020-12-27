package no.nav.pensjon.vtp.kafkaembedded

import no.nav.pensjon.vtp.felles.CryptoConfigurationParameters
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*
import java.util.function.Consumer

class LocalKafkaConsumerStream(
    bootstrapServers: String?,
    topics: Collection<String>,
    cryptoConfigurationParameters: CryptoConfigurationParameters
) {
    private val stream: KafkaStreams
    fun start() {
        stream.start()
        LOG.info("Starter konsumering av topics")
    }

    fun stop() {
        stream.close(Duration.ofSeconds(10))
    }

    private fun handleMessage(topic: String, key: String, message: String) {
        LOG.info("Receiced message on topic='{}' :: key='{}',value='{}'", topic, key, message)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LocalKafkaConsumerStream::class.java)
    }

    init {
        LOG.info("Starter konsumering av topics: {}", java.lang.String.join(",", topics))
        val props = Properties()
        props[StreamsConfig.APPLICATION_ID_CONFIG] = "vtp"
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
        props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
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
        val builder = StreamsBuilder()
        val stringStringConsumed = Consumed.with<String, String>(Topology.AutoOffsetReset.EARLIEST)
        topics.forEach(
            Consumer { topic: String ->
                builder
                    .stream(topic, stringStringConsumed)
                    .foreach { key: String, message: String -> handleMessage(topic, key, message) }
            }
        )
        val topology = builder.build()
        stream = KafkaStreams(topology, props)
    }
}
