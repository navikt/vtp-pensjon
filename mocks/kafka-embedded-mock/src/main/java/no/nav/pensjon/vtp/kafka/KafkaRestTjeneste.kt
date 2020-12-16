package no.nav.pensjon.vtp.kafka

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.kafkaembedded.LocalKafkaProducer
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.ArrayList

@Api(tags = ["Kafka services"])
@RequestMapping("/rest/api/kafka")
class KafkaRestTjeneste(private val localKafkaProducer: LocalKafkaProducer, private val kafkaAdminClient: AdminClient) {
    private val logger = LoggerFactory.getLogger(KafkaRestTjeneste::class.java)

    @GetMapping(value = ["/topics"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "", notes = "Returnerer kafka topics", response = ArrayList::class)
    fun listTopics(): List<KafkaTopicDto> {
        return kafkaAdminClient.listTopics().namesToListings().get()
            .map { (key, value) ->
                KafkaTopicDto(
                    name = key,
                    internal = value.isInternal
                )
            }
    }

    @PostMapping(value = ["/topics/{topic}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "", notes = "Oppretter ny (tom) Kafka topic.")
    fun createTopic(@PathVariable("topic") topic: String): ResponseEntity<Any> {
        logger.info("Request: oppretter topic: {}", topic)
        kafkaAdminClient.createTopics(setOf(NewTopic(topic, 1, 1.toShort())))
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build()
    }

    @PostMapping(value = ["/send/{topic}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "", notes = "Legger melding på Kafka topic")
    fun sendMessage(@PathVariable("topic") topic: String, @RequestParam("key") key: String, message: String): String {
        logger.info("Request: send message to topic [{}]: {}", topic, message)
        localKafkaProducer.sendMelding(topic, key, message)
        return message
    }
}
