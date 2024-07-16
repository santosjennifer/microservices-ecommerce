![Last Commit](https://img.shields.io/github/last-commit/santosjennifer/microservices-ecommerce)
[![Continuous Integration with Github](https://github.com/santosjennifer/microservices-ecommerce/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/santosjennifer/microservices-ecommerce/actions/workflows/docker-publish.yml)

## E-commerce
Responsável por cadastrar produtos, categorias e clientes. Gerando pedidos e enviando e-mail de notificação para os clientes, simulando um e-commerce.

### Serviços naming-server, gateway-server, ms-authentication, ms-order, ms-product, ms-customer e ms-mail:

- **naming-server:** responsável por registrar e gerenciar a comunicação dos demais microserviços utilizando o spring eureka.
- **gateway-server:** responsável gerenciar as rotas do API Gateway, utilizando o spring security para autenticação JWT nessas rotas.
- **ms-authentication:** responsável por cadastrar os usuários e autenticar utilizando autenticação JWT.
- **ms-order:** responsável criar os pedidos, integrando com os ms de customer e product. Publica eventos em um tópico Kafka solicitando o envio de e-mails com os dados do pedido.
- **ms-customer:** responsável cadastrar e retornar os clientes e endereços.
- **ms-product:** responsável por cadastrar e retornar os produtos e categorias.
- **ms-mail:** responsável consumir os eventos no tópico do Kafka e enviar os e-mails para os clientes com os dados do pedido.

### Tecnologia

- Java 21
- Maven
- Spring Boot 3.2.5
- Spring Security
- Apache Kafka
- PostgreSQL

### Como executar o projeto:

1 - Clone o repositório:
```
git clone git@github.com:santosjennifer/microservices-ecommerce.git
```
2 - Na raiz do projeto, execute o comando:
```
docker-compose up
```
### Links Úteis

- Eureka Server:
```
http://localhost:8761
```
![image](https://github.com/santosjennifer/microservices-ecommerce/assets/90192611/3c9a0d05-2b9b-42f7-a9ee-8d6bf8799d53)

- Swagger Authentication API:
```
http://localhost:8004/swagger-ui/index.html#/
```
![image](https://github.com/santosjennifer/microservices-ecommerce/assets/90192611/ca86ee65-33d6-4652-92fd-449cce97941c)

-  Swagger Order API:
```
http://localhost:8003/swagger-ui/index.html#/
```
![image](https://github.com/santosjennifer/microservices-ecommerce/assets/90192611/5b411679-861d-4059-9a64-83915c4754cc)

- Swagger Customer API:
```
http://localhost:8002/swagger-ui/index.html#/
```
![image](https://github.com/santosjennifer/microservices-ecommerce/assets/90192611/c0ff180b-803a-4b34-b6f6-dbd114aab2dc)

- Swagger Product and Category API:
```
http://localhost:8001/swagger-ui/index.html#/
```
![image](https://github.com/santosjennifer/microservices-ecommerce/assets/90192611/4f0648bd-ab8a-4eb5-bbfd-2e40b90e5140)
