# Uni DevOps Projekt – SS25

## 📚 Inhaltsverzeichnis

-   [🎯 Projektübersicht](#-projektübersicht)
-   [👨‍⚕️ Fachlicher Kontext](#️-fachlicher-kontext)
-   [🏗️ Architektur & Technologie-Stack](#️-architektur--technologie-stack)
-   [⚙️ CI/CD Pipeline](#️-cicd-pipeline)
-   [📦 Helm Charts & Infrastructure as Code](#-helm-charts--infrastructure-as-code)
-   [🏷️ Versionierung & Container-Management](#️-versionierung--container-management)
-   [🚀 Deployment Anleitungen](#-deployment-anleitungen)
    -   [Quick Start](#quick-start)
    -   [Docker Deployment](#docker-deployment)
    -   [Kubernetes/Minikube Deployment](#kubernetesminikube-deployment)
-   [🔧 Troubleshooting](#-troubleshooting)

---

## 🎯 Projektübersicht

Dieses DevOps-Projekt implementiert eine **Fullstack-Webanwendung** zur Verwaltung einer Physiotherapie-Praxis. Das Hauptziel war es, moderne DevOps-Praktiken in einem praxisnahen Szenario umzusetzen und dabei den gesamten Software-Lebenszyklus abzubilden.

### 🎓 Lernziele & DevOps-Praktiken

-   **Containerisierung**: Docker für einheitliche Deployment-Umgebungen
-   **Orchestrierung**: Kubernetes mit Helm für skalierbare Infrastructure as Code
-   **CI/CD**: GitHub Actions für automatisierte Build-, Test- und Deployment-Pipelines
-   **Code Quality**: Statische Analyse mit SonarQube, Linting und Testing
-   **Monitoring**: Health Checks und Application Observability

---

## 👨‍⚕️ Fachlicher Kontext

Die Anwendung verwaltet eine **Physiotherapiepraxis** mit Fokus auf Trainings- und Kursmanagement. Die Wahl eines realistischen fachlichen Kontexts ermöglicht es, echte Geschäftsanforderungen in technische Lösungen zu übersetzen.

### 🏥 Funktionale Anforderungen

-   **Benutzerverwaltung**: Authentifizierung und Autorisierung
-   **Kursverwaltung**: Erstellung und Verwaltung von Therapiekursen
-   **Terminplanung**: Buchung und Verwaltung von Therapieterminen
-   **Patientenverwaltung**: Grundlegende Patientendaten
-   **Reporting**: Einfache Auswertungen und Übersichten

---

## 🏗️ Architektur & Technologie-Stack

### 📋 Technologie-Entscheidungen

| Bereich            | Technologie                     | Begründung                                           |
| ------------------ | ------------------------------- | ---------------------------------------------------- |
| **Backend**        | Spring Boot 3 + Java 21         | Robustes Enterprise-Framework, moderne Java-Features |
| **Frontend**       | Next.js 14 + React + TypeScript | SSR/SSG, optimale Performance, Type Safety           |
| **Datenbank**      | PostgreSQL                      | ACID-Konformität, robuste Relational DB              |
| **Container**      | Docker                          | Portabilität, einheitliche Umgebungen                |
| **Orchestrierung** | Kubernetes + Helm               | Skalierbarkeit, deklarative Konfiguration            |
| **CI/CD**          | GitHub Actions                  | Native Integration, kostenlos für OSS                |
| **Code Quality**   | SonarQube                       | Umfassende statische Analyse                         |

### 🔄 System-Architektur

```
┌─────────────────────────────────────────────────────────────┐
│                    Client (Browser)                         │
└─────────────────────┬───────────────────────────────────────┘
                      │ HTTP/HTTPS
┌─────────────────────▼───────────────────────────────────────┐
│                NGINX Ingress Controller                     │
│              (Load Balancer & SSL Termination)              │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┴─────────────┐
        │                           │
┌───────▼─────────┐        ┌────────▼────────┐
│   Next.js       │        │   Spring Boot   │
│   Frontend      │◄──────►│   REST API      │
│   Port: 3000    │   API  │   Port: 8080    │
│                 │ Calls  │                 │
└─────────────────┘        └────────┬────────┘
                                    │ JDBC
                           ┌────────▼────────┐
                           │   PostgreSQL    │
                           │   Database      │
                           │   Port: 5432    │
                           └─────────────────┘
```

---

## ⚙️ CI/CD Pipeline

Die **GitHub Actions** Pipeline implementiert moderne DevOps-Praktiken mit automatisierten Build-, Test-, und Quality-Gates. Das Setup folgt dem Prinzip "Shift Left" – Probleme werden so früh wie möglich im Entwicklungsprozess erkannt.

### 🔄 Pipeline-Übersicht

```mermaid
graph LR
    A[Git Push/PR] --> B[CI Pipeline]
    B --> C[Backend CI]
    B --> D[Frontend CI]
    C --> E[Lint & Test]
    D --> F[Lint & Test]
    E --> G[SonarQube]
    F --> H[SonarQube]
    G --> I[Docker Build]
    H --> J[Docker Build]
    I --> K[CD Pipeline]
    J --> K
    K --> L[Integration Tests]
    L --> M[Deployment Validation]
```

### 🔄 Continuous Integration (CI) - `fullstack-ci.yml`

Die CI-Pipeline startet in der aktuellen Implementierung automatisch bei **Push** oder **Pull Request** und führt parallele Jobs für Backend und Frontend aus. In einem Produktiv-System sollten spezifischere Regeln genutzt werden um wirklich nur die Teile der Anwendung neu zu Bauen, welche sich auch wirklich geändert haben:

#### **Backend CI-Pipeline**

1. **Setup & Caching**:

    - Java 21 (Temurin Distribution)
    - Maven Dependencies caching für schnellere Builds
    - PostgreSQL 15.3 Test-Container mit Health Checks

2. **Build, Test & Quality**:

    ```bash
    ./mvnw clean verify checkstyle:check
    ```

    - Unit Tests (JUnit)
    - Integration Tests mit TestContainers
    - Checkstyle für Code-Style Compliance
    - JaCoCo für Code Coverage

3. **SonarQube Analyse**:

    - Statische Code-Analyse
    - Code Coverage Integration (JaCoCo XML Reports)
    - Test-Results Upload (Surefire & Failsafe Reports)

4. **Container Build**:
    - JAR-Artefakt Upload für nachgelagerte Jobs
    - Docker Image Build & Push zu Docker Hub
    - Multi-Tag Strategy: `:latest` und `:$GITHUB_SHA`

#### **Frontend CI-Pipeline**

1. **Setup & Dependencies**:

    - Node.js 20
    - npm Dependencies caching
    - `npm ci` für deterministische Installs

2. **Code Quality & Testing**:

    ```bash
    npx eslint . --ext .js,.jsx,.ts,.tsx
    npx stylelint "**/*.{css,scss}"
    npm run test -- --coverage
    ```

    - ESLint für JavaScript/TypeScript
    - Stylelint für CSS/SCSS
    - Jest Tests mit Coverage Reports

3. **SonarQube Integration**:

    - Frontend-spezifische Code-Analyse
    - Coverage Reports Integration

4. **Production Build**:
    - Next.js Build (`npm run build`)
    - Docker Image mit optimiertem Production Bundle
    - Multi-Stage Dockerfile für minimale Image-Größe

### 🚀 Continuous Deployment (CD) - `fullstack-cd.yml`

Die CD-Pipeline wird **manuell über GitHub UI** gestartet und führt End-to-End Tests in isolierter Umgebung durch. Über ein Input-Parameter kann die Version des Docker Images ausgewählt werden, welche zum Deployment genutzt werden soll ("Version der Anwendung"). Das Versionierungskonzept ist später in der README beschrieben:

#### **Deployment-Strategie**

```bash
# Manuelle Trigger mit Version-Parameter
workflow_dispatch:
  inputs:
    version: "latest" | "fb83cc3" # SHA oder Tag
```

#### **Test-Infrastruktur Setup**

1. **Docker Network erstellen**: Isolierte Test-Umgebung, ermöglicht Kommuniktation zwischen Teilen der Anwendung
2. **PostgreSQL starten**: Echte Datenbank für Integrationstests
3. **Backend Container**: Mit DB-Verbindung über Docker Network
4. **Frontend Container**: Mit Backend-API Verbindung

#### **Automatisierte Validierung**

1. **Health Checks**:

    ```bash
    curl http://localhost:8080/actuator/health
    # Wartet auf {"status":"UP"}
    ```

2. **Funktionale Tests**:

    - **Login-API Test**: POST `/api/v1/auth/authenticate`
    - **Response Validation**: Prüft `accessToken` und `userId`
    - **Frontend Rendering**: HTML-Struktur Validierung
    - **UI Content Check**: Spezifische Inhalte wie "Hallo Körperschmiede"

3. **Cleanup**:
    - Container werden nach Tests automatisch entfernt
    - Kein persistenter Zustand (Ephemeral Testing)

### 🔧 Pipeline-Features

#### **Parallelisierung & Effizienz**

-   Backend und Frontend Jobs laufen parallel
-   Dependency Caching reduziert Build-Zeiten
-   `continue-on-error: true` für Linter (non-blocking, sollte im Prduktiv-System sehr wahrscheinlich so nicht genutzt werden)

#### **Sicherheit & Secrets**

-   Docker Hub Credentials via GitHub Secrets
-   SonarQube Token Management
-   Keine Hardcoded Credentials in Code

#### **Artefakt-Management**

-   Build-Artefakte werden zwischen Jobs übertragen
-   Docker Images mit SHA-Tags für Reproduzierbarkeit
-   Separate Uploads für JAR und Frontend-Build

### 📊 Quality Gates

-   **Backend**: Maven Tests + Checkstyle + SonarQube
-   **Frontend**: Jest Tests + ESLint + Stylelint + SonarQube
-   **Integration**: End-to-End API & UI Tests in CD
-   **Coverage**: JaCoCo (Backend) & Jest (Frontend) Reports

---

## 📦 Helm Charts & Infrastructure as Code

Als Bonus für unsere Projektarbeit habe ich versucht, **Helm Charts** für deklaratives sund wiederverwendbares Kubernetes-Deployment zu benutzen.
Helm fungiert als "Package Manager" für Kubernetes und ermöglicht es, komplexe Anwendungen mit Templates und konfigurierbaren Werten zu verwalten.

### 🎯 Warum Helm?

-   **Wiederverwendbarkeit**: Ein Chart für mehrere Umgebungen (dev, staging, prod)
-   **Versionierung**: Chart-Versionen für rollback-fähige Deployments
-   **Konfiguration**: Trennung von Code und Umgebungsspezifischen Werten
-   **Dependencies**: Automatisches Management von Abhängigkeiten (z.B. PostgreSQL)

### 🏗️ Chart-Struktur

Das Projekt enthält zwei separate Helm Charts, da die Hauptanwendung auch aus zwei Teilen besteht:

```
charts/
├── backEnd/           # Backend (Spring Boot) Chart
│   ├── Chart.yaml     # Chart-Metadaten
│   ├── values.yaml    # Standardkonfiguration
│   └── templates/     # Kubernetes-Manifeste
│       ├── deployment.yml
│       ├── service.yaml
│       └── ingress.yaml
└── frontend/          # Frontend (Next.js) Chart
    ├── Chart.yaml
    ├── values.yaml
    └── templates/
        ├── deployment.yaml
        ├── service.yaml
        └── ingress.yaml
```

### 🔧 Backend Chart (`charts/backEnd/`)

#### **Chart.yaml** – Chart-Metadaten

```yaml
apiVersion: v2
name: backend
description: A Helm chart for the backend service
type: application
version: 0.1.0
appVersion: "1.0.0"
```

#### **values.yaml** – Konfigurierbare Werte

Das Backend Chart verwendet folgende konfigurierbare Parameter:

-   **`replicaCount: 1`** – Anzahl der Pod-Replikas
-   **`image.repository`** – Docker Image Repository (`richardprax/devops-github-backend`)
-   **`image.tag: latest`** – Image-Tag (in CI/CD wird dies mit dem Git-SHA überschrieben)
-   **`service.type: ClusterIP`** – Service-Typ (intern im Cluster erreichbar)
-   **`service.port: 8080`** – Service-Port
-   **`ingress.enabled: true`** – Ingress aktiviert für externen Zugriff
-   **`ingress.host: backend.local`** – Hostname für den externen Zugriff
-   **`database.*`** – PostgreSQL-Verbindungsparameter

#### **Templates – Kubernetes-Ressourcen**

1. **Deployment** (`templates/deployment.yml`):

    - Erstellt einen Pod mit dem Spring Boot Container
    - Umgebungsvariablen für Datenbankverbindung werden aus `values.yaml` injiziert
    - Container lauscht auf Port 8080

2. **Service** (`templates/service.yaml`):

    - Typ: `ClusterIP` (nur intern erreichbar)
    - Mapped Port 8080 des Services auf Port 8080 des Containers
    - Selector: `app: backend`

3. **Ingress** (`templates/ingress.yaml`):
    - Ermöglicht externen HTTP-Zugriff über `backend.local`
    - Nutzt NGINX Ingress Controller
    - Leitet Traffic an den Backend-Service weiter

### 💻 Frontend Chart (`charts/frontend/`)

#### **values.yaml** – Konfiguration

-   **`replicaCount: 1`** – Ein Frontend-Pod
-   **`image.repository`** – Docker Image (`richardprax/devops-github-frontend`)
-   **`service.port: 3000`** – Next.js Standard-Port
-   **`ingress.host: frontend.local`** – Hostname für die Web-UI

#### **Templates**

1. **Deployment** (`templates/deployment.yaml`):

    - Next.js Container auf Port 3000
    - Umgebungsvariablen werden zur Laufzeit vom Container gelesen

2. **Service** (`templates/service.yaml`):

    - ClusterIP Service für Port 3000

3. **Ingress** (`templates/ingress.yaml`):
    - Externe Erreichbarkeit über `frontend.local`

### 🌐 Netzwerk-Architektur

```
[Browser]
    ↓ (HTTP)
[NGINX Ingress Controller]
    ↓
[frontend.local] → [Frontend Service:3000] → [Frontend Pod:3000]
[backend.local]  → [Backend Service:8080]  → [Backend Pod:8080]
                                                      ↓ (JDBC)
                                            [PostgreSQL Service:5432]
```

#### **Warum diese Architektur?**

1. **Separation of Concerns**: Jede Komponente hat ihre eigene Chart
2. **Skalierbarkeit**: `replicaCount` kann pro Service angepasst werden
3. **Konfigurierbarkeit**: Verschiedene Umgebungen (dev, staging, prod) können unterschiedliche `values.yaml` verwenden
4. **Service Discovery**: Kubernetes DNS löst Service-Namen automatisch auf
5. **Load Balancing**: Services verteilen Traffic automatisch auf verfügbare Pods

### 🔧 Erweiterte Konfiguration

#### **Verschiedene Umgebungen**

Für verschiedene Umgebungen können separate Values-Dateien erstellt werden:

```bash
# Development
helm install backend ./charts/backEnd -f values-dev.yaml

# Production
helm install backend ./charts/backEnd -f values-prod.yaml
```

#### **Image-Tags überschreiben**

In CI/CD-Pipelines wird das Image-Tag dynamisch gesetzt:

```bash
helm upgrade backend ./charts/backEnd \
  --set image.tag=$GITHUB_SHA \
  --install
```

#### **Skalierung**

Horizontale Skalierung durch Anpassung der Replica-Anzahl:

```bash
helm upgrade backend ./charts/backEnd \
  --set replicaCount=3 \
  --install
```

---

## 🏷️ Versionierung & Container-Management

### 🏷️ Image-Tagging Strategie

Im Rahmen dieses Projekts werden Docker Images mit dem **SHA des jeweiligen Commits** getaggt (`:SHA`).  
Dadurch wird sichergestellt, dass keine Images überschrieben werden und jede Pipeline ein eigenes, reproduzierbares Artefakt erzeugt.

#### **Warum SHA-basierte Tags?**

1. **Eindeutigkeit**: Jeder Commit erzeugt ein eindeutiges Image
2. **Reproduzierbarkeit**: Exakte Nachvollziehbarkeit welcher Code deployed wurde
3. **Rollback-Fähigkeit**: Einfache Rückkehr zu vorherigen Versionen
4. **Parallel Development**: Verschiedene Branches überschreiben sich nicht

#### **Tag-Beispiele**

```bash
# Commit-spezifische Tags
richardprax/devops-github-frontend:fb83cc3
richardprax/devops-github-backend:a1b2c3d

# Zusätzlich: Latest Tag für aktuelle Entwicklung
richardprax/devops-github-frontend:latest
richardprax/devops-github-backend:latest
```

### 🐳 Container-Strategie

#### **Multi-Stage Dockerfiles**

Beide Services nutzen optimierte Multi-Stage Builds:

**Backend (Spring Boot)**:

```dockerfile
# Build Stage
FROM openjdk:21-jdk-slim as builder
COPY . .
RUN ./mvnw clean package -DskipTests

# Runtime Stage
FROM openjdk:21-jre-slim
COPY --from=builder target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Frontend (Next.js)**:

```dockerfile
# Dependencies Stage
FROM node:20-alpine AS deps
COPY package*.json ./
RUN npm ci --only=production

# Build Stage
FROM node:20-alpine AS builder
COPY . .
RUN npm run build

# Runtime Stage
FROM node:20-alpine AS runner
COPY --from=builder .next ./
ENTRYPOINT ["npm", "start"]
```

#### **Optimierungen**

-   **Layer Caching**: Dependencies werden separat kopiert
-   **Minimale Base Images**: Alpine Linux für kleinere Image-Größen
-   **Security**: Non-root User für Runtime
-   **Health Checks**: Container-native Health Endpoints

---

## 🚀 Deployment Anleitungen

Dieser Abschnitt bietet praktische Anleitungen für verschiedene Deployment-Szenarien – vom schnellen lokalen Start bis hin zum produktionsreifen Kubernetes-Setup.

### Quick Start

#### Voraussetzungen

-   Docker & Docker Compose
-   Helm 3+ (für Kubernetes)
-   Minikube (für lokales K8s)

#### Schnellstart mit Docker

```bash
# 1. Repository klonen
git clone https://github.com/RichardPrax/uni-devops-ss-25.git
cd uni-devops-ss-25

# 2. PostgreSQL starten
docker run -d --name postgres \
  -e POSTGRES_DB=koerperschmiede \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin \
  -p 5432:5432 postgres:15.3

# 3. Backend starten
docker run -d --name backend \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/koerperschmiede \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin \
  -p 8080:8080 richardprax/devops-github-backend:latest

# 4. Frontend starten
docker run -d --name frontend \
  -e NEXT_PUBLIC_API_URL=http://host.docker.internal:8080 \
  -p 3000:3000 richardprax/devops-github-frontend:latest
```

#### Zugriff

-   **Frontend**: http://localhost:3000
-   **Backend API**: http://localhost:8080
-   **Health Check**: http://localhost:8080/actuator/health

---

### Docker Deployment

#### 🔧 Voraussetzungen

-   Docker (inkl. Docker Daemon)
-   PostgreSQL läuft entweder lokal oder via Docker Compose (siehe unten)

#### 📋 Schritt-für-Schritt Anleitung

##### 🧱 Backend starten

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

##### 💻 Frontend starten

1. Starte die DB und das Backend
2. Dann das Frontend:

```bash
docker run -d \
  --name frontend-container \
  -e NEXT_PUBLIC_API_URL=http://host.docker.internal:8080 \
  -p 3000:3000 \
  richardprax/devops-github-frontend:latest
```

#### ✅ Validierung

```bash
# Backend Health Check
curl http://localhost:8080/actuator/health

# Frontend Erreichbarkeit
curl http://localhost:3000
```

---

### Kubernetes/Minikube Deployment

#### ✅ Voraussetzungen

-   Minikube
-   Helm (mind. v3)

#### 📦 Setup & Deployment

##### 1. **Minikube initialisieren**

```bash
minikube start
```

##### 2. **Ingress aktivieren**

```bash
minikube addons enable ingress
```

##### 3. **Hosts-Datei konfigurieren**

```bash
# Minikube IP ermitteln
minikube ip

# Hosts-Datei aktualisieren (IP entsprechend anpassen)
echo "{MINIKUBE_IP} backend.local frontend.local" | sudo tee -a /etc/hosts
```

##### 4. **Helm Repository einrichten**

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
```

##### 5. **PostgreSQL installieren**

```bash
helm install my-postgres bitnami/postgresql \
  --set auth.postgresPassword=admin \
  --set auth.database=koerperschmiede \
  --set auth.username=admin \
  --set auth.password=admin
```

##### 6. **Application Services deployen**

```bash
# Backend deployen
helm install backend ./charts/backEnd

# Frontend deployen
helm install frontend ./charts/frontend
```

##### 7. **Deployment validieren**

```bash
# Optional: kubectl Alias setzen
alias kubectl="minikube kubectl --"

# Status prüfen
kubectl get pods
kubectl get svc
kubectl get ingress
```

#### 🌐 Zugriff auf die Anwendung

-   **Frontend**: http://frontend.local
-   **Backend API**: http://backend.local
-   **PostgreSQL**: Via Port-Forward `kubectl port-forward svc/my-postgres-postgresql 5432:5432`

#### 🔍 Helm Troubleshooting

```bash
# Chart-Status prüfen
helm list

# Detaillierte Informationen
helm status backend
helm status frontend

# Logs anzeigen
kubectl logs -l app=backend
kubectl logs -l app=frontend

# Pods und Services prüfen
kubectl get pods,svc,ingress

# Chart deinstallieren
helm uninstall backend
helm uninstall frontend
helm uninstall my-postgres
```

---

## 🔧 Troubleshooting

### 🐳 Docker Issues

#### Container startet nicht

```bash
# Logs anzeigen
docker logs backend-container
docker logs frontend-container

# Container Status prüfen
docker ps -a

# Ports prüfen
netstat -tulpn | grep :8080
netstat -tulpn | grep :3000
```

#### Datenbankverbindung

```bash
# PostgreSQL Container prüfen
docker exec -it postgres psql -U admin -d koerperschmiede

# Netzwerk-Connectivity testen
docker exec backend-container curl postgres:5432
```

### ☸️ Kubernetes/Minikube Issues

#### Pods starten nicht

```bash
# Pod Details anzeigen
kubectl describe pod <pod-name>

# Logs anzeigen
kubectl logs <pod-name> -f

# Events prüfen
kubectl get events --sort-by=.metadata.creationTimestamp
```

#### Ingress nicht erreichbar

```bash
# Ingress Controller Status
kubectl get pods -n ingress-nginx

# Minikube Tunnel (falls nötig)
minikube tunnel

# DNS/Hosts Konfiguration prüfen
nslookup backend.local
nslookup frontend.local
```

#### Image Pull Errors

```bash
# Image lokal in Minikube laden
minikube image load richardprax/devops-github-backend:latest
minikube image load richardprax/devops-github-frontend:latest

# Registry Secrets prüfen
kubectl get secrets
```

### 🔄 CI/CD Issues

#### Pipeline Fails

```bash
# GitHub Actions Logs in der Web-UI prüfen
# Häufige Probleme:
# - Docker Hub Rate Limits
# - SonarQube Token abgelaufen
# - Test-Dependencies fehlen
# - Secrets abgelaufen
```

#### SonarQube Probleme

```bash
# Token validieren
curl -u $SONAR_TOKEN: $SONAR_HOST_URL/api/authentication/validate

# Projekt-Key prüfen
curl -u $SONAR_TOKEN: $SONAR_HOST_URL/api/projects/search
```

### 🚨 Häufige Probleme

| Problem                       | Lösung                                                    |
| ----------------------------- | --------------------------------------------------------- |
| Port bereits belegt           | `docker stop $(docker ps -q)` oder anderen Port verwenden |
| Image nicht gefunden          | Tag prüfen: `docker images`                               |
| Minikube IP ändert sich       | Hosts-Datei neu konfigurieren                             |
| Helm Installation fehlschlägt | `helm uninstall` und erneut versuchen                     |
| PostgreSQL Connection Timeout | Container-Reihenfolge beachten (DB → Backend → Frontend)  |

---
