package no.nav.pensjon.vtp.kafkaembedded

import org.apache.zookeeper.server.ServerConfig
import org.apache.zookeeper.server.ZooKeeperServerMain
import org.apache.zookeeper.server.admin.AdminServer.AdminServerException
import org.apache.zookeeper.server.quorum.QuorumPeerConfig
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal class ZooKeeperLocal(zkProperties: Properties?) {
    private val t: Thread
    private val logger = LoggerFactory.getLogger(ZooKeeperLocal::class.java)
    private val zooKeeperServer: ZooKeeperServerMain
    fun stop() {
        t.interrupt()
    }

    init {
        val quorumConfiguration = QuorumPeerConfig()
        try {
            quorumConfiguration.parseProperties(zkProperties)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        zooKeeperServer = ZooKeeperServerMain()
        val configuration = ServerConfig()
        configuration.readFrom(quorumConfiguration)
        val started = CountDownLatch(1)
        t = Thread {
            try {
                started.countDown() // here we go
                zooKeeperServer.runFromConfig(configuration)
            } catch (e: IOException) {
                logger.error("Zookeeper failed: {}", e.message)
            } catch (e: AdminServerException) {
                logger.error("Zookeeper failed: {}", e.message)
                e.printStackTrace()
            }
        }
        t.start()
        // Vent på zookeeper start
        try {
            check(started.await(5, TimeUnit.SECONDS)) { "Could not start Zookeeper in time (5 secs)" }
            Thread.sleep(1 * 1000L) // vent littegrann til på zookeeper
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}
