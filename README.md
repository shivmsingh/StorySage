
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
- Regisers users inside the datbase (`user_credentials` table).
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
  token = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImpvaG5AZXhhbXBsZS5jb20iLCJpYXQiOjE2NDcwMzAwNzEsImV4cCI6MTY0NzAzMzY3MX0.LO9nI7sO3wU6mZvDtWhgZ4-kpVw9DVU7nGldtZM6VW4
```

### From 3-11 Every Request requires Bearer token

| Header      | Type     | Description                 | Example           |
| :---------- | :------- | :-------------------------- |:----------------- |
| `Authorization`  | `string` | **Required**. Bearer token | "Bearer ${token}" |

#### 1. Register

```
  POST ${identity_service}/register
```

| Body Parameter | Type     | Description                | Example           |
| :------------- | :------- | :------------------------- |:----------------- |
| `name`         | `string` | **Required**. User's name | "John Doe"        |
| `password`     | `string` | **Required**. User's password | "Pass123"       |
| `email`        | `string` | **Required**. User's email | "john@example.com"|

Response
```
  user added to system
```

Sequence Diagram
<br>
<br>
![Register](https://i.ibb.co/dQhXFVt/Register.png)


#### 2. Login

```
  POST ${identity_service}/token
```

| Body Parameter | Type     | Description                | Example           |
| :------------- | :------- | :------------------------- |:----------------- |
| `username`     | `string` | **Required**. User's username | "John Doe"        |
| `password`     | `string` | **Required**. User's password | "Pass123"       |

Response
```
  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImpvaG5AZXhhbXBsZS5jb20iLCJpYXQiOjE2NDcwMzAwNzEsImV4cCI6MTY0NzAzMzY3MX0.LO9nI7sO3wU6mZvDtWhgZ4-kpVw9DVU7nGldtZM6VW4
```

Sequence Diagram
<br>
<br>
![Login](https://i.ibb.co/YNzrdyN/Login.png)

#### 3. Get Books From Catalog

```
  GET ${book_catalog}/my-books
```

Response
```json
 [
    {
        "id": 18,
        "username": "John Doe",
        "book": {
            "bookId": 1,
            "title": "The Hunger Games",
            "series": "The Hunger Games",
            "rating": 4.33,
            "description": "WINNING MEANS FAME AND FORTUNE.LOSING MEANS CERTAIN DEATH.THE HUNGER GAMES HAVE BEGUN. . . .In the ruins of a place once known as North America lies the nation of Panem, a shining Capitol surrounded by twelve outlying districts. The Capitol is harsh and c",
            "language": "English",
            "authors": [
                {
                    "id": 1,
                    "name": "Suzanne Collins"
                }
            ],
            "genres": [
                {
                    "id": 1,
                    "name": "Action"
                },
                {
                    "id": 2,
                    "name": "Adventure"
                },
                {
                    "id": 3,
                    "name": "Dystopia"
                },
                {
                    "id": 4,
                    "name": "Fantasy"
                },
                {
                    "id": 5,
                    "name": "Fiction"
                },
                {
                    "id": 6,
                    "name": "Post Apocalyptic"
                },
                {
                    "id": 7,
                    "name": "Romance"
                },
                {
                    "id": 8,
                    "name": "Science Fiction"
                },
                {
                    "id": 9,
                    "name": "Teen"
                },
                {
                    "id": 10,
                    "name": "Young Adult"
                }
            ],
            "characters": "Katniss Everdeen, Peeta Mellark, Cato (Hunger Games), Primrose Everdeen, Gale Hawthorne, Effie Trinket, Haymitch Abernathy, Cinna, President Coriolanus Snow, Rue, Flavius, Lavinia (Hunger Games), Marvel, Glimmer, Clove, Foxface, Thresh, Greasy Sae, Madge ",
            "publisher": "Scholastic Press",
            "awards": "Locus Award Nominee for Best Young Adult Book (2009), Georgia Peach Book Award (2009), Buxtehuder Bulle (2009), Golden Duck Award for Young Adult (Hal Clement Award) (2009), \"Grand Prix de lImaginaire Nominee for Roman jeunesse étranger (2010)\", Books I L",
            "numRating": 6376780,
            "likedPercent": 96,
            "setting": "Capitol, District 12, Panem, Panem, Panem (United States)",
            "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1586722975l/2767052.jpg"
        },
        "totalPages": 100,
        "pagesRead": 50,
        "progress": 50.0,
        "status": "Reading"
    }
]
```

Sequence Diagram
<br>
<br>
<img src="https://i.ibb.co/GVNgqvs/Get-User-Books.png" width="50%" height="50%" />

| Path Parameter | Type     | Description                   |
| :------------- | :------- | :---------------------------- |
| `pageSize`           | `int` | Size of Page |
| `pageNo`           | `int` | Page Number (0 based) |

#### 4. Post Books to Catalog

```
  POST ${book_catalog}/my-books
