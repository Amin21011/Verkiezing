# Stage 1: Build
FROM node:23.11.0-alpine AS builder

WORKDIR /app

# Copy package files
COPY package.json package-lock.json ./

# Zet NODE_ENV tijdelijk op development
ENV NODE_ENV=development

# Installeer alle dependencies inclusief devDependencies
RUN npm ci

# Copy rest of the source
COPY . .

# Build
RUN npm run build

# Stage 2: Serve
FROM nginx:alpine AS runner
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
