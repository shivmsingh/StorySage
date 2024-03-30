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

