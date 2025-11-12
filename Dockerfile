# Etapa 1: construir la aplicación Angular
FROM node:20 AS build
WORKDIR /app

# Copiamos archivos necesarios para instalar dependencias
COPY package*.json ./
RUN npm install

# Copiamos todo el proyecto y construimos la app
COPY . .
RUN npm run build --prod

# Etapa 2: servir la app con Nginx
FROM nginx:alpine
COPY --from=build /app/dist/ProjectV2/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto 80
EXPOSE 80

# Iniciamos Nginx
CMD ["nginx", "-g", "daemon off;"]