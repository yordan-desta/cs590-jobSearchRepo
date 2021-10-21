require("dotenv").config();
const express = require("express");
const router = express.Router();
const { Client } = require('@elastic/elasticsearch');
const { query } = require("express");
const client = new Client({ node: process.env.ES_ADDRESS })


router.get("/", async(req, res) => {
    await client.indices.refresh({ index: process.env.ELASTICINDEX })
    let query = { index: process.env.ELASTICINDEX }
    if (req.query.seeker) query.q = `*${req.query.seeker}*`;
    await client.search(query).then(response => {
        return res.status(200).json({
            jobs: response.body.hits.hits
        })
    });
});

// router.get('/:id', async (req, res) => {
//     let query = { index: process.env.ELASTICINDEX, id: req.params.id }
//     await client.get(query).then(response => res.send(response.body));
// });

// router.get('/:searchterm', async (req, res) => {
//     await client.indices.refresh({ index: process.env.ELASTICINDEX })
//     await client.search({
//         index: process.env.ELASTICINDEX,
//         q: req.params.searchterm
//     }).then(response => {
//         return res.status(200).json({
//             jobs: response.body.hits.hits
//         })
//     });
// });

module.exports = router;