```

| Body Parameter | Type     | Description                   | Example       |
| :------------- | :------- | :---------------------------- |---------------|
| `bookId`       | `number` | **Required**. Id of the book | 123           |
| `totalPages`   | `number` | **Required**. Total pages of the book | 300     |
| `pagesRead`    | `number` | **Required**. Pages read by the user | 100        |
| `status`       | `string` | **Required**. Reading status of the book | "Reading" |


Sequence Diagram
<br>
<br>
<img src="https://i.ibb.co/YRXpGJv/Post-Books.png" alt="Post-Books" width="50%" height="50%" />

#### 5. Delete Books from Catalog

```
  DELETE ${book_catalog}/my-boos/${bookId}
```

| Path Parameter | Type     | Description                   | Example       |
| :------------- | :------- | :---------------------------- |:------------- |
| `bookId`       | `number` | **Required**. Id of the book | 123           |

Sequence Diagram
<br>
<br>
<img src="https://i.ibb.co/1RYNn3T/Delete-Books.png" alt="Delete-Books" width="50%" height="50%" />

#### 6. Get All Books

```
  GET ${book_catalog}/user-books
```
| Path Parameter | Type     | Description                   |
| :------------- | :------- | :---------------------------- |
| `pageSize`           | `int` | Size of Page |
| `pageNo`           | `int` | Page Number (0 based) |
Response
```json
  [
    {
        "bookId": 13,
        "title": "The Giving Tree",
        "series": "No series available",
        "authors": [
            {
                "id": 17,
                "name": "Shel Silverstein"
            }
        ],
        "rating": 4.37,
        "description": "\"Once there was a tree...and she loved a little boy.\"So begins a story of unforgettable perception, beautifully written and illustrated by the gifted and versatile Shel Silverstein.Every day the boy would come to the tree to eat her apples, swing from her",
        "language": "English",
        "genres": [
            {
                "id": 12,
                "name": "Childrens"
            },
            {
                "id": 13,
                "name": "Classics"
            },
            {
                "id": 4,
                "name": "Fantasy"
            },
            {
                "id": 5,
                "name": "Fiction"
            },
            {
                "id": 49,
                "name": "Juvenile"
            },
            {
                "id": 50,
                "name": "Kids"
            },
            {
                "id": 51,
                "name": "Picture Books"
            },
            {
                "id": 52,
                "name": "Poetry"
            },
            {
                "id": 53,
                "name": "Short Stories"
            },
            {
                "id": 10,
                "name": "Young Adult"
            }
        ],
        "characters": [
            "No characters available"
        ],
        "publisher": "HarperCollins Publishers",
        "awards": [
            "No awards"
        ],
        "numRating": 905731,
        "likedPercent": 94,
        "setting": [
            "No Setting Available"
        ],
        "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1174210942l/370493._SX318_.jpg",
        "status": null,
        "totalPages": 0,
        "pagesRead": 0,
        "progress": 0.0
    },
    {
        "bookId": 14,
        "title": "Wuthering Heights",
        "series": "No series available",
        "authors": [
            {
                "id": 18,
                "name": "Emily Brontë"
            },
            {
                "id": 19,
                "name": "Richard J. Dunn"
            }
        ],
        "rating": 3.86,
        "description": "You can find the redesigned cover of this edition HERE.This best-selling Norton Critical Edition is based on the 1847 first edition of the novel. For the Fourth Edition, the editor has collated the 1847 text with several modern editions and has corrected ",
        "language": "English",
        "genres": [
            {
                "id": 54,
                "name": "19th Century"
            },
            {
                "id": 25,
                "name": "Classic Literature"
            },
            {
                "id": 13,
                "name": "Classics"
            },
            {
                "id": 5,
                "name": "Fiction"
            },
            {
                "id": 55,
                "name": "Gothic"
            },
            {
                "id": 18,
                "name": "Historical"
            },
            {
                "id": 19,
                "name": "Historical Fiction"
            },
            {
                "id": 20,
                "name": "Literature"
            },
            {
                "id": 21,
                "name": "Novels"
            },
            {
                "id": 7,
                "name": "Romance"
            }
        ],
        "characters": [
            "Heathcliff",
            "Catherine Earnshaw",
            "Edgar Linton",
            "Isabella Linton",
            "Hindley Earnshaw",
            "Ellen (Nelly) Dean",
            "Mr. Lockwood",
            "Hareton Earnshaw",
            "Catherine Linton",
            "Linton Heathcliff"
        ],
        "publisher": "Norton",
        "awards": [
            "No awards"
        ],
        "numRating": 1342664,
        "likedPercent": 88,
        "setting": [
            "England",
            "Yorkshire Dales"
        ],
        "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1587655304l/6185._SY475_.jpg",
        "status": null,
        "totalPages": 0,
        "pagesRead": 0,
        "progress": 0.0
    },
    {
        "bookId": 15,
        "title": "The Da Vinci Code",
        "series": "Robert Langdon",
        "authors": [
            {
                "id": 20,
                "name": "Dan Brown"
            }
        ],
        "rating": 3.86,
        "description": "ISBN 9780307277671 moved to this edition.While in Paris, Harvard symbologist Robert Langdon is awakened by a phone call in the dead of the night. The elderly curator of the Louvre has been murdered inside the museum, his body covered in baffling symbols. ",
        "language": "English",
        "genres": [
            {
                "id": 24,
                "name": "Adult"
            },
            {
                "id": 2,
                "name": "Adventure"
            },
            {
                "id": 56,
                "name": "Crime"
            },
            {
                "id": 5,
                "name": "Fiction"
            },
            {
                "id": 19,
                "name": "Historical Fiction"
            },
            {
                "id": 57,
                "name": "Mystery"
            },
            {
                "id": 58,
                "name": "Mystery Thriller"
            },
            {
                "id": 21,
                "name": "Novels"
            },
            {
                "id": 59,
                "name": "Suspense"
            },
            {
                "id": 60,
                "name": "Thriller"
            }
        ],
        "characters": [
            "Sophie Neveu",
            "Robert Langdon",
            "Sir Leigh Teabing",
            "Silas (The Da Vinci Code)",
            "Bezu Fache",
            "Jerome Collet",
            "Manuel Aringarosa",
            "Rémy Legaludec",
            "André Vernet"
        ],
        "publisher": "Anchor",
        "awards": [
            "British Book Award for Book of the Year (2005)",
            "Book Sense Book of the Year Award for Adult Fiction (2004)",
            "\"Humos Gouden Bladwijzer (2004)\"",
            "Zilveren Vingerafdruk (2004)",
            "\"The Flume: New Hampshire Teen Readers Choice Award (2006)\"",
            "Teen Buckeye Book Awar"
        ],
        "numRating": 1933446,
        "likedPercent": 89,
        "setting": [
            "England",
            "England",
            "France",
            "London",
            "Paris (France)",
            "United Kingdom"
        ],
        "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1579621267l/968.jpg",
        "status": null,
        "totalPages": 0,
        "pagesRead": 0,
        "progress": 0.0
    }
]
```

Sequence Diagram
<br>
<br>
<img src="https://i.ibb.co/dD3xXyN/Get-All-Books.png" alt="Get-All-Books" width="50%" height="50%" />

#### 7. Get Book By Id

```
  GET ${book_catalog}/user-books/book/${id}
