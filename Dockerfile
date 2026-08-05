# Imagen base con Node
FROM node:18

# Directorio de trabajo
WORKDIR /app

# Copiar dependencias primero (mejora cache)
COPY app/package.json .

# Instalar dependencias
RUN npm install

# Copiar el resto del código
COPY app/ .

# Exponer puerto
EXPOSE 3000

# Comando de ejecución
CMD ["node", "index.js"]