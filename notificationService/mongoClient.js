const { MongoClient } = require('mongodb');
const { url } = require("./db.config");
const mongoClient = new MongoClient(url);

module.exports = mongoClient;