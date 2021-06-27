## Build executable jar
Execute the `mvn -DskipTests package` task to get a runnable jar in the target directory.
(Note: `mvn clean package` will not work since the client resources will be removed from the out folder
by the clean task).

The runnable jar can be execture with `java -jar target/server-1.0-SNAPSHOT.jar`



## Docker image
When you want to build a docker-image from your application execute the following commands in order
- `mvn clean`
- `mvn -DskipTests package`
- `mvn --projects server -DskipTests spring-boot:build-image`

The created image will have the name `eu.trixner/base`.

### Pushing to docker repo
If you, additionally want to push the image to a docker repo you need to execute
the following command instead of the last one up there
- `mvn --projects server -Pprod -DskipTests spring-boot:build-image`

If you do this, you need to set the following environment vairables 
  - env.dockerUsername 
  - env.dockerPassword 
  - env.dockerUrl
