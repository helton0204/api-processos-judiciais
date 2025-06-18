# ⚖️ API de Gestão de Processos Judiciais

Este é um projeto Java com Spring Boot, desenvolvido como desafio técnico para simular um sistema de gestão de processos judiciais e agendamento de audiências. A API expõe endpoints RESTful para cadastro, consulta e agendamento, seguindo regras de negócio específicas do domínio jurídico.

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- H2 Database (em memória)
- Maven
- Swagger (Springdoc OpenAPI)

---

## 📘 Funcionalidades

- Cadastro e consulta de processos judiciais
- Filtros por status e comarca
- Agendamento de audiências (sem sobreposição de horários/local)
- Consulta de agenda de audiências por comarca e data
- Validações de formato, status e datas úteis

---

## 🔐 Autenticação

A API utiliza autenticação via **JWT**. Após login, um token válido é gerado com base em uma chave secreta segura definida em `application.properties`.

---

## ▶️ Executando o Projeto

### 1. Clone o repositório (ou posicione-se na raiz do projeto)

```
git clone <repo-url>
cd api-processos-judiciais
```

### 2. Execute o projeto

Via Maven:

```
mvn spring-boot:run
```

Ou, se preferir, gere o JAR e execute:

```
mvn clean package
java -jar target/api-processos-judiciais-1.0-SNAPSHOT.jar
```

A aplicação estará disponível em:

- http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

---

## 🧪 Testes

Os testes automatizados podem ser executados com:

```
mvn test
```

---

## 📄 Documentação da API

A documentação interativa está disponível via Swagger em:

- http://localhost:8080/swagger-ui.html

---

## 📬 Exemplos de Requisições

- Coleção Postman: `CompleteAPI.postman_collection.json` (na raiz do projeto)
- Exemplos também disponíveis na interface Swagger

---

## 📝 Observações

- O número do processo é validado por regex conforme padrão nacional.
- Não é permitido agendar audiências para processos arquivados ou suspensos.
- Audiências só podem ser marcadas em dias úteis (segunda a sexta).

---

## 👨‍💻 Autor

**Helton Oliveira**
