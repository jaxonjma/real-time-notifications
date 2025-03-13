# Real-Time Notifications System

 **A real-time notification system for an online gaming platform.**

This project enables players to receive **real-time notifications** about **in-game events** and **social interactions**, leveraging **Spring Boot, Kafka, and WebSockets**.

## **Architecture**
The system follows a **microservices architecture** structured as a **multi-module Gradle project**:

```plaintext
📦 real-time-notifications
├── 📂 game-events-service      # Handles in-game event generation & Kafka publishing
│   ├── 📂 controller
│   ├── 📂 service
│   ├── 📂 producer
│   ├── 📄 GameEventApplication.java
│   ├── 📄 build.gradle
│   ├── 📄 application.yml
│
├── 📂 notification-service     # Listens for events, stores them, and sends WebSocket notifications
│   ├── 📂 controller
│   ├── 📂 service
│   ├── 📂 consumer
│   ├── 📂 repository
│   ├── 📂 model
│   ├── 📄 NotificationApplication.java
│   ├── 📄 build.gradle
│   ├── 📄 application.yml
│
├── 📂 api-gateway              # Spring Cloud Gateway for request routing
│   ├── 📂 config
│   ├── 📄 ApiGatewayApplication.java
│   ├── 📄 build.gradle
│   ├── 📄 application.yml
│
├── 📄 build.gradle             # Root Gradle configuration
├── 📄 settings.gradle          # Declares modules
├── 📄 run.sh                   # Script to launch all services
├── 📄 README.md                # Project documentation
```

---

## **Technologies Used**
- **Java 21**
- **Spring Boot 3.2.1**
- **Spring Cloud Gateway**
- **Spring Data JPA**
- **Spring WebSockets**
- **Apache Kafka**
- **Gradle**
- **H2 Database** (In-memory persistence)
- **Lombok** (For reducing boilerplate code)
- **Docker** (Optional, for Kafka deployment)

---

## **Prerequisites**
Before running the project, ensure you have the following installed:
- **Java 21** 
- **Gradle** 
- **Apache Kafka** (Optional if using Docker)
- **Docker** (Optional for running Kafka in containers)

---

## **Setup & Execution**
### ** Start Kafka (Optional with Docker)**
If you prefer to run **Kafka using Docker**, execute:

```bash
docker-compose -f kafka-docker-compose.yml up -d
```

If you want to start Kafka manually:

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties &
bin/kafka-server-start.sh config/server.properties &
```

---

### ** Build the Project**
Compile and resolve dependencies:

```bash
./gradlew clean build
```

---

### ** Start All Microservices**
Launch all services simultaneously:

```bash
./run.sh
```

 **To start individual microservices manually:**
```bash
./gradlew :game-events-service:bootRun
./gradlew :notification-service:bootRun
./gradlew :api-gateway:bootRun
```

---

## **API Usage**
Once the system is running, you can test the following endpoints:

### ** Trigger a Level-Up Event**
```bash
curl -X POST http://localhost:8080/events/level-up/1/15
```
**Expected Response:**
```json
{
  "message": "User 1 leveled up to 15",
  "type": "GAME_EVENT",
  "timestamp": "2025-03-03T21:00:00"
}
```

### ** Retrieve Notifications for a User**
```bash
curl -X GET http://localhost:8080/notifications/1
```
**Expected Response:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "message": "User 1 leveled up to 15",
    "type": "GAME_EVENT",
    "timestamp": "2025-03-03T21:00:00"
  }
]
```

---

## **Running Tests**
To execute unit tests:

```bash
./gradlew test
```

## **Troubleshooting**
### **Error: Port 8080 Already in Use**
If you see:
```
Web server failed to start. Port 8080 was already in use.
```
Fix:
```bash
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows
kill -9 <PID>  # macOS/Linux
taskkill /PID <PID> /F  # Windows
```

### **Error: Kafka Not Responding**
Restart Kafka:

```bash
docker-compose restart
```
Or manually:

```bash
bin/kafka-server-start.sh config/server.properties &
```

### **Refresh project and depencencies**

```bash
./gradlew --stop
rm -rf ~/.gradle/caches/
rm -rf ~/.gradle/wrapper/
./gradlew clean build --refresh-dependencies
```