package no.nav.pensjon.vtp.kafka;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.kafkaembedded.LocalKafkaProducer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Api(tags = "Kafka services")
@RequestMapping("/api/kafka")
public class KafkaRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaRestTjeneste.class);

    private final LocalKafkaProducer localKafkaProducer;
    private final AdminClient kafkaAdminClient;

    public KafkaRestTjeneste(LocalKafkaProducer localKafkaProducer, AdminClient kafkaAdminClient) {
        this.localKafkaProducer = localKafkaProducer;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @GetMapping(value = "/topics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = ("Returnerer kafka topics"), response = ArrayList.class)
    public ResponseEntity getTopics() throws InterruptedException, ExecutionException {
        ArrayList<KafkaTopicDto> list = new ArrayList<>();
        Map<String, TopicListing> topics = kafkaAdminClient.listTopics().namesToListings().get();
        for (Map.Entry<String, TopicListing> entry : topics.entrySet()) {
            KafkaTopicDto dto = new KafkaTopicDto();
            dto.setName(entry.getKey());
            dto.setInternal(entry.getValue().isInternal());
            list.add(dto);
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/topics/{topic}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = ("Oppretter ny (tom) Kafka topic."))
    public ResponseEntity createTopic(@PathVariable("topic") String topic) {
        LOG.info("Request: oppretter topic: {}", topic);
        kafkaAdminClient.createTopics(Collections.singleton(new NewTopic(topic, 1, (short) 1)));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(value = "/send/{topic}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = ("Legger melding på Kafka topic"))
    public ResponseEntity sendMessage(@PathVariable("topic") String topic, @RequestParam("key") String key, String message) {
        LOG.info("Request: send message to topic [{}]: {}", topic, message);
        localKafkaProducer.sendMelding(topic, key, message);

        return ResponseEntity.ok(message);
    }
}