```

| Path Parameter | Type     | Description                   |
| :------------- | :------- | :---------------------------- |
| `id`           | `string` | **Required**. Id of book to fetch |

Response
```json
  {
    "bookId": 33,
    "title": "Harry Potter and the Sorcerer's Stone",
    "series": "Harry Potter",
    "authors": [
        {
            "id": 2,
            "name": "J.K. Rowling"
        },
        {
            "id": 3,
            "name": "Mary GrandPré"
        }
    ],
    "rating": 4.47,
    "description": "Harry Potter's life is miserable. His parents are dead and he's stuck with his heartless relatives, who force him to live in a tiny closet under the stairs. But his fortune changes when he receives a letter that tells him the truth about himself: he's a w",
    "language": "English",
    "genres": [
        {
            "id": 2,
            "name": "Adventure"
        },
        {
            "id": 11,
            "name": "Audiobook"
        },
        {
            "id": 12,
            "name": "Childrens"
        },
        {
            "id": 13,
            "name": "Classics"
        },
        {
            "id": 4,
            "name": "Fantasy"
        },
        {
            "id": 5,
            "name": "Fiction"
        },
        {
            "id": 14,
            "name": "Magic"
        },
        {
            "id": 15,
            "name": "Middle Grade"
        },
        {
            "id": 16,
            "name": "Science Fiction Fantasy"
        },
        {
            "id": 10,
            "name": "Young Adult"
        }
    ],
    "characters": [
        "Draco Malfoy",
        "Ron Weasley",
        "Petunia Dursley",
        "Vernon Dursley",
        "Dudley Dursley",
        "Severus Snape",
        "Quirinus Quirrell",
        "Rubeus Hagrid",
        "Lord Voldemort",
        "Minerva McGonagall",
        "Neville Longbottom",
        "Fred Weasley",
        "George Weasley",
        "Percy Weasley",
        "Filius Flitwick",
        "Pomona Sprou"
    ],
    "publisher": "Scholastic Inc",
    "awards": [
        "\"Mythopoeic Fantasy Award for Childrens Literature (2008)\"",
        "\"British Book Award for Childrens Book of the Year (1998)\"",
        "Prijs van de Nederlandse Kinderjury for 6-9 jaar en 10-12 jaar (2002)",
        "American Booksellers Book Of The Year  Award for Children (1999)"
    ],
    "numRating": 7048471,
    "likedPercent": 96,
    "setting": [
        "England",
        "Hogwarts School of Witchcraft and Wizardry (United Kingdom)",
        "Hogwarts School of Witchcraft and Wizardry (United Kingdom)",
        "London"
    ],
    "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1474154022l/3._SY475_.jpg",
    "status": null,
    "totalPages": 0,
    "pagesRead": 0,
    "progress": 0.0
}
```

Sequence Diagram
<br>
<br>

<img src="https://i.ibb.co/JvhbxzS/Get-Book.png" alt="Get-Book" width="50%" height="50%" />

#### 8. Get All Authors

```
  GET ${book_catalog}/authors
