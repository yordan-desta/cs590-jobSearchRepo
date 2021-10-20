require("dotenv").config();
const express = require("express");
const { Kafka } = require("kafkajs");
const sendEmail = require("./sendgrid");
const app = express();

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

app.use((req, res, next) => {
    console.log(req.url, req.body, req.method, req.params);
    next();
});

const kafka = new Kafka({
    clientId: process.env.EMAIL_SERVICE_APP,
    brokers: [process.env.KAFKA]
});

const gid = process.env.EMAIL_SERVICE_GROUP;
const consumer = kafka.consumer({ groupId: gid + Date.now() });

const run = async() => {
    await consumer.connect();
    await consumer.subscribe({
        topic: process.env.EMAIL_SERVICE_TOPIC,
        fromBeginning: true
    })

    await consumer.run({
        eachMessage: async({ topic, partion, message }) => {
            const data = JSON.parse(message.value.toString());
            const { to, subject, text} = data;
            sendEmail(to, subject, text);
        }
    });

    // const producer = kafka.producer();
    // await producer.connect();

    // // Wait 5 second before sending a new message
    // await new Promise(resolve => setTimeout(resolve, 5000));

    // const emailbody = { to: "yordan.desta@gmail.com", subject: "test subject", text: "test body" };
    // await producer.send({
    //   topic: "email-topic",
    //   messages: [
    //     {value: JSON.stringify(emailbody)}
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