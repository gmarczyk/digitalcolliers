# 9am Health Backend

This is the 9am Health Backend. This repository contains the following elements:

* [service](./service/): The actual service code, a Spring Boot application
* [nginx](./nginx/): The nginx reverse proxy, acting as frontend to handle incoming traffic
* [fluentbit](./fluentbit/): A sidecar container for log distribution, based on [AWS for BluentBit](https://github.com/aws/aws-for-fluent-bit)

## Getting Started

Take the following steps to set up the project locally

### Prerequisites

Install the following software components.

* [brew](https://brew.sh/)
  ```
  curl -fsSL -o install.sh https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh
  /bin/bash install.sh
  echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> /Users/$USER/.zprofile
  eval "$(/opt/homebrew/bin/brew shellenv)"
  ```
  
* [git](https://git-scm.com/)
  ```sh
  brew install git
  ```
  
* [IntelliJ IDEA CE](https://www.jetbrains.com/idea/)
  ```sh
  brew install --cask intellij-idea-ce
  ```
  
* [Docker](https://www.docker.com/)
  ```sh
  brew install --cask docker
  ```
  Now launch the docker application in the UI and follow all steps.


* [OpenJDK](https://adoptium.net) 
  * Version: 17.0.1
  * Download the [pkg](https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.1%2B12/OpenJDK17U-jdk_x64_mac_hotspot_17.0.1_12.pkg)
  * Install the pkg


* [SequelAce](https://github.com/Sequel-Ace/Sequel-Ace)
  ```sh
  brew install --cask sequel-ace
  ```

### Launch

The following services need to be launched to run the backend:

* *[mysql](https://hub.docker.com/_/mariadb)*: A MariaDB (!) instance that serves as persistent data storage for the backend service. It listens on port 3306.
* *service*: The application itself, which is a Spring Boot application with an embedded Tomcat, listening on port 8080.
* *[nginx](https://hub.docker.com/_/nginx)*: A reverse HTTP proxy for terminating SSL traffic, listening at 443 and forwarding to *service:8080*. Usually you do not need this for local development.

To launch the backend, follow these steps:

1. Clone the project
   ```sh
   git clone git@github.com:9amHealth/backend.git
   ```
2. Start the mysql service
   ```sh
   docker-compose up mysql
   ```
3. Load an SQL dump with required data (access tokens) e.g. using Sequel Ace
   * Access Data:
     * Host: `localhost`
     * Username: `root`
     * Password: 
     * Database: `nineamhealth`
4. Start the remaining services
   ```sh
   docker-compose up
   ```
5. Verify that the backend is running
   ```sh
   curl http://localhost:8080/health
   ```
   You should see the following message:
   ```sh
   {"status":"Hello 9am.health!"}
   ```

Congratulations, you are now able to successfully run the backend :partying_face:!

## Useful SQL Functions

To have possibility manually add records to DB will be useful to register these functions:

* UUID2BIN
  ```sql
  CREATE FUNCTION UUID2BIN(uuid CHAR(36))
  RETURNS BINARY(16) DETERMINISTIC
  RETURN UNHEX(REPLACE(UPPER(uuid), "-", ""));
  ```

* BIN2UUID
  ```sql
  CREATE FUNCTION BIN2UUID(id BINARY(16))
  RETURNS char(36) CHARSET utf8mb4 DETERMINISTIC
  RETURN LOWER(CONCAT(SUBSTRING(HEX(id), 1, 8), "-", SUBSTRING(HEX(id), 9, 4), "-", SUBSTRING(HEX(id), 13, 4), "-", SUBSTRING(HEX(id), 17, 4), "-", SUBSTRING(HEX(id), 21, 12)));
  ```
