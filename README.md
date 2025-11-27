# 🔐 Authentication REST API

API RESTful robusta para gerenciamento de autenticação e usuários desenvolvida com Spring Boot, implementando JWT (JSON Web Tokens), Spring Security e HATEOAS.

## 🚀 Tecnologias

- Java 17+
- Spring Boot 3.x
- Spring Security 6.x
- Spring Data JPA
- JWT (JSON Web Token) com JJWT
- Spring HATEOAS
- Bean Validation
- Lombok
- BCrypt Password Encoder
- H2 Database (ou seu banco de dados preferido)

## 📋 Funcionalidades

- ✅ Registro de usuários com validações
- ✅ Login com geração de JWT
- ✅ Autenticação stateless com tokens
- ✅ Autorização baseada em roles (USER, ADMIN)
- ✅ Consulta de perfil do usuário autenticado
- ✅ Atualização de dados do usuário
- ✅ Exclusão de conta
- ✅ Criptografia de senhas com BCrypt
- ✅ Proteção contra duplicação de email/username
- ✅ HATEOAS (links navegáveis)
- ✅ Tratamento global de exceções
- ✅ Auditoria automática (timestamps)

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas com segurança integrada:

```
├── config/          # Configurações (Spring Security)
├── controller/      # Endpoints REST
├── service/         # Lógica de negócio
├── repository/      # Acesso a dados
├── entity/          # Entidades JPA
├── dtos/            # Objetos de transferência de dados
├── security/        # JWT, filtros e UserDetails
├── exceptions/      # Exceções customizadas
└── enums/           # Enumerações (Roles)
```

## 🔒 Segurança

### Autenticação JWT

A API utiliza tokens JWT para autenticação stateless:

1. **Registro/Login** → Gera um JWT válido por tempo configurável
2. **Requisições** → Token enviado no header `Authorization: Bearer <token>`
3. **Validação** → Filtro intercepta e valida o token automaticamente
4. **Sessões** → Não há sessões no servidor (STATELESS)

### Roles e Permissões

- **USER**: Role padrão para usuários comuns
- **ADMIN**: Role para administradores (expansível)

### Criptografia

Senhas são criptografadas com BCrypt antes de serem armazenadas no banco.

## 📡 Endpoints

### 🔓 Endpoints Públicos (sem autenticação)

#### Registrar Usuário
```http
POST /auth/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "username": "usuario123",
  "password": "senha123"
}
```

**Resposta:** `201 Created`
```json
{
  "id": 1,
  "email": "usuario@example.com",
  "username": "usuario123",
  "createdIt": "2025-11-27T10:30:00",
  "role": ["USER"],
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "senha123"
}
```

**Resposta:** `200 OK`
```json
{
  "id": 1,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "usuario@example.com",
  "username": "usuario123",
  "roles": ["USER"],
  "createdIt": "2025-11-27T10:30:00"
}
```

### 🔐 Endpoints Protegidos (requerem autenticação)

**Header obrigatório:**
```
Authorization: Bearer <seu-token-jwt>
```

#### Consultar Perfil
```http
GET /users/me
```

**Resposta:** `200 OK`
```json
{
  "id": 1,
  "username": "usuario123",
  "email": "usuario@example.com",
  "role": ["USER"],
  "createdAt": "2025-11-27T10:30:00",
  "_links": {
    "me": { "href": "http://localhost:8080/users/me" },
    "atualizar": { "href": "http://localhost:8080/users/me" },
    "deletar": { "href": "http://localhost:8080/users/me" }
  }
}
```

#### Atualizar Usuário
```http
PUT /users/me
Content-Type: application/json

{
  "username": "novo_username",
  "password": "novaSenha123"
}
```

**Resposta:** `200 OK`
```json
{
  "id": 1,
  "username": "novo_username",
  "email": "usuario@example.com"
}
```

#### Deletar Conta
```http
DELETE /users/me
```

**Resposta:** `204 No Content`

## ⚠️ Tratamento de Erros

A API retorna respostas padronizadas para diferentes cenários:

| Status | Erro | Descrição |
|--------|------|-----------|
| 400 | Bad Request | Dados de entrada inválidos |
| 401 | Unauthorized | Token inválido ou ausente |
| 403 | Forbidden | Sem permissão para a operação |
| 404 | Not Found | Recurso não encontrado |
| 409 | Conflict | Email ou username já existente |

### Exemplo de Resposta de Erro
```json
{
  "timestamp": "2025-11-27T10:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Email já está em uso."
}
```

## 🔒 Regras de Negócio

### Registro
- Email deve ser único e válido
- Username deve ser único, com 6-50 caracteres
- Senha deve ter no mínimo 6 caracteres
- Todo novo usuário recebe automaticamente a role `USER`
- Token JWT é gerado automaticamente no registro

