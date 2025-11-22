# AuthSystem Pro – API de Autenticação Completa

API RESTful de autenticação e gerenciamento de usuários desenvolvida com **Spring Boot 3** e as melhores práticas de segurança (nível produção).

### Funcionalidades
- Registro e login com **JWT (Bearer Token)**
- Criptografia de senhas com **BCrypt**
- Controle de acesso baseado em **roles** (ADMIN / USER) usando `@ElementCollection`
- Filtro JWT customizado + Spring Security 6 (stateless)
- Endpoints protegidos para visualizar, atualizar e deletar próprio perfil (`/users/me`)
- Implementação de **HATEOAS** com links dinâmicos nos responses
- Validação robusta com **Bean Validation**
- Exceções customizadas + respostas padronizadas
- Persistência com **PostgreSQL** (configurável para H2 em testes)

### Endpoints principais
- POST   /auth/register → cria usuário + retorna token
- POST   /auth/login    → autentica + retorna token
- GET    /users/me      → perfil do usuário autenticado (com links HATEOAS)
- PUT    /users/me      → atualiza username e senha
- DELETE /users/me      → deleta próprio usuário

- ### Tecnologias
- Java 17+
- Spring Boot 3
- Spring Security 6 + JWT (JJWT)
- Spring Data JPA + Hibernate
- PostgreSQL
- Lombok + Maven
- HATEOAS
- BCryptPasswordEncoder

### Como rodar
```bash
git clone https://github.com/KaykMurphy/authsystem-pro.git
cd authsystem-pro/restFull
./mvnw spring-boot:run
