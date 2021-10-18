const express = require("express");
const { Kafka, logLevel } = require('kafkajs')
const env = require("env2")(".env");

const app = express();

// const { KAFKA_USERNAME: username, KAFKA_PASSWORD: password } = process.env
// const sasl = username && password ? { username, password, mechanism: 'plain' } : null
// const ssl = !!sasl

// For Production code
// const conf = {
//   clientId: 'backend',
//   brokers: KAFKA.bootstrapServers,
//   ssl: true,
//   sasl: {
//     mechanism: 'plain' as SASLMechanism,
//     username: ENV.CONFLUENT_KAFKA_API_KEY,
//     password: ENV.CONFLUENT_KAFKA_API_SECRET,
//   },
// };
// const kafka = new Kafka(conf);
// const producer = kafka.producer({
//   createPartitioner: Partitioners.JavaCompatiblePartitioner,
// });
// await producer.connect();

const kafka = new Kafka({
  clientId: 'notification-service',
  brokers: ['localhost:9092'],
  ssl: false,
  connectionTimeout: 3000,
  requestTimeout: 25000,
  logLevel: logLevel.ERROR,
  retry: {
      initialRetryTime: 100,
      retries: 8
  }
})

const admin = kafka.admin()

const runAdmin = async () => {
  await admin.connect()
  await admin.listTopics().then((data) => console.log(data))
  await admin.disconnect()
}

const producer = kafka.producer()

const runProducer = async (message) => {
  await producer.connect()
  await producer.send({
    topic: 'notification-topic',
    messages: [
      { value: 'Hello KafkaJS!!' },
    ],
  })
  
  await producer.disconnect()
}

const consumer = kafka.consumer({ groupId: 'job-group' })

const runConsumer = async () => {
  await consumer.connect()
  await consumer.subscribe({ topic: 'new-job-topic', fromBeginning: true })

  await consumer.run({
  eachMessage: async ({ topic, partition, message }) => {
      // Run Producer
      runProducer()
      console.log({
      value: message.value.toString(),
      })
  },
  })
}

// Run Admin
runAdmin()

// Run Consumer
runConsumer()


//health check
app.get('/', async (req, res) => {
    res.send('Ok From Notification Service')
}, []);

const port = process.env.PORT
app.listen(port, () => {
    console.log(`Payment Service listening at http://localhost:${port}`)
})