# Etapa 1 — build do Flutter Web
FROM ghcr.io/cirruslabs/flutter:stable AS build
WORKDIR /app

# Cache de dependências: copia só os manifests primeiro.
COPY pubspec.* ./
RUN flutter pub get

# Copia o resto do projeto e gera o build web otimizado.
COPY . .
RUN flutter build web --release

# Etapa 2 — servir os arquivos estáticos com nginx
FROM nginx:alpine
COPY --from=build /app/build/web /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
