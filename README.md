# Uni DevOps Projekt – SS25

Dies ist ein Fullstack-Projekt zur Verwaltung einer Physiotherapie-Praxis mit **Spring Boot (Backend)** und **Next.js (Frontend)**. Ziel war es, moderne DevOps-Praktiken wie Containerisierung, CI/CD, Infrastructure as Code (Helm) sowie automatisiertes Testing und Deployment umzusetzen.

---

## 🐳 Lokales Deployment mit Docker

### 🔧 Voraussetzungen

-   Docker (inkl. Docker Daemon)
-   PostgreSQL läuft entweder lokal oder via Docker Compose (siehe unten)

### 🧱 Backend starten

1. Stelle sicher, dass PostgreSQL auf Port `5432` läuft  
   (siehe `backEnd/docker-compose.yml`)
2. Starte das Backend:

```bash
docker run -d \
 --name backend-container \
 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/koerperschmiede \
 -e SPRING_DATASOURCE_USERNAME=admin \
 -e SPRING_DATASOURCE_PASSWORD=admin \
 -p 8080:8080 \
 richardprax/devops-github-backend:latest
```

### 💻 Frontend starten

1. Starte die DB und das Backend
2. Dann das Frontend:

```bash
docker run -d \
  --name frontend-container \
  -e NEXT_PUBLIC_API_URL=http://host.docker.internal:8080 \
  -p 3000:3000 \
  richardprax/devops-github-frontend:latest
```

---

## ☸️ Lokales Deployment mit Minikube

### ✅ Voraussetzungen

-   Minikube
-   Helm (mind. v3)

### 📦 Schritte

1. **Minikube starten:**

```bash
minikube start
```

2. **Ingress aktivieren:**

```bash
minikube addons enable ingress
```

3. **Hosts-Datei aktualisieren:**

```bash
minikube ip
```

Dann:

```bash
echo "{MINIKUBE_IP} backend.local frontend.local" | sudo tee -a /etc/hosts
```

4. **Helm-Repo aktualisieren:**

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
```

5. **PostgreSQL installieren:**

```bash
helm install my-postgres bitnami/postgresql \
  --set auth.postgresPassword=admin \
  --set auth.database=koerperschmiede \
  --set auth.username=admin \
  --set auth.password=admin
```

6. **Backend und Frontend via Helm Charts deployen:**

```bash
helm install backend ./charts/backEnd
helm install frontend ./charts/frontend
```

7. **Optional: Alias für `kubectl` setzen**

```bash
alias kubectl="minikube kubectl --"
```

8. **Status prüfen:**

```bash
kubectl get pods
kubectl get svc
kubectl get ingress
```

---

## ⚙️ CI/CD Übersicht

Die Anwendung verwendet GitHub Actions für Build, Test, Linting, SonarQube-Analyse und das Container-Building.

### 🔄 Continuous Integration (CI)

-   Separate Pipelines für Backend (Maven) und Frontend (Node.js)
-   Tests und Linter Checks (Checkstyle, ESLint, Stylelint)
-   SonarQube Scans inklusive Code Coverage
-   Build-Artefakte werden gespeichert und im nächsten Schritt verwendet

### 🚀 Continuous Deployment (CD)

-   Startet nur, wenn CI erfolgreich ist
-   Deployment startet lokale Container via Docker
-   Health Checks (Spring Boot Actuator, HTML Checks)
-   Validierung von Login-Flow und UI-Inhalten

---

## 🏷️ Versionierung

Im Rahmen dieses Projekts werden Docker Images mit dem SHA des jeweiligen Commits getaggt (`:SHA`).  
Dadurch wird sichergestellt, dass keine Images überschrieben werden und jede Pipeline ein eigenes, reproduzierbares Artefakt erzeugt.

Beispiel:

```bash
richardprax/devops-github-frontend:fb83cc3
```

---

## 👨‍⚕️ Fachlicher Kontext

Die Anwendung verwaltet eine Physiotherapiepraxis mit Fokus auf Trainings- und Kursmanagement.

-   **Backend (Spring Boot)**: REST API, Authentifizierung, Datenpersistenz via PostgreSQL
-   **Frontend (Next.js)**: Benutzeroberfläche mit Login, Kursansicht etc.
