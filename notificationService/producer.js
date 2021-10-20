const { Kafka, logLevel } = require("kafkajs");
const kafka = new Kafka({
  clientId: process.env.KAFKA_CLIENT_ID,
  brokers: [process.env.KAFKA_BROKER_URL],
  ssl: false,
  connectionTimeout: 3000,
  requestTimeout: 25000,
  logLevel: logLevel.ERROR,
  retry: {
    initialRetryTime: 100,
    retries: 8
  }
})

const producer = kafka.producer()
const Producer = async (message) => {
  console.log("Notification Producer");
  await producer.connect()
  await producer.send({
    topic: process.env.NOTIFICATION_TOPIC,
    messages: [
      { value: JSON.stringify({user_id: message.user_id, subject: "New Vacancy opened for you", body: "Go and login to check out this opportunity"}) },
    ],
  });
}

module.exports = Producer