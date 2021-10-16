package service.notification.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
class InitSend {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private KafkaSenderExample kafkaSenderExample;
	
	@Autowired
	private KafkaSenderWithMessageConverter messageConverterSender;
	
	@Value("${kafka.notification-topic}")
	private String topic1;
	
	@EventListener
	void initiateSendingMessage(ApplicationReadyEvent event) throws InterruptedException {
		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendMessage("I'll be recevied by MultipleTopicListener, Listener & ClassLevel KafkaHandler", topic1);
		
		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendMessageWithCallback("I'll get a asyc Callback", "reflectoring-others");
		
		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendMessageWithCallback("I'm sent using RoutingTemplate", "reflectoring-bytes");
		
		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendMessage("I will get reply back from @SendTo", "reflectoring-others");
	}
}
