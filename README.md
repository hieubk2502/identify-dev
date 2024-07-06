image docker:
docker pull mysql:8.0.37-debian
docker run --name mysql:8.0.37 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.37-debian

https://hub.docker.com/_/mysql