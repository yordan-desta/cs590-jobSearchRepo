package service.job.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
class KafkaListenersExample {

	private final Logger LOG = LoggerFactory.getLogger(KafkaListenersExample.class);

	@KafkaListener(topics = "job-topic")
	void listener(String message) {
		LOG.info("Listener [{}]", message);
	}

	@KafkaListener(topics = { "job-topic" }, groupId = "job-group")
	void commonListenerForMultipleTopics(String message) {
		LOG.info("MultipleTopicListener - [{}]", message);
	}

	@KafkaListener(topicPartitions = @TopicPartition(topic = "job-topic", partitionOffsets = {
			@PartitionOffset(partition = "0", initialOffset = "0") }), groupId = "job-group")
	void listenToParitionWithOffset(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.OFFSET) int offset) {
		LOG.info("ListenToPartitionWithOffset [{}] from partition-{} with offset-{}", message, partition, offset);
	}

//	@KafkaListener(topics = "reflectoring-bytes")
//	void listenerForRoutingTemplate(String message) {
//		LOG.info("RoutingTemplate BytesListener [{}]", message);
//	}
//
//	@KafkaListener(topics = "reflectoring-others")
//	@SendTo("reflectoring-2")
//	String listenAndReply(String message) {
//		LOG.info("ListenAndReply [{}]", message);
//		return "This is a reply sent to 'reflectoring-2' topic after receiving message at 'reflectoring-others' topic";
//	}
}
