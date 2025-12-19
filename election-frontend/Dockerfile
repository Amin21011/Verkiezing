# Stage 1: Build

FROM node:20-bullseye AS build

ENV NPM_CONFIG_CACHE=/tmp/.npm
ENV npm_config_loglevel=warn
ENV npm_config_fund=false
ENV npm_config_audit=false

WORKDIR /app

COPY package*.json ./

RUN rm -rf /root/.npm \
&& npm cache clean --force \
&& npm install --no-progress

COPY . .

RUN npm run build
 
# Stage 2: Serve

FROM nginx:alpine AS runner
 
COPY --from=builder /app/dist /usr/share/nginx/html
 
# NOTE: The default Nginx image listens on port 80. 

# If you haven't provided a custom nginx.conf, you should EXPOSE 80

EXPOSE 80
 
CMD ["nginx", "-g", "daemon off;"]
 