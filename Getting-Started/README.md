Jenkins Installation and Overview Guide

This guide provides an overview of Jenkins, its features, and step-by-step instructions to install Jenkins on a Debian-based system. The content is designed to help users understand Jenkins and set it up effectively. 

# Jenkins Installation on Ubuntu/Debian

## Install Java (Required for Jenkins)

Jenkins requires Java to run.  
This guide uses OpenJDK 21.

Update package repositories and install Java:

```bash
sudo apt update
sudo apt install fontconfig openjdk-21-jre -y
```

Verify Java installation:

```bash
java -version
```

Expected output:

```text
openjdk 21.0.8 2025-07-15
OpenJDK Runtime Environment (build 21.0.8+9-Debian-1)
OpenJDK 64-Bit Server VM (build 21.0.8+9-Debian-1, mixed mode, sharing)
```

> Install Java before Jenkins to avoid:
>
> ```text
> jenkins: failed to find a valid Java installation
> ```

---

# Install Jenkins (LTS Release)

Add Jenkins repository key:

```bash
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc \
https://pkg.jenkins.io/debian-stable/jenkins.io-2026.key
```

Add Jenkins repository:

```bash
echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc] \
https://pkg.jenkins.io/debian-stable binary/" | \
sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
```

Update repositories:

```bash
sudo apt update
```

Install Jenkins:

```bash
sudo apt install jenkins -y
```

---

# Start and Enable Jenkins

Start Jenkins service:

```bash
sudo systemctl start jenkins
```

Enable Jenkins on system boot:

```bash
sudo systemctl enable jenkins
```

Verify Jenkins status:

```bash
sudo systemctl status jenkins
```

If running successfully, the output should show:

```text
active (running)
```

---

# Access Jenkins

Open your browser and visit:

```text
http://localhost:8080
```

or

```text
http://YOUR_SERVER_IP:8080
```

---

# Jenkins Service Information

The Jenkins package automatically:

- Creates a `jenkins` system user
- Configures Jenkins as a systemd service
- Stores logs in `journalctl`
- Uses port `8080` by default
- Starts Jenkins automatically on boot

View Jenkins logs:

```bash
journalctl -u jenkins.service
```

---

# Change Jenkins Port (Optional)

If port `8080` is already in use:

Edit Jenkins service configuration:

```bash
sudo systemctl edit jenkins
```

Add:

```ini
[Service]
Environment="JENKINS_PORT=8081"
```

Reload systemd:

```bash
sudo systemctl daemon-reload
```

Restart Jenkins:

```bash
sudo systemctl restart jenkins
```

---

# Useful Commands

Restart Jenkins:

```bash
sudo systemctl restart jenkins
```

Stop Jenkins:

```bash
sudo systemctl stop jenkins
```

Check Jenkins logs:

```bash
journalctl -u jenkins.service -n 50 --no-pager
```

Check installed Jenkins version:

```bash
jenkins --version
```




> **Security Note:** By default Jenkins runs on port `8080`. You can test the Jenkins dashboard locally using `http://localhost:8080`, but if you are deploying Jenkins on a cloud VM or remote server, you must create a new inbound rule under your instance security settings/firewall so that port `8080` is accessible. Configure the rule in AWS Security Groups, UFW, Azure NSG, GCP Firewall, or your hosting provider’s firewall settings to allow inbound TCP traffic on port `8080`. 

> **Security Note:** By default Jenkins runs on port `8080`. You can test the Jenkins dashboard locally using `http://localhost:8080`, but if you are deploying Jenkins on a cloud VM or remote server, you must configure your firewall/security rules (AWS Security Groups, UFW, Azure NSG, GCP Firewall, etc.) to allow inbound access to port `8080`.
