require("dotenv").config();
const express = require("express");
const bodyParser = require("body-parser");
const { Client } = require('@elastic/elasticsearch')
const client = new Client({
    node: process.env.ES_ADDRESS,
    auth: {
        username: process.env.ES_USERNAME,
        password: process.env.ES_PASSWORD
    }
})
const { Kafka } = require("kafkajs");
const { json } = require("body-parser");
const recruiterRoutes = require("./routes/recruiter");
const app = express();

app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use('/api/seeker/search', recruiterRoutes);

app.use((req, res, next) => {
    console.log(req.url, req.body, req.method, req.params);
    next();
});

const kafka = new Kafka({
    clientId: process.env.RECRUITERSEARCH_SERVICE_APP,
    brokers: [process.env.KAFKA]
});

const gid = process.env.RECRUITERSEARCH_SERVICE_GROUP;
const consumer = kafka.consumer({ groupId: gid + Date.now() });

const run = async() => {
    await consumer.connect();
    await consumer.subscribe({
        topic: process.env.JOBSEARCH_SERVICE_TOPIC,
        fromBeginning: true
    })

    await consumer.run({
        eachMessage: async({ topic, partion, message }) => {
            console.log("Recieved message topic: " + topic + "message: " + message.value.toString());
            try {
                const data = JSON.parse(message.value.toString());
                client.index({
                    index: process.env.ELASTICINDEX,
                    body: data
                }).then(res => {
                    console.log(topic, JSON.parse(message.value.toString()));
                }).catch(err => console.log(err));
            } catch (error) {
                console.log("unable to serialize object: " + message.value.toString());
            }

        }
    });
}

run().then(() => {
    console.log("Done")
}, err => { console.log(err) });

app.listen(process.env.PORT || 5000, () => {
    console.log("express app is runing on port " + process.env.PORT);
});