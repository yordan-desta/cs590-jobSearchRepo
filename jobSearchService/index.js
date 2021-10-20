require("dotenv").config();
const express = require("express");

const { Client } = require('@elastic/elasticsearch');

const client = new Client({
    node: process.env.ES_ADDRESS,
    auth: {
        username: process.env.ES_USERNAME,
        password: process.env.ES_PASSWORD
    }
})
const { Kafka } = require("kafkajs");
const { json } = require("body-parser");
const jobsRoutes = require("./routes/jobs");
const app = express();

app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use('/api/job/search', jobsRoutes);

app.use((req, res, next) => {
    console.log(req.url, req.body, req.method, req.params);
    next();
});

const kafka = new Kafka({
    clientId: process.env.JOBSEARCH_SERVICE_APP,
    brokers: [process.env.KAFKA1]
});

const gid = process.env.JOBSEARCH_SERVICE_GROUP;
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

    // const producer = kafka.producer();
    // await producer.connect();

    // // Wait 5 second before sending a new message
    // await new Promise(resolve => setTimeout(resolve, 5000));

    // const job = { name: "amharic developer", level: "senior" };
    // await producer.send({
    //   topic: "job-creation2",
    //   messages: [
    //     {value: JSON.stringify(job)}
    //   ]
    // });
}

// const printData = () => {
//   let query = { index: 'job8', id: 'OfAxlnwBaCQkgn22pZJZ'}
//   console.log("-------");
//   client.get(query).then(res => console.log(res));
//   console.log("-------");
// }

run().then(() => {
    //printData();
    console.log("Done")
}, err => { console.log(err) });

app.listen(process.env.PORT || 4000, () => {
    console.log("express app is running on port " + process.env.PORT);
});