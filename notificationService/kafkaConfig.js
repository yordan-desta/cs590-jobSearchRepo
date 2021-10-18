
const env = require("env2")(".env");
const { logLevel } = require('kafkajs');
// const { KAFKA_USERNAME: username, KAFKA_PASSWORD: password } = process.env
// const sasl = username && password ? { username, password, mechanism: 'plain' } : null
// const ssl = !!sasl

// For Production code
// const conf = {
//   clientId: 'backend',
//   brokers: KAFKA.bootstrapServers,
//   ssl: true,
//   sasl: {
//     mechanism: 'plain' as SASLMechanism,
//     username: ENV.CONFLUENT_KAFKA_API_KEY,
//     password: ENV.CONFLUENT_KAFKA_API_SECRET,
//   },
// };
// const kafka = new Kafka(conf);
// const producer = kafka.producer({
//   createPartitioner: Partitioners.JavaCompatiblePartitioner,
// });
// await producer.connect();



const KafkaConfig = {
    clientId: 'notification-service',
    brokers: ['localhost:9092'],
    ssl: false,
    connectionTimeout: 3000,
    requestTimeout: 25000,
    logLevel: logLevel.ERROR,
    retry: {
        initialRetryTime: 100,
        retries: 8
    }
}

exports.default = KafkaConfig
