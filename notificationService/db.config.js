const env = require("env2")(".env");

const {
    MONGODB_USER,
    MONGODB_PASSWORD,
    MONGODB_HOST,
    MONGODB_PORT,
    MONGODB_DATABASE,
} = process.env;

module.exports = {
    url:  `mongodb://${MONGODB_USER}:${MONGODB_PASSWORD}@${MONGODB_HOST}:${MONGODB_PORT}/${MONGODB_DATABASE}`
};