#!/bin/bash

echo "Deploying SangSangPlus to Kubernetes..."

# Create namespace if it doesn't exist
kubectl create namespace sangsangplus --dry-run=client -o yaml | kubectl apply -f -

# Apply ConfigMaps and Secrets first
echo "Applying ConfigMaps and Secrets..."
kubectl apply -f configmap.yaml -n sangsangplus

# Deploy PostgreSQL
echo "Deploying PostgreSQL..."
kubectl apply -f postgres-deployment.yaml -n sangsangplus

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/postgres -n sangsangplus

# Deploy User Service
echo "Deploying User Service..."
kubectl apply -f user-deployment.yaml -n sangsangplus

# Wait for User Service to be ready
echo "Waiting for User Service to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/user-service -n sangsangplus

# Deploy Gateway Service
echo "Deploying Gateway Service..."
kubectl apply -f gateway-deployment.yaml -n sangsangplus

# Wait for Gateway Service to be ready
echo "Waiting for Gateway Service to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/gateway-service -n sangsangplus

# Apply Ingress
echo "Applying Ingress..."
kubectl apply -f ingress.yaml -n sangsangplus

echo "Deployment completed!"
echo ""
echo "To access the application:"
echo "1. Add to /etc/hosts: 127.0.0.1 sangsangplus.local api.sangsangplus.local"
echo "2. If using minikube: minikube tunnel"
echo "3. Access Gateway: http://sangsangplus.local"
echo "4. Access API docs: http://sangsangplus.local/swagger-ui/index.html"
echo ""
echo "To check status:"
echo "kubectl get pods -n sangsangplus"
echo "kubectl get services -n sangsangplus"