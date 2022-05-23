# Online Book Retail Application - [![Build Status](https://app.travis-ci.com/enesoral/online-book-retail.svg?branch=main)](https://app.travis-ci.com/enesoral/online-book-retail)

## Design & Information

- Used **package by feature** structure to achieve high-cohesion, low-coupling and maximum encapsulation. <br/>
Please, [see](https://medium.com/sahibinden-technology/package-by-layer-vs-package-by-feature-7e89cde2ae3a) my article about this.
- Built CI pipeline to detect any errors immediately
- Locked services optimistically to ensure consistency
- Created retry mechanism using AOP in case of OptimisticLockFailureException
- Used kafka pub-sub approach to update statistics asynchronously
- Wrote [well-structured](https://cbea.ms/git-commit/) commit messages to ease understanding changes

## Tech Stack
- Spring Boot, MongoDB, Apache Kafka, Docker, JUnit, Docker

## Running services locally
- **mvn clean install -DskipTests** <br/> **docker-compose up --build** <br/>
- Postman collection can be found in root directory
- Swagger-ui will be available here: http://localhost:8080/swagger-ui/index.html