#Base
Will update once it's done!

Execute the `mvn -DskipTests package` task to get a runnable jar in the target directory of the server module.
(Note: `mvn clean package` will not work since the client resources will be removed from the out folder
by the clean task).

The runnable jar can be exectuted with `java -jar server/target/server-1.0-SNAPSHOT.jar`
