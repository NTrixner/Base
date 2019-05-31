# Base
Base repository for future Fullstack application.
This application uses [spring boot](https://spring.io/) web as a backend with either an [h2](https://www.h2database.com/html/main.html) or a [mysql](https://www.mysql.com/) database, an [angular](https://angular.io/) frontend, and an [openAPI-generator](https://github.com/OpenAPITools/openapi-generator) created swagger REST openAPI created from [javax-ws-rs](https://docs.oracle.com/javaee/7/api/javax/ws/rs/package-summary.html) annotations with [io.swagger.core](https://github.com/swagger-api/swagger-core)


## Usage
* Download, clone or copy this repository.
* Rename and refactor paths, packages, controller names etc.
* In "api", run "mvn clean install"
* In "client", run "npm install"
* In "client", run "ng serve -c development"
* In "server", run "mvn clean install"
* In "server", run "mvn spring-boot:run"

Now you can develop your new Use Cases by adding new REST methods to the API, running "mvn clean install" in api, applying the changes to the server and implementing them in the client.


## Production
* Change "spring.profiles.active" to "prod" in server/src/main/resources/application.properties
* Run "ng build -c production" in client/