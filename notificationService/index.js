const express = require("express");
const { Kafka, logLevel } = require('kafkajs')
const { MongoClient } = require('mongodb');
const env = require("env2")(".env");
const { url } = require("./db.config");

const app = express();

// const { KAFKA_USERNAME: username, KAFKA_PASSWORD: password } = process.env
// const sasl = username && password ? { username, password, mechanism: 'plain' } : null
// const ssl = !!sasl

// For Production code
// const conf = {
//   clientId: 'backend',
//   brokers: [process.env.KAFKA_BROKER_URL],
//   ssl: true,
//   sasl: {
//     mechanism: 'plain' as SASLMechanism,
//     username: process.env.CONFLUENT_KAFKA_API_KEY,
//     password: process.env.CONFLUENT_KAFKA_API_SECRET,
//   },
// };
// const kafka = new Kafka(conf);


const kafka = new Kafka({
  clientId: 'notification-service',
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

const client = new MongoClient(url);
const admin = kafka.admin()

const runAdmin = async () => {
  await admin.connect()
  await admin.createTopics({
    topics: [{
      topic: "notification-topic",
      numPartitions: 3
    }]
    // {
    //   topic: "job-topic",
    //   numPartitions: 3, 
    // }, {
    //   topic: "job-seeker-skills-topic",
    //   numPartitions: 3
    // }
  }).then((msg) => console.log(`Topic Created?: ${msg}`));
  await admin.listTopics().then((data) => console.log(data))
  await admin.disconnect()
}

const consumerJob = kafka.consumer({ groupId: 'job-group' });
// const consumerJobSeeker = kafka.consumer({ groupId: 'job-seeker-group' })

const runConsumer = async () => {
  await client.connect().then((data) => {
    console.log("--------------------");
    console.log("Connected to MongoDB");
    console.log("--------------------");
  })
  await consumerJob.connect()
  await consumerJob.subscribe({ topic: 'job-topic', fromBeginning: true })

  await consumerJob.run({
  eachMessage: async ({ topic, partition, message }) => {
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