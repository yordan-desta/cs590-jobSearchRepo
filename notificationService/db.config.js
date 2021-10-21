const {
    MONGODB_USER,
    MONGODB_PASSWORD,
    MONGODB_HOST,
    MONGODB_PORT,
    MONGODB_DATABASE,
} = process.env;

console.log(`MongoDB URL: mongodb://${MONGODB_HOST}:${MONGODB_PORT}/${MONGODB_DATABASE}`);

module.exports = {
    url: `mongodb://${MONGODB_HOST}:${MONGODB_PORT}/${MONGODB_DATABASE}`
};