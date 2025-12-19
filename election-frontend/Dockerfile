# Stage 1: Build

FROM node:23-alpine AS builder

ENV NPM_CONFIG_CACHE=/tmp/.npm
ENV npm_config_loglevel=warn

WORKDIR /app

COPY package*.json ./
RUN npm cache clean --force && npm install --no-audit --no-fund --legacy-peer-deps

COPY . .
RUN npm run build
 
# Stage 2: Serve

FROM nginx:alpine AS runner
 
COPY --from=builder /app/dist /usr/share/nginx/html
 
# NOTE: The default Nginx image listens on port 80. 

# If you haven't provided a custom nginx.conf, you should EXPOSE 80

EXPOSE 80
 
CMD ["nginx", "-g", "daemon off;"]
 