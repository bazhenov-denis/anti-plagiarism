FROM node:18-alpine

WORKDIR /app

# установим все зависимости (включая dev)
COPY package.json ./
RUN npm install

# монтируем исходники, чтобы Vite-сервер видел изменения
COPY . .

EXPOSE 5173
CMD ["npm", "run", "dev"]