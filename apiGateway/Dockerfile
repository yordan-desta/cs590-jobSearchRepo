FROM node:alpine3.10
WORKDIR /apiapp
COPY . /apiapp/
RUN npm install
EXPOSE 8080
ENTRYPOINT [ "node", "index.js" ]