```
Response
```json
 [
    {
        "id": 1,
        "name": "Suzanne Collins"
    },
    {
        "id": 2,
        "name": "J.K. Rowling"
    },
    {
        "id": 3,
        "name": "Mary GrandPré"
    },
    {
        "id": 4,
        "name": "Harper Lee"
    },
    {
        "id": 5,
        "name": "Jane Austen"
    },
    {
        "id": 6,
        "name": "Anna Quindlen"
    },
    {
        "id": 7,
        "name": "Stephenie Meyer"
    },
    {
        "id": 8,
        "name": "Markus Zusak"
    },
    {
        "id": 9,
        "name": "George Orwell"
    },
    {
        "id": 10,
        "name": "Russell Baker"
    }
]
```

Sequence Diagram
<br>
<br>
<img src="https://i.ibb.co/wrnsRc9/Get-All-Authors.png" alt="Get-All-Authors" width="50%" height="50%" />

#### 9. Get Books By Author

```
  GET ${book_catalog}/authors/${id}
```

| Path Parameter | Type     | Description                   |
| :------------- | :------- | :---------------------------- |
| `id`           | `string` | **Required**. Id of author to fetch |
| `pageSize`           | `int` | Size of Page |
| `pageNo`           | `int` | Page Number (0 based) |

Response
```json
  [
    {
        "bookId": 7,
        "title": "Animal Farm",
        "series": "No series available",
        "authors": [
            {
                "id": 9,
                "name": "George Orwell"
            },
            {
                "id": 10,
                "name": "Russell Baker"
            }
        ],
        "rating": 3.95,
        "description": "Librarian's note: There is an Alternate Cover Edition for this edition of this book here.A farm is taken over by its overworked, mistreated animals. With flaming idealism and stirring slogans, they set out to create a paradise of progress, justice, and eq",
        "language": "English",
        "genres": [
            {
                "id": 13,
                "name": "Classics"
            },
            {
                "id": 3,
                "name": "Dystopia"
            },
            {
                "id": 4,
                "name": "Fantasy"
            },
            {
                "id": 5,
                "name": "Fiction"
            },
            {
                "id": 20,
                "name": "Literature"
            },
            {
                "id": 21,
                "name": "Novels"
            },
            {
                "id": 36,
                "name": "Politics"
            },
            {
                "id": 22,
                "name": "Read For School"
            },
            {
                "id": 23,
                "name": "School"
            },
            {
                "id": 8,
                "name": "Science Fiction"
            }
        ],
        "characters": [
            "Snowball",
            "Napoleon",
            "Clover",
            "Boxer",
            "Old Major",
            "Muriel",
            "Jones",
            "Squealer",
            "Moses the Raven",
            "Benjamin"
        ],
        "publisher": "Signet Classics",
        "awards": [
            "Prometheus Hall of Fame Award (2011)",
            "Retro Hugo Award for Best Novella (1996)"
        ],
        "numRating": 2740713,
        "likedPercent": 91,
        "setting": [
            "England",
            "United Kingdom"
        ],
        "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1325861570l/170448.jpg",
        "status": null,
        "totalPages": 0,
        "pagesRead": 0,
        "progress": 0.0
    },
    {
        "bookId": 81,
        "title": "1984",
        "series": "No series available",
        "authors": [
            {
                "id": 9,
                "name": "George Orwell"
            }
        ],
        "rating": 4.19,
        "description": "Among the seminal texts of the 20th century, Nineteen Eighty-Four is a rare work that grows more haunting as its futuristic purgatory becomes more real. Published in 1949, the book offers political satirist George Orwell's nightmarish vision of a totalita",
        "language": "English",
        "genres": [
            {
                "id": 24,
                "name": "Adult"
            },
            {
                "id": 13,
                "name": "Classics"
            },
            {
                "id": 3,
                "name": "Dystopia"
            },
            {
                "id": 4,
                "name": "Fantasy"
            },
            {
                "id": 5,
                "name": "Fiction"
            },
            {
                "id": 20,
                "name": "Literature"
            },
            {
                "id": 21,
                "name": "Novels"
            },
            {
                "id": 36,
                "name": "Politics"
            },
            {
                "id": 23,
                "name": "School"
            },
            {
                "id": 8,
                "name": "Science Fiction"
            }
        ],
        "characters": [
            "Winston Smith",
            "Big Brother",
            "\"OBrien\"",
            "Emmanuel Goldstein",
            "Tom Parsons",
            "Syme",
            "Julia"
        ],
        "publisher": "Houghton Mifflin Harcourt",
        "awards": [
            "Prometheus Hall of Fame Award (1984)",
            "Locus Award Nominee for All-Time Best Science Fiction Novel (1987)"
        ],
        "numRating": 3140442,
        "likedPercent": 94,
        "setting": [
            "England",
            "England",
            "London",
            "United Kingdom"
        ],
        "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1532714506l/40961427._SX318_.jpg",
        "status": null,
        "totalPages": 0,
        "pagesRead": 0,
        "progress": 0.0
    }
]
```

Sequence Diagram
<br>
<br>
<img src="https://i.ibb.co/N3zTmWD/Get-Books-By-Author.png" alt="Get-Books-By-Author" width="50%" height="50%" />


#### 10. Get All Genres

```
  GET ${book_catalog}/genres
