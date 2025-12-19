# Stage 1: Build

# Multi-stage build for Vue 3 application

# Stage 1: Build the application
FROM node:22-alpine AS build
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm ci

# Copy source code and build
COPY . .
RUN npm run build
 
# Stage 2: Serve

FROM nginx:alpine AS runner
 
COPY --from=build /app/dist /usr/share/nginx/html
 
# NOTE: The default Nginx image listens on port 80. 

# If you haven't provided a custom nginx.conf, you should EXPOSE 80

EXPOSE 80
 
CMD ["nginx", "-g", "daemon off;"]
 