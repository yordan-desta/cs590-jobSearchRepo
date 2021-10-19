FROM node:14

WORKDIR /searchapiapp
COPY package*.json ./
RUN npm install
EXPOSE 2121
CMD ["node", "index.js"]