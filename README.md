image docker:
docker pull mysql:8.0.37-debian
docker run --name mysql:8.0.37 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.37-debian
https://hub.docker.com/_/mysql

// Connect to mysql
Step 1: Add into table PERMISSION
Step 2: Add into table ROLE
Step 3: Run app
mvn spring-boot:run -Dspring.profiles.active=local