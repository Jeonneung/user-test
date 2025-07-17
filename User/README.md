# User Service - Spring Boot REST API

A RESTful user management service with Google OAuth2 authentication, built with Spring Boot and deployed on Azure Kubernetes Service.

## Features

- 🔐 Google OAuth2 Authentication
- 👤 User Management (CRUD operations)  
- 🐘 PostgreSQL Database
- 🚀 Kubernetes Deployment
- 🔒 JWT Token Support
- ☁️ Azure Cloud Integration

## Prerequisites

- Java 11+
- Maven 3.6+
- Docker
- kubectl
- Azure CLI (for deployment)

## Environment Variables

Create a `.env` file or set these environment variables:

```bash
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/userdb
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your-db-password

# Google OAuth2 Configuration
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
OAUTH_REDIRECT_URI=https://your-domain.com/login/oauth2/code/google

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-change-this
JWT_ACCESS_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=2592000000
```

## Google OAuth2 Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API
4. Create OAuth 2.0 credentials
5. Download `client-secret.json` and place it in `src/main/resources/`
6. Add your domain to authorized redirect URIs

## Local Development

```bash
# Clone repository
git clone <your-repo-url>
cd user-service

# Install dependencies
mvn clean install

# Run PostgreSQL with Docker Compose
docker-compose up -d postgres

# Run application
mvn spring-boot:run
```

## API Endpoints

### Authentication
- `GET /api/auth/oauth2/user` - Get authenticated user info
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login

### User Management
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Health Check
- `GET /api/health` - Service health status

## Docker Deployment

```bash
# Build application
mvn clean package

# Build Docker image
docker build -t user-service:latest .

# Run with Docker Compose
docker-compose up
```

## Kubernetes Deployment

```bash
# Apply Kubernetes manifests
kubectl apply -f k8s/

# Check deployment status
kubectl get pods -n user-service

# Access logs
kubectl logs -f deployment/user-service -n user-service
```

## Production Configuration

For production deployment:

1. Use Kubernetes Secrets for sensitive data:
```bash
kubectl create secret generic user-service-secrets \
  --from-literal=google-client-secret=your-secret \
  --from-literal=jwt-secret=your-jwt-secret \
  --from-literal=db-password=your-db-password \
  -n user-service
```

2. Update ConfigMap to reference secrets
3. Use proper SSL certificates
4. Configure ingress with proper domains
5. Set up monitoring and logging

## Security Notes

⚠️ **Important**: Never commit sensitive information to Git:
- `client-secret.json` files
- Database passwords
- JWT secrets
- SSL private keys
- Environment files with secrets

All sensitive data should be:
- Added to `.gitignore`
- Stored in environment variables
- Managed via Kubernetes Secrets in production

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.