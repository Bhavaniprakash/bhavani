# AWS EKS Multi-App Starter

This starter shows how to run one UI application and multiple backend applications on AWS EKS behind one domain using path-based routing.

All container images in this starter are Linux container images and are intended to run on Linux-based EKS worker nodes.

Example:

- `https://yourdomain.com/`
- `https://yourdomain.com/app1`
- `https://yourdomain.com/app2`
- `https://yourdomain.com/app3`
- `https://yourdomain.com/app4`

## Recommended Architecture

1. Developers push code to GitHub.
2. GitHub Actions builds Docker images.
3. Images are pushed to Amazon ECR.
4. GitHub Actions updates Kubernetes deployments on EKS.
5. AWS Load Balancer Controller creates an ALB.
6. ALB routes traffic to the UI and backend apps through Ingress paths.

## Folder Structure

```text
aws-eks-multiapp-starter/
|-- README.md
|-- .github/
|   `-- workflows/
|       |-- ci.yml
|       `-- deploy.yml
|-- apps/
|   |-- ui-app/
|   |-- app1/
|   |-- app2/
|   |-- app3/
|   `-- app4/
`-- k8s/
    |-- namespace.yaml
    |-- ingress.yaml
    |-- ui/
    |   |-- deployment.yaml
    |   `-- service.yaml
    |-- app1/
    |   |-- deployment.yaml
    |   `-- service.yaml
    |-- app2/
    |   |-- deployment.yaml
    |   `-- service.yaml
    |-- app3/
    |   |-- deployment.yaml
    |   `-- service.yaml
    `-- app4/
        |-- deployment.yaml
        `-- service.yaml
```

## Route Design

Use one domain and multiple paths:

- `/` -> ui app
- `/app1` -> app1 service
- `/app2` -> app2 service
- `/app3` -> app3 service
- `/app4` -> app4 service

If you want subdomains instead, you can use:

- `app1.yourdomain.com`
- `app2.yourdomain.com`
- `app3.yourdomain.com`
- `app4.yourdomain.com`

For starting out, path-based routing is simpler.

## AWS Services You Need

- Amazon EKS
- Amazon ECR
- AWS Load Balancer Controller
- Route 53
- ACM certificate
- IAM OIDC provider for EKS

## App Mapping

- `ui-app` -> frontend UI -> ECR repo `ui-app` -> Kubernetes deployment `ui-app`
- `app1` -> Spring Boot backend -> ECR repo `app1` -> Kubernetes deployment `app1`
- `app2` -> Spring Boot backend -> ECR repo `app2` -> Kubernetes deployment `app2`
- `app3` -> Quarkus backend -> ECR repo `app3` -> Kubernetes deployment `app3`
- `app4` -> Quarkus backend -> ECR repo `app4` -> Kubernetes deployment `app4`

## GitHub Actions Mapping

- `ci.yml` builds all 5 apps and validates their Docker builds.
- `deploy.yml` builds Linux `linux/amd64` images, pushes all 5 images to ECR, and updates EKS deployments.
- You should create 5 ECR repositories:
- `ui-app`
- `app1`
- `app2`
- `app3`
- `app4`

## High-Level Setup Steps

1. Create EKS cluster.
2. Install AWS Load Balancer Controller in the cluster.
3. Create ECR repos for each app.
4. Point Route 53 domain to the ALB.
5. Create GitHub secrets for AWS authentication.
6. Apply Kubernetes manifests.
7. Push code to GitHub and let GitHub Actions deploy.

## GitHub Secrets Needed

Add these in GitHub repository secrets:

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION`
- `EKS_CLUSTER_NAME`

Better option:

- Use GitHub OIDC with IAM role instead of long-lived AWS keys.

## Deploy Order

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/ui/
kubectl apply -f k8s/app1/
kubectl apply -f k8s/app2/
kubectl apply -f k8s/app3/
kubectl apply -f k8s/app4/
kubectl apply -f k8s/ingress.yaml
```

## Important Customizations

Before using this starter, replace:

- `yourdomain.com`
- `us-east-1`
- `your-aws-account-id`
- image repository names
- ACM certificate ARN
- cluster name

## Linux Pod Note

- EKS nodes should be Linux worker nodes.
- GitHub Actions is configured to build `linux/amd64` images for all 5 apps.
- The Dockerfiles already use Linux base images such as `nginx:alpine`, `maven`, and `eclipse-temurin`.

## Suggested GitHub Repo Strategy

Option 1:

- One repo per application
- One infra repo for Kubernetes manifests

Option 2:

- One monorepo containing all 5 apps and infra

For a beginner-friendly team setup, one infra repo plus separate app repos is clean and scalable.

## Next Best Step

If you want, we can next do one of these:

1. Create Terraform for EKS, ECR, IAM, and ALB setup.
2. Convert this starter into Helm charts.
3. Create a full CI/CD flow for your real app names.
4. Add Argo CD for GitOps deployment.
