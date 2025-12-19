# Stage 1: Build
FROM node:23.11.0-alpine AS builder
WORKDIR /app

# Copy package files
COPY package.json package-lock.json ./

# Installeer ALLE dependencies (inclusief Tailwind)
RUN npm ci

# Copy source code
COPY . .

# Build frontend
RUN npm run build

# Stage 2: Serve met Nginx
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
