FROM rabbitmq:3.8.17-management-alpine
COPY rabbit-definitions.json /etc/rabbitmq/definitions.json