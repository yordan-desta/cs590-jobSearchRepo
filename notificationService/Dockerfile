FROM node:12-alpine
WORKDIR .
COPY ./ /
RUN npm i
CMD ["node", "--max_old_space_size=4112", "index.js"]