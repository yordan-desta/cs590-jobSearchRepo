require("dotenv").config();
const express = require("express");
const bodyParser = require("body-parser");
const elasticsearch = require("elasticsearch");
const { Kafka } = require("kafkajs");
const app = express();

app.use(express.urlencoded({extended: true})); 
app.use(express.json());

app.use((req, res, next) => {
    console.log(req.url, req.body, req.method, req.params);
    next();
});

const kafka = new Kafka({
    clientId: process.env.JOBSEARCH_SERVICE_APP,
    brokers: [process.env.KAFKA1]
  });

const esClient = elasticsearch.Client({
    host: process.env.ES_ADDRESS
});

const consumer = kafka.consumer({ groupId: process.env.JOBSEARCH_SERVICE_GROUP });

const run = async () => {
    await consumer.connect();
    await consumer.subscribe({ topic: process.env.JOBSEARCH_SERVICE_TOPIC, fromBeginning: true })

    await consumer.run({
        eachMessage: async({ topic, partition, message}) => {
            console.log({ partition, offset: message.offset, value: message.value.toString()})
        }
    })
}



app.listen(process.env.PORT || 4000, () => {
    console.log("express app is runnn.........");
});
