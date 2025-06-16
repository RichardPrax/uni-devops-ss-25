# uni-devops-ss-25

Uni DevOps Projekt

## Start Backend Imgae Locally

1. Start Docker Deamon
2. Start postgresql database on port 65432 => backend dir -> docker compose up (-d)
3. run command to start image with latest tag (specify tag if neccessary)

```bash
docker run -d \
 --name backend-container \
 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:65432/koerperschmiede \
 -e SPRING_DATASOURCE_USERNAME=admin \
 -e SPRING_DATASOURCE_PASSWORD=admin \
 -p 8080:8080 \
 richardprax/devops-github-backend:latest
```
