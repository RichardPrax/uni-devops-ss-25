# uni-devops-ss-25

Uni DevOps Projekt

## Deploy local with Docker

### Start Backend Image Locally

1. Start Docker Deamon
2. Start postgresql database on port 5432 => backend dir -> docker compose up (-d)
3. run command to start image with latest tag (specify tag if neccessary)

```bash
docker run -d \
 --name backend-container \
 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/koerperschmiede \
 -e SPRING_DATASOURCE_USERNAME=admin \
 -e SPRING_DATASOURCE_PASSWORD=admin \
 -p 8080:8080 \
 richardprax/devops-github-backend:latest
```

### Start Frontend Image Locally

1. start DB and Backend
2. run command to start image with latest tag (specify tag if neccessary)

```bash
docker run -d \
  --name frontend-container \
  -e NEXT_PUBLIC_API_URL=http://host.docker.internal:8080 \
  -p 3000:3000 \
  richardprax/devops-github-frontend:latest
```

## Deploy local with Minikube

1. Start minikube

```bash
start minikube
```

2. Enable ingress

```bash
minikube addons enable ingress
```

3. Get minikube ip and add it to /etc/hosts

```bash
minikube ip
```

```bash
echo "{replace with ip} backend.local frontend.local" | sudo tee -a /etc/hosts
```

4. Update Helm repo for postgresql

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
```

6. Deploy postgres with helm

```bash
helm install my-postgres bitnami/postgresql \
  --set auth.postgresPassword=admin \
  --set auth.database=koerperschmiede \
  --set auth.username=admin \
  --set auth.password=admin
```

7. Deploy backend and frontend

```bash
helm install backend ./charts/backEnd
helm install frontend ./charts/frontend
```

8. Add kubectl alias

```bash
alias kubectl="minikube kubectl --"
```

9. Check if everything started

```bash
kubectl get pods
kubectl get svc
kubectl get ingress
```
