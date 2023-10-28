# photowall-be
This repository contains the backend code for photowall application.

## Docker commands to run containers

### DATABASE
- `docker run --name db -d --rm --network photowall-net -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=photowall mysql`

### BACKEND
- `docker run --name be --rm -p 8080:8080 --network photowall-net -e DB_IP=db -e DB_PORT=3306 -e DB_USER=root -e DB_PASSWORD=root ashutoshraturi/photowall-be`
- NOTE:- First pull the image then run the container

### FRONTEND
- `docker run --name ui -it --rm -p 3000:3000 ashutoshraturi/photowall-ui`
