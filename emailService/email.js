const nodemailer = require('nodemailer');
var transporter = nodemailer.createTransport({
      service: "gmail",
      host: "smtp.gmail.com",
      auth: {
          user: "eaappointmentsystem@gmail.com",
          pass: "5r5J)N_`&w?33n"
      }
  });
const sendMail = function(mailOptions){
   return new Promise(function (resolve, reject){
      transporter.sendMail(mailOptions, (err, info) => {
         if (err) {
            console.log("error: ", err);
            reject(err);
         } else {
            console.log(`Mail sent successfully!`);
            resolve(info);
         }
      });
   });
}

async function mailTo(to, subject, text){
    const mailOptions = {
        from: {
            name: 'Appintment System',
            address:  'eaappointmentsystem@gmail.com'
        },
        to: to,
        subject: subject,
        text: text
        }
    await sendMail(mailOptions);
    console.log("Finish")
}

exports.handler = async (event, context) => {
 try{
   //event.Records.forEach(record =>
   for( let record of event.Records){
    let { body } = record;
    body = JSON.parse(body);
    console.log(body);
   const mailOptions = {
              from: {
                  name: 'Appintment System',
                  address:  'eaappointmentsystem@gmail.com'
              },
              to: body.to,
              subject: body.subject,
              text: body.body
              }
   await sendMail(mailOptions);
    console.log("Finish")
  };
  return {};
 }catch(e){
   console.log(e)
 }
}

module.exports = mailTo;