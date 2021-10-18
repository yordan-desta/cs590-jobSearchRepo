const { Kafka } = require("kafkajs");
const KafkaConfig = require("./kafkaConfig");

console.log(KafkaConfig)
const kafka = new Kafka(KafkaConfig)
const producer = kafka.producer()

const produce = async () => {
  await producer.connect()
  await producer.send({
    topic: 'notification-topic',
    messages: [
      { value: 'Hello KafkaJS!!' },
    ],
  });
}

exports.default = produce