const express = require("express");
const { Kafka, logLevel } = require('kafkajs')
const { MongoClient } = require('mongodb');
const env = require("env2")(".env");
const { url } = require("./db.config");

const app = express();

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

const mongoClient = new MongoClient(url);
const admin = kafka.admin()

const runAdmin = async () => {
  await admin.connect()
  await admin.createTopics({
    topics: [{
      topic: "notification-topic"
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
const consumerJobSeeker = kafka.consumer({ groupId: 'job-seeker-group' })

const runJobConsumer = async () => {
  await mongoClient.connect().then((data) => {
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
  await mongoClient.close()
}

const runJobSeekerConsumer = async () => {
  await mongoClient.connect().then((data) => {
    console.log("--------------------");
    console.log("Connected to MongoDB");
    console.log("--------------------");
  })
  await consumerJobSeeker.connect()
  await consumerJobSeeker.subscribe({ topic: 'job-seeker-skills-topic', fromBeginning: true })

  await consumerJobSeeker.run({
    //EachBatch for saving users??
    eachMessage: async ({ topic, partition, message }) => {
      mongoClient.db().collection().insertOne({ user_id: message.user_id, skills: message.skills })
      console.log({
        value: message.value.toString(),
      })
    },
  })
  await mongoClient.close()
}

// Run Admin
runAdmin()

// Run Job Consumer 
runJobConsumer()

// Run Job Consumer 
runJobSeekerConsumer()

//health check
app.get('/', async (req, res) => {
  res.send('Ok From Notification Service')
}, []);

const port = process.env.PORT
app.listen(port, () => {
  console.log(`Payment Service listening at http://localhost:${port}`)
})