# Stage 1: Build

FROM node:23-alpine AS builder
 
WORKDIR /app
 
# Copy package files

# We use package-lock.json* to make the lockfile optional in case you delete it

COPY package.json package-lock.json* ./
 
# Ensure we are in development mode so devDependencies (Tailwind, Vite) are installed

#ENV NODE_ENV=development
 
# CHANGED: Use 'npm install' to ensure Linux binaries are fetched correctly

# avoiding platform mismatches from the lockfile

RUN npm ci
 
# Copy rest of the source

COPY . .
 
# Build the application

RUN npm run build
 
# Stage 2: Serve

FROM nginx:alpine AS runner
 
COPY --from=builder /app/dist /usr/share/nginx/html
 
# NOTE: The default Nginx image listens on port 80. 

# If you haven't provided a custom nginx.conf, you should EXPOSE 80.

EXPOSE 80
 
CMD ["nginx", "-g", "daemon off;"]
 