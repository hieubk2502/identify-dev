image docker:
docker pull mysql:8.0.37-debian
docker run --name mysql:8.0.37 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.37-debian
https://hub.docker.com/_/mysql

// Connect to mysql
Step 1: Add into table PERMISSION
Step 2: Add into table ROLE
Step 3: Run app
mvn spring-boot:run -Dspring.profiles.active=local

// Build jar
mvn package
mvn package -DskipTests

// Run file jar
java -jar *.jar

// docker
docker exec -it [COntainer] sh
cat /etc/os-release

Build image:
docker build -t identity-service:0.0.1 .

Run container:
docker run -d identity-service:0.0.1

RUN Container with config properties
docker run --name identity-service -p 8080:8080 -e DBMS_CONNECTION= jdbc:mysql://${DBMS_IP_CONNECT:localhost}:3306/identify identify.dev-0.0.1-SNAPSHOT

or 

// create network
docker  network create my-network
// mount when run image
docker run --network my-network --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.37-debian

docker run --name identity-service --network my-network -p 8080:8080 -e DBMS_CONNECTION= jdbc:mysql://${DBMS_IP_CONNECT:localhost}:3306/identify identify.dev-0.0.1-SNAPSHOT

// push image
docker image push {name_dockerhub}/identify
