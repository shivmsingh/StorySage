# Story Sage

Story Sage is a comprehensive platform tailored for readers, offering detailed book information including descriptions, genres, and more. It offers features such as adding, tracking reading progress, and discovering authors and genres.
Users can also explore personalized recommendations based on content preferences, enhancing their reading experience with in-depth insights and tailored suggestions


## Tech Stack

**Server:** Spring Cloud Gateway, Spring Security, Spring DataJPA, Spring Webflux

**Message Queue:** Apache Kafka

**Database:** MySQL

**Tools:** Docker
## Architecture

![Architecture](https://i.ibb.co/pz7cPHX/untitled.png)

### GATEWAY

**Functions** :

- Routes the requests to appropriate microservices based on the request url.
- Checks if the request contains bearer token and validates it.
- Extracts the `username` from the bearer token and adds it as header to the header of the downstream request (which goes to the appropriate microservice).
- Works as the single entry-point for all the microservices.

### IDENTITY SERVICE

**Functions** :

- Incorporates Spring Boot Starter Security to protect end points.
- Registers users inside the database (`user_credentials` table).
- Utilizes BCrypt to hash the passwords
- Provides end-points to generate and validate `Bearer token`.

### BOOK CATALOG

**Functions** :

- Retrieves a list of all the books from `all_books` table.
- Allows users to add books from  `all_books` table to `user_books` table.
- Gives users the facility to track their reading progress by providing ways to record their pages read and generates the total percentage.
- Extracts all the books present inside user's book catalog.
- Provides functionality to get a list of all the authors, all the genres, get authors by Id and get genres by Id.
- Pushes messages inside the Kafka `Recommendation` topic whenever a book is added or removed from the `user_books` table.

### RECOMMENDATION SERVICE

**Functions** :

- Gives Recommendations based on books
- Actively listens to the Kafka `Recommendation` topic.
- Retrives the messages from the topic and generates recommendations by doing an API call to hugging face model (currently mock server in place) and persists the result inside `recommendations` table if the method is `SAVE`, removes the recommendations if method is `DELETE`.
## API Contract

#### Variables

```bash
  book_catalog = http://localhost:8083/bookcatalog
  identity_service = http://localhost:8083/auth
```


#### 1. Register

```http
  POST {identity_service}/register
```

| Body Parameter | Type     | Description                | Example           |
| :------------- | :------- | :------------------------- |:----------------- |
| `name`         | `string` | **Required**. User's name | "John Doe"        |
| `password`     | `string` | **Required**. User's password | "Pass123"       |
| `email`        | `string` | **Required**. User's email | "john@example.com"|

![Register](https://i.ibb.co/dQhXFVt/Register.png)

#### 2. Login

```http
  POST {identity_service}/token
```

| Body Parameter | Type     | Description                | Example           |
| :------------- | :------- | :------------------------- |:----------------- |
| `username`     | `string` | **Required**. User's username | "John Doe"        |
| `password`     | `string` | **Required**. User's password | "Pass123"       |

#### 3. Get All Books

```http
  GET /user-books
```

| Header      | Type     | Description                 | Example           |
| :---------- | :------- | :-------------------------- |:----------------- |
| `username`  | `string` | **Required**. User's username | "john@example.com" |

#### 4. Get Book By Id

```http
  GET /user-books/book/${id}
```

| Path Parameter | Type     | Description                   |
| :------------- | :------- | :---------------------------- |
| `id`           | `string` | **Required**. Id of book to fetch |

#### 5. Get All Authors

```http
  GET /authors
```

| Header      | Type     | Description                 | Example           |
| :---------- | :------- | :-------------------------- |:----------------- |
| `username`  | `string` | **Required**. User's username | "john@example.com" |

#### 6. Get Book By Author

```http
  GET /authors/${id}
```

| Path Parameter | Type     | Description                   |
| :------------- | :------- | :---------------------------- |
| `id`           | `string` | **Required**. Id of author to fetch |

#### 7. Get All Genres

```http
  GET /genres
```

| Header      | Type     | Description                 | Example           |
| :---------- | :------- | :-------------------------- |:----------------- |
| `username`  | `string` | **Required**. User's username | "john@example.com" |

#### 8. Get Book By Genre

```http
  GET /genres/${id}
```

| Path Parameter | Type     | Description                   |
| :------------- | :------- | :---------------------------- |
| `id`           | `string` | **Required**. Id of genre to fetch |

#### 9. Get My Books

```http
  GET /my-books
```

| Header      | Type     | Description                 | Example           |
| :---------- | :------- | :-------------------------- |:----------------- |
| `username`  | `string` | **Required**. User's username | "john@example.com" |

#### Post Books

```http
  POST /my-books
```

| Body Parameter | Type     | Description                   | Example       |
| :------------- | :------- | :---------------------------- |:------------- |
| `bookId`       | `number` | **Required**. Id of the book | 123           |
| `totalPages`   | `number` | **Required**. Total pages of the book | 300     |
| `pagesRead`    | `number` | **Required**. Pages read by the user | 100        |
| `status`       | `string` | **Required**. Reading status of the book | "Reading" |

#### 10. Delete Books

```http
  DELETE /user-books
```

| Body Parameter | Type     | Description                   | Example       |
| :------------- | :------- | :---------------------------- |:------------- |
| `bookId`       | `number` | **Required**. Id of the book | 123           |
| `totalPages`   | `number` | **Required**. Total pages of the book | 300     |
| `pagesRead`    | `number` | **Required**. Pages read by the user | 100        |
| `status`       | `string` | **Required**. Reading status of the book | "Reading" |
