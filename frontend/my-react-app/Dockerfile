FROM node:18-alpine

WORKDIR /app

# 1. копируем ТОЛЬКО package.json + lock, ставим deps
COPY package.json package-lock.json ./
RUN npm ci          # ставит ровно то, что описано в lock

# 2. копируем исходники (последние), но node_modules уже готовы
COPY . .

EXPOSE 5173
CMD ["npm", "run", "dev"]
