FROM nginx:1.25.3-alpine

COPY selfsigned.crt /etc/nginx/ssl/selfsigned.crt
COPY private.key /etc/nginx/ssl/private.key

COPY nginx.conf /etc/nginx/nginx.conf