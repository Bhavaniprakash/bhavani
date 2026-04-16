# Java Apps

This folder contains four sample services for the EKS starter.

- `app1` - Spring Boot
- `app2` - Spring Boot
- `app3` - Quarkus
- `app4` - Quarkus

All apps expose HTTP on port `8080`, which matches the Kubernetes manifests.

## Local Run

Spring Boot apps:

```bash
mvn spring-boot:run
```

Quarkus apps:

```bash
mvn quarkus:dev
```

## Docker Build

Example:

```bash
docker build -t app1:local .
```
