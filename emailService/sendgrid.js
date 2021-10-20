require("dotenv").config();
const sendgrid = require('@sendgrid/mail');
const SENDGRID_API_KEY = process.env.SENDGRID_API_KEY

function sendEmail(to, subject, text){
    sendgrid.setApiKey(SENDGRID_API_KEY)

    const msg = {
    to: to,
    from: 'meyoseph@gmail.com',
    subject: subject,
    text: text
    }

    sendgrid
    .send(msg)
    .then((resp) => {
        console.log('Email sent\n', resp)
    })
    .catch((error) => {
        console.error(error)
    })
}

module.exports = sendEmail;
