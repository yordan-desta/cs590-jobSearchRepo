require('dotenv').config();
const express = require('express');
const { createProxyMiddleware, fixRequestBody } = require('http-proxy-middleware');
const cors = require('cors')

const app = express();

app.use(cors());
app.options('*', cors());

app.use(express.urlencoded({ extended: true }));
app.use(express.json());

app.use((req, res, next) => {
    if (req.url !== '/')
        console.log(req.url, req.method, req.body, req.params, req.query);
    next();
});

const jobsSearchAPi = process.env.JOB_SEARCH_API;
const candidateSearchApi = process.env.CANDIDATE_SEARCH_API;
const jobsAPi = process.env.JOB_API;
const accountsApi = process.env.ACCOUNT_API;
const candidateAPI = process.env.CANDIDATE_API;

const jobSearchProxy = createProxyMiddleware({
    target: process.env.JOB_SEARCH_ENDPOINT,
    pathRewrite: { jobsSearchAPi: '/' },
    changeOrigin: true,
    onProxyReq: fixRequestBody
});
const candidateProxy = createProxyMiddleware({
    target: process.env.CANDIDATE_ENDPOINT,
    pathRewrite: { candidateAPI: '/' },
    changeOrigin: true,
    onProxyReq: fixRequestBody
});;
const candidateSearchProxy = createProxyMiddleware({
    target: process.env.CANDIDATE_SEARCH_ENDPOINT,
    pathRewrite: {
        candidateSearchApi: '/'
    },
    changeOrigin: true,
    onProxyReq: fixRequestBody
});
const jobsProxy = createProxyMiddleware({
    target: process.env.JOBS_ENDPOINT,
    pathRewrite: { jobsAPi: '/' },
    changeOrigin: true,
    onProxyReq: fixRequestBody
});
const accountsProxy = createProxyMiddleware({
    target: process.env.ACCOUNTS_ENDPOINT,
    pathRewrite: { accountsApi: '/' },
    changeOrigin: true,
    onProxyReq: fixRequestBody
});

app.use(jobsSearchAPi, jobSearchProxy);
app.use(candidateSearchApi, candidateSearchProxy);
app.use(jobsAPi, jobsProxy);
app.use(accountsApi, accountsProxy);
app.use(candidateAPI, candidateProxy);

app.get('/', (req, res) => {
    res.json("working");
});

const server = app.listen(process.env.PORT, function() {
    console.log(`listening on port ${server.address().port}`)
});