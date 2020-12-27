package no.nav.pensjon.vtp.kafkaembedded

import kafka.server.KafkaConfig
import kafka.server.KafkaServerStartable
import org.slf4j.LoggerFactory
import java.util.*

internal class KafkaLocal(kafkaProperties: Properties?, zkProperties: Properties) {
    private val logger = LoggerFactory.getLogger(KafkaLocal::class.java)
    private var kafka: KafkaServerStartable? = null
    private var zookeeper: ZooKeeperLocal? = null

    private fun startZookeeper(zkProperties: Properties) {
        logger.info("starting local zookeeper...")
        zookeeper = ZooKeeperLocal(zkProperties)
    }

    private fun startKafka(kafkaConfig: KafkaConfig) {
        kafka = KafkaServerStartable(kafkaConfig)
        logger.info("starting local kafka broker...")
        kafka!!.startup()
    }

    fun stop() {
        logger.info("stopping kafka...")
        kafka!!.shutdown()
        zookeeper!!.stop()
    }

    init {
        val kafkaConfig = KafkaConfig(kafkaProperties)
        startZookeeper(zkProperties)
        startKafka(kafkaConfig)
    }
}
