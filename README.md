# Imprimaqui

Aplicação Spring Boot para uma gráfica rápida com autenticação, catálogo de produtos, carrinho, checkout, pedidos e área administrativa, usando PostgreSQL, Thymeleaf e API REST.

## Stack

- Java 17
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Maven

## Organizacao do Projeto

```text
src/main/java/springproject/imprimaqui
|-- api
|   `-- controller
|-- config
|   |-- exception
|   `-- security
|-- domain
|   |-- order
|   |-- product
|   `-- user
|-- dto
|   |-- auth
|   |-- order
|   |-- product
|   `-- user
|-- service
`-- web
    |-- controller
    `-- dto
```

## Responsabilidades por Camada

- `api.controller`
  Endpoints REST para autenticação, usuários, produtos e pedidos.

- `web.controller`
  Controllers das páginas HTML, navegação, carrinho, checkout, meus pedidos e painéis administrativos.

- `config.security`
  Configuração do Spring Security, login form, logout, rotas públicas e redirecionamento após autenticação.

- `config.exception`
  Tratamento global de erros para API e para páginas web.

- `domain.user`
  Entidade `User` e repositório de usuários.

- `domain.product`
  Entidade `Product`, enums de tipo de impressao e tamanho de folha, e repositorio de produtos.

- `domain.order`
  Entidades `CustomerOrder` e `OrderItem`, enum `OrderStatus` e repositório de pedidos.

- `dto.auth`
  DTOs da API de autenticação.

- `dto.product`
  DTOs REST de produtos.

- `dto.order`
  DTOs REST de criação e resposta de pedidos.

- `dto.user`
  DTOs REST de resposta e atualização de usuários.

- `web.dto`
  DTOs específicos das telas web, como login, cadastro, carrinho e checkout.

- `service`
  Regras de negócio de usuários, produtos, pedidos e carrinho.

## Funcionalidades Atuais

### Autenticacao e usuarios

- cadastro web em `/cadastro`
- login web em `/login`
- logout com redirecionamento
- usuários persistidos no PostgreSQL
- senha criptografada com `PasswordEncoder`
- CRUD REST de usuarios em `/api/users`
- tela administrativa de usuarios em `/admin/users`

### Catalogo e produtos

- produtos reais persistidos no banco
- página de produtos integrada ao domínio real
- seed inicial com produtos padrão
- API REST de produtos em `/api/products`
- vitrine resumida de produtos na home

### Carrinho e checkout

- adicionar produto ao carrinho pela tela de produtos
- carrinho baseado em sessao HTTP
- atualização e remoção de itens
- calculo de frete por CEP
- checkout web com fechamento de pedido

### Pedidos e fluxo comercial

- criação de pedido real no PostgreSQL
- itens de pedido persistidos no banco
- associação do pedido ao usuario autenticado
- acompanhamento em `/meus-pedidos`
- painel administrativo de pedidos em `/admin/orders`
- atualização de status comercial do pedido

## Fluxos Principais

### Cadastro web

1. Usuário acessa `/cadastro`
2. Formulario envia `POST /cadastro`
3. `SiteController` valida `RegisterFormDTO`
4. `UserService` registra o usuário no PostgreSQL
5. A senha é criptografada antes do save
6. Usuario é redirecionado para `/login?registered`

### Login web

1. Usuario acessa `/login`
2. Formulario envia `POST /login`
3. Spring Security usa `UserService#loadUserByUsername`
4. A autenticação ocorre com email e senha do PostgreSQL
5. Se o login vier de uma rota protegida, o usuario retorna para essa rota
6. Caso contrário, o destino padrao é `/home`

### Fluxo comercial do cliente

1. Usuario navega em `/produtos`
2. Seleciona um produto e define a quantidade
3. Adiciona ao carrinho em `POST /carrinho/adicionar`
4. Revisa os itens em `/carrinho`
5. Calcula frete e fecha em `/checkout`
6. O sistema cria um pedido real no banco
7. O pedido fica visível em `/meus-pedidos`

### Fluxo operacional do pedido

1. Um pedido e criado com status `CRIADO`
2. O usuario acompanha o pedido em `/meus-pedidos`
3. A área administrativa lista pedidos em `/admin/orders`
4. O status pode evoluir para:
   - `CRIADO`
   - `EM_PROCESSAMENTO`
   - `PRONTO`
   - `ENTREGUE`
   - `CANCELADO`

### Autenticacao

- `POST /auth/register`
- `POST /auth/login`

### Usuarios

- `GET /api/users`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Produtos

- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

### Pedidos

- `GET /api/orders`
- `GET /api/orders/{id}`
- `GET /api/orders/me`
- `POST /api/orders`
- `PATCH /api/orders/{id}/status`
- `DELETE /api/orders/{id}`

## Telas Web

- `/`
- `/home`
- `/login`
- `/cadastro`
- `/produtos`
- `/carrinho`
- `/checkout`
- `/meus-pedidos`
- `/admin/users`
- `/admin/orders`

## Banco de Dados

Configuração atual em `src/main/resources/application.properties`:

- Banco: PostgreSQL
- URL: `jdbc:postgresql://localhost:5883/imprimaqui`
- ddl-auto: `update`

O projeto cria e usa tabelas de:

- usuarios
- produtos
- pedidos
- itens de pedido

## Tratamento de Erros

- API com resposta padronizada via `ApiExceptionHandler`
- Páginas web com `WebExceptionHandler`
- Página de erro dedicada em `error.html`

## Testes

O projeto possui testes para:

- subida do contexto Spring
- senha sendo salva criptografada
- regras principais de usuário
- integracao basica do fluxo de autenticação

Rodar:

```bash
mvn test
```