### Login
- Validação de credenciais via Spring Security
- Geração de novo token JWT a cada login
- Token contém email e roles do usuário

### Atualização
- Usuário só pode atualizar seus próprios dados
- Username deve ser único se alterado
- Senha é sempre criptografada antes de salvar
- Email não pode ser alterado

### Exclusão
- Usuário só pode deletar sua própria conta
- Operação é irreversível

## 🛠️ Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Configuração

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/auth-api.git
cd auth-api
```

2. Configure o `application.properties`:
```properties
# Banco de Dados
spring.datasource.url=jdbc:h2:mem:authdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# JWT Configuration
jwt.secret=SEU_SECRET_BASE64_AQUI_MINIMO_256_BITS
jwt.expiration=86400000
```

**⚠️ IMPORTANTE:** Gere um secret seguro para JWT:
```bash
# No terminal (Linux/Mac)
openssl rand -base64 32

# Ou use um gerador online: https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx
```

3. Execute o projeto:
```bash
mvn spring-boot:run
```

4. Acesse a API em: `http://localhost:8080`

## 🧪 Testando a API

### Fluxo Completo com cURL

**1. Registrar um usuário:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "username": "teste123",
    "password": "senha123"
  }'
```

**2. Fazer login (se já registrado):**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "password": "senha123"
  }'
```

**3. Salvar o token retornado:**
```bash
TOKEN="seu_token_aqui"
```

**4. Consultar perfil (com autenticação):**
```bash
curl -X GET http://localhost:8080/users/me \
  -H "Authorization: Bearer $TOKEN"
```

**5. Atualizar dados:**
```bash
curl -X PUT http://localhost:8080/users/me \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "novo_nome",
    "password": "novaSenha123"
  }'
```

**6. Deletar conta:**
```bash
curl -X DELETE http://localhost:8080/users/me \
  -H "Authorization: Bearer $TOKEN"
```

## 📦 Estrutura do Token JWT

O token JWT gerado contém:

```json
{
  "sub": "usuario@example.com",
  "role": "ROLE_USER",
  "iat": 1234567890,
  "exp": 1234654290
}
```

- **sub**: Email do usuário (subject)
- **role**: Roles do usuário
- **iat**: Data de emissão (issued at)
- **exp**: Data de expiração

## 🔐 Validações

### RegisterRequestDTO
- **email**: obrigatório, formato válido, máximo 100 caracteres
- **username**: obrigatório, 6-50 caracteres, único
- **password**: obrigatório, mínimo 6 caracteres, máximo 100

### LoginRequestDTO
- **email**: obrigatório, formato válido
- **password**: obrigatório

### UpdateRequestDTO
- **username**: obrigatório, 6-50 caracteres
- **password**: obrigatório, 6-100 caracteres

## 🎯 Recursos Avançados

### HATEOAS
A API implementa HATEOAS nos endpoints de usuário, fornecendo links navegáveis para descoberta dinâmica de operações.

### Auditoria
Timestamp automático de criação (`createdAt`) usando `@CreationTimestamp`.

### Transações
Todas as operações de escrita são transacionais com `@Transactional`.

### Segurança de Sessão
Configuração STATELESS - sem sessões no servidor, totalmente baseado em JWT.

## 🛡️ Segurança

Esta API implementa as seguintes práticas de segurança:

- ✅ Senhas nunca armazenadas em texto plano (BCrypt)
- ✅ Tokens JWT assinados e validados
- ✅ CSRF desabilitado (apropriado para APIs stateless)
- ✅ Sessões desabilitadas (STATELESS)
- ✅ Validação de entrada com Bean Validation
- ✅ Proteção contra SQL Injection (JPA)
- ✅ Separação de roles e permissões

**⚠️ Para produção:**
- Use HTTPS obrigatório
- Configure secrets em variáveis de ambiente
- Implemente rate limiting
- Adicione logs de segurança
- Configure CORS adequadamente

## 📚 Documentação Adicional

### Spring Security
- [Documentação Oficial](https://docs.spring.io/spring-security/reference/)

### JWT
- [JWT.io](https://jwt.io/)
- [JJWT Library](https://github.com/jwtk/jjwt)

### HATEOAS
- [Spring HATEOAS](https://docs.spring.io/spring-hateoas/docs/current/reference/html/)

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.

## 👤 Autor

[Kayk Murphy]([https://github.com/seu-usuario](https://github.com/KaykMurphy))

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!

## 💡 Dica de Uso

Esta API pode ser integrada com:
- Frontend React/Angular/Vue
- Mobile apps (Android/iOS)
- Outras APIs que precisam de autenticação
- Microserviços que requerem validação de tokens JWT
