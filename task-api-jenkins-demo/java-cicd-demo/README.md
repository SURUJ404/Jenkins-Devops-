# task-api — Jenkins CI/CD Demo (Java / Maven / Docker)

A minimal Java REST service used to demonstrate a complete Jenkins CI/CD
pipeline: build → test → package → Docker image → push → deploy.

Built from scratch, in the spirit of the uploaded `jenkins-tutorial-main`
course repo, but as an independent project rather than a copy of it.

## Why it's dependency-free at runtime

The app uses only `com.sun.net.httpserver.HttpServer` from the JDK — no
Spring Boot, no third-party web framework. That keeps the Docker image
small, the Maven build fast, and the pipeline free of flaky dependency
downloads. JUnit 5 is used for tests only (test scope).

## API

| Method | Path                  | Description                |
|--------|-----------------------|-----------------------------|
| GET    | `/health`              | Liveness check              |
| GET    | `/tasks`               | List all tasks              |
| POST   | `/tasks`               | Create a task (body = title)|
| POST   | `/tasks/{id}/complete` | Mark a task done            |
| DELETE | `/tasks/{id}`          | Delete a task                |

## Run locally

```bash
mvn clean package
java -jar target/task-api.jar
# or
docker compose up --build
```

```bash
curl -X POST localhost:8080/tasks -d "write Jenkinsfile"
curl localhost:8080/tasks
```

## Project layout

```
task-api/
├── Jenkinsfile              # Build → Test → Package → Docker Build → Push → Deploy
├── Dockerfile                # multi-stage: maven build, then JRE-alpine runtime
├── docker-compose.yml
├── pom.xml
└── src/
    ├── main/java/com/example/taskapi/
    │   ├── App.java          # HTTP server + routing
    │   ├── Task.java         # model
    │   └── TaskStore.java    # in-memory store
    └── test/java/com/example/taskapi/
        └── TaskStoreTest.java
```

## Jenkins setup notes

The `Jenkinsfile` expects:
- Tools named `Maven-3.9` and `JDK-17` configured under **Manage Jenkins →
  Tools**.
- A `dockerhub-registry-url` secret text credential holding your registry
  host (e.g. `docker.io/yourorg`).
- A `dockerhub-credentials` username/password credential for `docker login`.

`Docker Push` and `Deploy` only run on the `main` branch, so feature
branches build and test without touching the registry — the same pattern
the course's multibranch-pipeline module covers.
