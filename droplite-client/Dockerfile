# Step 1: Build React app
FROM node:22 AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

# Step 2: Use Nginx to serve the static files
FROM nginx:stable-alpine
COPY --from=build /app/build /usr/share/nginx/html

# Expose port 80 for Nginx
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]