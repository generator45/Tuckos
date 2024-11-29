# Tuckos

## Table of contents

1. [Resources](#resources)
2. [Project setup](#project-setup)
3. [Databse schema](#database-schema)

## Resources

- Rest API in spring boot tutorial [link](https://medium.com/javajams/creating-a-rest-api-in-spring-boot-68ce785f652f).

- Java Persistence API documentation [link](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html).

- SQL tutorial [link](https://www.w3schools.com/sql/default.asp).

## Project setup

1. Install maven for build automation and dependency management.

2. Install postgreSQL for database connection.

3. Setup application.properties file by adding the variables:

   - **`spring.datasource.url`**: postgres db connection url
   - **`spring.datasource.username`**: postgres username
   - **`spring.datasource.password`**: password for the user

4. To run project locally:

   ```bash
    mvn spring-boot:run -Dspring-boot.run.fork=true
   ```

## Database Schema

### Student Table

| Column      | Data Type    | Constraints   |
| ----------- | ------------ | ------------- |
| rollNo      | VARCHAR(15)  | `PRIMARY KEY` |
| password    | VARCHAR(255) |               |
| studentName | VARCHAR(255) |               |

### Item Table

| Column   | Data Type    | Constraints   |
| -------- | ------------ | ------------- |
| itemId   | INT          | `PRIMARY KEY` |
| itemName | VARCHAR(255) |               |
| price    | INT          |               |
| itemDesc | VARCHAR(255) |               |
| imgUrl   | VARCHAR(255) |               |

### Inventory Table

| Column   | Data Type | Constraints                                              |
| -------- | --------- | -------------------------------------------------------- |
| itemId   | INT       | `PRIMARY KEY`, `FOREIGN KEY` REFERENCES **Item(itemId)** |
| quantity | INT       |                                                          |

### OrderHistory Table

| Column       | Data Type   | Constraints                                  |
| ------------ | ----------- | -------------------------------------------- |
| orderId      | INT         | `PRIMARY KEY`                                |
| rollNo       | VARCHAR(15) | `FOREIGN KEY` REFERENCES **Student(rollNo)** |
| order_date   | DATE        |                                              |
| order_status | BOOLEAN     |                                              |

### OrderItem Table

| Column   | Data Type | Constraints                                                                         |
| -------- | --------- | ----------------------------------------------------------------------------------- |
| orderId  | INT       | `PRIMARY KEY` (orderId, itemId), `FOREIGN KEY` REFERENCES **OrderHistory(orderId)** |
| itemId   | INT       | `PRIMARY KEY` (orderId, itemId), `FOREIGN KEY` REFERENCES **Item(itemId)**          |
| quantity | INT       |                                                                                     |
| price    | INT       |                                                                                     |
