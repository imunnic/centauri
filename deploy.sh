#!/bin/bash

# Hacer git pull en la carpeta raíz
echo "Haciendo git pull en la carpeta raíz..."
git pull
if [ $? -ne 0 ]; then
  echo "Error al hacer git pull en la carpeta raíz."
  exit 1
fi
echo "construyendo api"
gradle build
if [$? -ne 0 ]; then
  echo "Error en el build"
  exit
fi
# Ejecutar docker-compose en la carpeta raíz
echo "Construyendo y desplegando los contenedores en la carpeta raíz..."
docker compose up -d --build
if [ $? -ne 0 ]; then
  echo "Error al construir y desplegar los contenedores en la carpeta raíz."
  exit 1
fi

# Entrar en la carpeta frontend
echo "Cambiando a la carpeta frontend..."
cd frontend
if [ $? -ne 0 ]; then
  echo "Error al cambiar a la carpeta frontend."
  exit 1
fi

# Ejecutar docker-compose en la carpeta frontend
echo "Construyendo y desplegando los contenedores en la carpeta frontend..."
docker compose up -d --build
if [ $? -ne 0 ]; then
  echo "Error al construir y desplegar los contenedores en la carpeta frontend."
  exit 1
fi

echo "Proceso completado exitosamente."
