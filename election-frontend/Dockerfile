# =========================================
# Stage 1: Build the Vue.js Application
# =========================================
ARG NODE_VERSION=23.11.0-alpine

FROM node:${NODE_VERSION} AS builder

# Set working directory
WORKDIR /app

# Copy package files first (voor caching)
COPY package.json package-lock.json ./

# Install dependencies
RUN --mount=type=cache,target=/root/.npm npm ci

# Copy rest of the source code
COPY . .

# Build the Vue.js app
RUN npm run build

# =========================================
# Stage 2: Serve with Nginx
# =========================================
FROM nginx:alpine AS runner

# Copy build output to Nginx default html folder
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose port 8080
EXPOSE 8080

# Start Nginx in foreground
CMD ["nginx", "-g", "daemon off;"]
