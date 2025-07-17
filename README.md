# User Service Project

A complete Spring Boot microservice with Google OAuth2 authentication, PostgreSQL database, and Kubernetes deployment support.

## 🚀 Quick Start

### Option 1: Automated Setup (Recommended)

Run the setup script to install all required tools automatically:

```bash
# Clone the repository
git clone https://github.com/Jeonneung/user-test.git
cd user-test

# Run automated setup
chmod +x setup.sh
./setup.sh
```

The script will install:
- ☕ Java 11
- 🔧 Maven
- 🐳 Docker
- ⚙️ kubectl
- ☁️ Azure CLI
- 🛠️ Essential utilities (curl, wget, jq)

### Option 2: Manual Setup

If you prefer manual installation, see the [User/README.md](User/README.md) for detailed instructions.

## 📁 Project Structure

```
user-test/
├── setup.sh              # 🛠️ Automated environment setup
├── README.md             # 📖 This file
└── User/                 # 📦 Main application
    ├── src/              # 💻 Source code
    ├── k8s/              # ☸️ Kubernetes manifests
    ├── .env.example      # 🔐 Environment template
    ├── README.md         # 📚 Detailed documentation
    └── ...
```

## 🎯 Features

### 🔐 Authentication & Security
- Google OAuth2 integration
- JWT token support
- Secure credential management
- Environment-based configuration

### 🗄️ Database
- PostgreSQL with JPA/Hibernate
- Automatic schema generation
- Connection pooling with HikariCP

### 🚀 Deployment
- **Docker Hub**: `buildingbite/sangsangplus-user:latest`
- **Kubernetes**: Complete AKS-ready manifests
- **Azure Cloud**: LoadBalancer with static IP
- **SSL/TLS**: Cloudflare integration support

### 🔧 Development
- Spring Boot 2.7.14
- Maven build system
- Comprehensive logging
- Health check endpoints

## 📋 Prerequisites

- **OS**: Ubuntu 18.04+, CentOS 7+, macOS 10.15+, or Debian 10+
- **Memory**: 4GB RAM minimum
- **Disk**: 10GB free space
- **Network**: Internet connection for package downloads

## 🛠️ Manual Installation

If you need to install tools manually:

<details>
<summary>Click to expand manual installation guide</summary>

### Java 11
```bash
# Ubuntu/Debian
sudo apt install openjdk-11-jdk

# CentOS/RHEL
sudo yum install java-11-openjdk-devel

# macOS
brew install openjdk@11
```

### Maven
```bash
# Ubuntu/Debian
sudo apt install maven

# CentOS/RHEL
sudo yum install maven

# macOS
brew install maven
```

### Docker
```bash
# Ubuntu
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
sudo usermod -aG docker $USER

# macOS
# Install Docker Desktop from https://docker.com
```

### kubectl
```bash
# Linux
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# macOS
brew install kubectl
```

### Azure CLI
```bash
# Ubuntu/Debian
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash

# macOS
brew install azure-cli
```

</details>

## 🚀 Quick Deploy

After running the setup script:

```bash
# 1. Navigate to the application directory
cd User

# 2. Configure environment
cp .env.example .env
nano .env  # Update with your secrets

# 3. Build application
mvn clean package

# 4. Run locally with Docker
docker-compose up

# 5. Deploy to Kubernetes (if configured)
kubectl apply -f k8s/
```

## 🔗 Resources

- **📚 Detailed Documentation**: [User/README.md](User/README.md)
- **🐳 Docker Hub**: [buildingbite/sangsangplus-user](https://hub.docker.com/r/buildingbite/sangsangplus-user)
- **🔧 Environment Setup**: [.env.example](User/.env.example)
- **☸️ Kubernetes Manifests**: [k8s/](User/k8s/)

## 🆘 Troubleshooting

### Setup Script Issues

1. **Permission Denied**:
   ```bash
   chmod +x setup.sh
   ./setup.sh
   ```

2. **Docker Permission Issues**:
   ```bash
   sudo usermod -aG docker $USER
   # Log out and log back in
   ```

3. **Package Manager Issues**:
   ```bash
   # Update package lists
   sudo apt update  # Ubuntu/Debian
   sudo yum update  # CentOS/RHEL
   ```

### Application Issues

Check the [User/README.md](User/README.md) for detailed troubleshooting.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests: `mvn test`
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**🤖 Generated with [Claude Code](https://claude.ai/code)**