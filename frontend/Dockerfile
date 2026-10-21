# STAGE 1: PRODUCTION BUNDLE COMPILE ENVIRONMENT
FROM node:20-alpine AS build_stage
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# STAGE 2: PRODUCTION WEB SERVER LAYER RUNTIME ENGINE
FROM nginx:1.25-alpine
COPY --from=build_stage /app/dist /usr/share/nginx/html

# Inject custom routing rules to handle clean React single page application routes natively
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