```

Response
```json
  [
    {
        "id": 1,
        "name": "Action"
    },
    {
        "id": 2,
        "name": "Adventure"
    },
    {
        "id": 3,
        "name": "Dystopia"
    },
    {
        "id": 4,
        "name": "Fantasy"
    },
    {
        "id": 5,
        "name": "Fiction"
    },
    {
        "id": 6,
        "name": "Post Apocalyptic"
    },
    {
        "id": 7,
        "name": "Romance"
    },
    {
        "id": 8,
        "name": "Science Fiction"
    },
    {
        "id": 9,
        "name": "Teen"
    },
    {
        "id": 10,
        "name": "Young Adult"
    }
]
```

Sequence Diagram
<br>
<br>


<img src="https://i.ibb.co/XxX0gSr/Get-All-Genres.png" alt="Get-All-Genres" width="50%" height="50%" />

#### 11. Get Books By Genre

```
  GET ${book_catalog}/genres/${id}
```

| Path Parameter | Type     | Description                   |
| :------------- | :------- | :---------------------------- |
| `id`           | `string` | **Required**. Id of genre to fetch |
| `pageSize`           | `int` | Size of Page |
| `pageNo`           | `int` | Page Number (0 based) |

Response
```json
  [
    {
        "bookId": 63,
        "title": "Catch-22",
        "series": "Catch-22",
        "authors": [
            {
                "id": 83,
                "name": "Joseph Heller"
            }
        ],
        "rating": 3.98,
        "description": "The novel is set during World War II, from 1942 to 1944. It mainly follows the life of Captain John Yossarian, a U.S. Army Air Forces B-25 bombardier. Most of the events in the book occur while the fictional 256th Squadron is based on the island of Pianos",
        "language": "English",
        "genres": [
            {
                "id": 77,
                "name": "American"
            },
            {
                "id": 13,
                "name": "Classics"
            },
            {
                "id": 5,
                "name": "Fiction"
            },
            {
                "id": 18,
                "name": "Historical"
            },
            {
                "id": 19,
                "name": "Historical Fiction"
            },
            {
                "id": 48,
                "name": "Humor"
            },
            {
                "id": 20,
                "name": "Literature"
            },
            {
                "id": 21,
                "name": "Novels"
            },
            {
                "id": 99,
                "name": "Unfinished"
            },
            {
                "id": 34,
                "name": "War"
            }
        ],
        "characters": [
            "Yossarian",
            "Chaplain Tappman",
            "Milo Minderbinder",
            "Nately"
        ],
        "publisher": "Simon & Schuster",
        "awards": [
            "National Book Award Finalist for Fiction (1962)"
        ],
        "numRating": 723147,
        "likedPercent": 90,
        "setting": [
            "Pianosa (Italy)"
        ],
        "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1463157317l/168668.jpg",
        "status": null,
        "totalPages": 0,
        "pagesRead": 0,
        "progress": 0.0
    },
    {
        "bookId": 101,
        "title": "Moby-Dick or, the Whale",
        "series": "No series available",
        "authors": [
            {
                "id": 124,
                "name": "Herman Melville"
            },
            {
                "id": 125,
                "name": "Andrew Delbanco"
            }
        ],
        "rating": 3.51,
        "description": "\"It is the horrible texture of a fabric that should be woven of ships' cables and hawsers. A Polar wind blows through it, and birds of prey hover over it.\" So Melville wrote of his masterpiece, one of the greatest works of imagination in literary history.",
        "language": "English",
        "genres": [
            {
                "id": 54,
                "name": "19th Century"
            },
            {
                "id": 2,
                "name": "Adventure"
            },
            {
                "id": 77,
                "name": "American"
            },
            {
                "id": 25,
                "name": "Classic Literature"
            },
            {
                "id": 13,
                "name": "Classics"
            },
            {
                "id": 5,
                "name": "Fiction"
            },
            {
                "id": 19,
                "name": "Historical Fiction"
            },
            {
                "id": 20,
                "name": "Literature"
            },
            {
                "id": 21,
                "name": "Novels"
            },
            {
                "id": 99,
                "name": "Unfinished"
            }
        ],
        "characters": [
            "Elijah",
            "Stubb",
            "Captain Ahab",
            "Ishmael",
            "Queequeg",
            "Starbuck",
            "Tashtego",
            "Dough-boy",
            "Flask",
            "Daggoo",
            "Fedallah",
            "Pippin (\"Pip\")",
            "Moby Dick",
            "Mapple Priest",
            "Peleg",
            "Bildad",
            "Fleece",
            "Perth",
            "Peter Coffin",
            "Aunt Charity",
            "Mrs Hussey"
        ],
        "publisher": "Penguin Classics",
        "awards": [
            "Audie Award for Solo Narration - Male (2006)",
            "Премія імені Максима Рильського (1991)"
        ],
        "numRating": 479125,
        "likedPercent": 79,
        "setting": [
            "Atlantic Ocean",
            "Massachusetts (United States)",
            "Massachusetts (United States)",
            "Nantucket Island",
            "New Bedford",
            "Pacific Ocean"
        ],
        "coverImg": "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1327940656l/153747.jpg",
        "status": null,
        "totalPages": 0,
        "pagesRead": 0,
        "progress": 0.0
    }
]
```

Sequence Diagram
<br>
<br>
<img src="https://i.ibb.co/Kr2vwJD/Get-Books-By-Genre.png" alt="Get-Books-By-Genre" width="50%" height="50%" />




