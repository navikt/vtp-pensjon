package no.nav.pensjon.vtp.client.testcontainers

import no.nav.pensjon.vtp.client.VtpPensjonClient
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.output.OutputFrame
import org.testcontainers.utility.DockerImageName
import java.util.function.Consumer

open class VtpPensjonContainer(image: String = defaultVtpPensjonImage()) : GenericContainer<VtpPensjonContainer>(image) {
    init {
        exposedPorts = listOf(8060, 8389)
    }

    private var embeddedMongoDBContainer: MongoDBContainer? = null

    /**
     * Creates a MongoDB Container that will be stopped and started together with the lifecycle of this container
     * @param logConsumer optional log consumer where the logs of the Mongo DB container should be sent
     */
    fun withEmbeddedMongoDB(
        logConsumer: Consumer<OutputFrame>? = null,
    ): VtpPensjonContainer {
        val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
            .withNetwork(Network.newNetwork())
            .withNetworkAliases("mongodb")

        logConsumer?.let(mongoDBContainer::withLogConsumer)

        embeddedMongoDBContainer = mongoDBContainer

        withMongoDb(mongoDBContainer)

        return self()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun withMongoDb(container: MongoDBContainer): VtpPensjonContainer = dependsOn(container)
        .withNetwork(container.network)
        .withEnv("SPRING_DATA_MONGODB_HOST", container.networkAliases[0])

    fun baseUrl(): String {
        return "http://${host}:${getMappedPort(8060)}"
    }

    fun ldapUrl(): String {
        return "ldap.url=ldap://$host:${getMappedPort(8389)}"
    }

    override fun stop() {
        super.stop()
        embeddedMongoDBContainer?.stop()
    }

    fun client(
        azureAdClientId: String? = null,
        azureAdIssuer: String? = null,
        issoIssuer: String? = null,
        maskinportenIssuer: String? = null,
        stsIssuer: String? = null,
    ) = VtpPensjonClient(
        baseUrl = baseUrl(),
        azureAdClientId = azureAdClientId,
        azureAdIssuer = azureAdIssuer,
        issoIssuer = issoIssuer,
        maskinportenIssuer = maskinportenIssuer,
        stsIssuer = stsIssuer,
    )

    companion object {
        internal fun defaultVtpPensjonImage() = "ghcr.io/navikt/vtp-pensjon/vtp-pensjon:2022.04.01-09.34-b9cb0ceb52e4"
    }
}
