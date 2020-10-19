<p align="center">
  <h3 align="center">web-crawler</h3>
  <p align="center">
    A simple web crawler via REST API
  </p>
</p>

<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)

<!-- ABOUT THE PROJECT -->
## About The Project
A simple web crawler via REST API with 2 endpoints
1. Perform an async web crawl on a given url, if the given url has already a record of crawl, no crawl action will be made.
All crawled data are persisted in H2 database. Input is validated if url is valid and a minimum of depth equal to 1.
2. Retrieve all web crawl saved in memory

### Built With

* Java 8
* Spring (Boot, Web, Data JPA, Validation)
* Gradle - dependency
* jsoup - html parser

<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

* Java 8
* Gradle

### Local Execution

1. Build the project using Gradle wrapper
```sh
./gradlew clean build
```
2. Run the jar file
```sh
java -jar build/libs/web-crawler-0.0.1.jar
```

## Usage

#### Perform a crawl on a given URL
```
curl --location --request POST 'http://localhost:8080/crawls' \
--header 'Content-Type: application/json' \
--data-raw '{
	"url": "https://www.google.com/",
	"depth": 2
}'
```

#### Retrieve all crawled pages
```
curl --location --request GET 'http://localhost:8080/crawls'
```