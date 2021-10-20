const sendgrid = require('@sendgrid/mail');
const SENDGRID_API_KEY = "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"

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
