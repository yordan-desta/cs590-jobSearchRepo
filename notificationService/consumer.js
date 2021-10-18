const { Kafka } = require('kafkajs');
const KafkaConfig  = require("./kafkaConfig");

const kafka = new Kafka(KafkaConfig);
const consumer = kafka.consumer({ groupId: 'notification-group' })

const run = async () => {
    await consumer.connect()
    await consumer.subscribe({ topic: 'notification-topic', fromBeginning: true })

    await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
        console.log({
        value: message.value.toString(),
        })
    },
    })
}
exports.default = () => run();