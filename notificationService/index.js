const express = require("express");
const env = require("env2")(".env");
const { Kafka, logLevel } = require('kafkajs')
const mongoClient = require("./mongoClient")
const Producer = require("./producer")
const app = express();

mongoClient.connect().then((data) => {
    console.log("--------------------");
    console.log("Connected to MongoDB");
    console.log("--------------------");
})

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

const admin = kafka.admin()

const runAdmin = async () => {
  await admin.connect()
  await admin.createTopics({
    topics: [{
      topic: process.env.EMAIL_TOPIC
    }]
  }).then((msg) => console.log(`Topic Created?: ${msg}`));
  await admin.listTopics().then((data) => console.log(data))
  await admin.disconnect()
}

const consumerJob = kafka.consumer({ groupId: process.env.JOB_GROUP });
const consumerJobSeeker = kafka.consumer({ groupId: process.env.JOB_SEEKER_SKILLS_GROUP })

const runJobConsumer = async () => {
  await consumerJob.connect()
  await consumerJob.subscribe({ topic: process.env.JOB_TOPIC, fromBeginning: true })

  await consumerJob.run({
    eachMessage: async ({ topic, partition, message }) => {
      // var query = { skills: message.tags };
      mongoClient.db().collection("jobseekers").find().toArray(function(err, result) {
        if (err) throw err;
        console.log(result);
        for (ele of result) {
          Producer(ele);
        }
      });
      console.log({
        value: message.value.toString(),
      })
    },
  })
}

const runJobSeekerConsumer = async () => {
  await consumerJobSeeker.connect()
  await consumerJobSeeker.subscribe({ topic: process.env.JOB_SEEKER_SKILLS_TOPIC, fromBeginning: true })

  await consumerJobSeeker.run({
    eachMessage: async ({ topic, partition, message }) => {
      let result = JSON.parse(message.value.toString());
      mongoClient.db().collection("jobseekers").insertOne({ user_id: result.user_id, skills: result.skills }).catch((e)=>{
          console.log(e);
      })
      console.log({user_id: result.user_id, skills: result.skills })
    },
  })
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