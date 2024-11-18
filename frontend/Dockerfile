# Usa una imagen base oficial de Node.js para construir la aplicación
FROM node:16 as build-stage

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos del proyecto a la carpeta de trabajo
COPY package*.json ./
COPY . .

# Instala las dependencias
RUN npm install

#Corrige errores de dependencias
RUN npm audit fix

# Construye la aplicación para producción
RUN npm run build

# Usa una imagen ligera de Nginx para servir el build
FROM nginx:alpine as production-stage

# Copia el build generado en la etapa anterior al directorio que Nginx usa para servir el contenido estático
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Expone el puerto 80 del contenedor
EXPOSE 80

# Comando por defecto para iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]
