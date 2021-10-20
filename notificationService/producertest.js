const env = require("env2")(".env");
const { Kafka, logLevel } = require("kafkajs");

// console.log(process.env.KAFKA_BROKER_URL)
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

const Producer = async () => {
  await producer.connect()
  await producer.send({
    topic: process.env.JOB_TOPIC,
    messages: [
      { value: JSON.stringify({user_id:2,skills:"najed, salah, test"}) },
    ],
  });
}

Producer();