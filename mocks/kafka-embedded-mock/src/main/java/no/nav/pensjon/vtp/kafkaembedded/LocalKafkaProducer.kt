package no.nav.pensjon.vtp.kafkaembedded

import no.nav.pensjon.vtp.felles.CryptoConfigurationParameters
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.StreamsConfig
import org.slf4j.LoggerFactory
import java.util.*

class LocalKafkaProducer(bootstrapServer: String?, cryptoConfigurationParameters: CryptoConfigurationParameters) {
    private val logger = LoggerFactory.getLogger(LocalKafkaProducer::class.java)
    private val producer: KafkaProducer<String, String>

    fun sendMelding(topic: String?, key: String, value: String) {
        producer.send(
            ProducerRecord(
                topic,
                key,
                value
            )
        ) { recordMetadata: RecordMetadata, _: Exception? ->
            logger.info(
                "Received new metadata: [topic: {} partition: {} offset: {}]",
                recordMetadata.topic(),
                recordMetadata.partition(),
                recordMetadata.offset()
            )
        }
        producer.flush()
    }

    fun sendMelding(topic: String?, value: String) {
        producer.send(
            ProducerRecord(
                topic,
                value
            )
        ) { recordMetadata: RecordMetadata, _: Exception? ->
            logger.info(
                "Received new metadata: [topic: {} partition: {} offset: {}]",
                recordMetadata.topic(),
                recordMetadata.partition(),
                recordMetadata.offset()
            )
        }
        producer.flush()
    }

    init {
        // Create Producer properties
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer
        props[ProducerConfig.RETRIES_CONFIG] = 15
        props[ProducerConfig.BATCH_SIZE_CONFIG] = 16384
        props[ProducerConfig.LINGER_MS_CONFIG] = 1
        props[ProducerConfig.BUFFER_MEMORY_CONFIG] = 33554432
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] =
            StringSerializer::class.java.name
        props[StreamsConfig.SECURITY_PROTOCOL_CONFIG] = "SASL_SSL"
        props[SaslConfigs.SASL_MECHANISM] = "PLAIN"
        props[SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG] = cryptoConfigurationParameters.truststorePath
        props[SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG] = cryptoConfigurationParameters.truststorePassword
        props[SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG] = cryptoConfigurationParameters.keystorePath
        props[SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG] = cryptoConfigurationParameters.keystorePassword
        val jaasTemplate =
            "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";"
        props[SaslConfigs.SASL_JAAS_CONFIG] = String.format(jaasTemplate, "vtp", "vtp")
        props[SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG] = ""

        // Create the producer
        producer = KafkaProducer(props)
    }
}
