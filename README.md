# Online Book Retail Application - [![Build Status](https://app.travis-ci.com/enesoral/online-book-retail.svg?branch=main)](https://app.travis-ci.com/enesoral/online-book-retail)

## Design & Information

- Used **package by feature** structure to achieve high-cohesion, low-coupling and maximum encapsulation. 
Please, [see](https://medium.com/sahibinden-technology/package-by-layer-vs-package-by-feature-7e89cde2ae3a) my article about this.
- Locked services optimistically to ensure consistency
- Created retry mechanism using AOP in case of OptimisticLockFailureException
- Used kafka pub-sub approach to update statistics asynchronously

## Tech Stack
- Spring Boot, MongoDB, Apache Kafka, Docker, JUnit, Docker

## Running services locally
- **mvn clean install** <br/> **docker-compose up --build**