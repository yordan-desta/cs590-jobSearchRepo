FROM node:alpine3.10

WORKDIR /searchapiapp
COPY package*.json ./
RUN npm install
EXPOSE 8000
CMD ["node", "index.js"]