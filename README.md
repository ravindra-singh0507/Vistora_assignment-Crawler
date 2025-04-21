# Vistora_assignment-Crawler-
A full-stack application that connects to a MySQL database, crawls schema metadata (tables, columns, relationships, etc.), dynamically generates model classes, provides RESTful APIs, and enables metadata exploration via a React + TypeScript frontend.

---

## ⚙️ Features

### ✅ Backend (Spring Boot)
- Connect to any MySQL database.
- Crawl schema metadata: tables, columns, foreign keys, indexes.
- Generate Java model classes from schema.
- Download model classes as `.zip`.
- Delete specific rows using primary keys.
- RESTful APIs for interacting with metadata.

### 🌐 Frontend (React + TypeScript)
- Upload DB config JSON to connect.
- Display tables and schema metadata.
- Preview column details and relationships.
- Toggle between light and dark mode.
- Trigger model export as `.zip`.

### 🗄️ MySQL
- Pre-built schema `university_db` with:
  - `departments`, `students`, `courses`, `enrollments`
- Sample data insertions included.

---
📦 Clone the Repository
-git clone https://github.com/ravindra-singh0507/Vistora_assignment-Crawler.git

-cd Vistora_assignment-Crawler

🔧 Run Backend (Spring Boot)
-cd crawler-backend
-./mvnw spring-boot:run
💻 Run Frontend (React + TypeScript)
-cd ../crawler-frontend
-npm install
-npm start

📌 Technologies Used
Backend:
Java 17,
Spring Boot,
JDBC,
ZipOutputStream

Frontend:
React 18,
TypeScript,
Tailwind CSS,
Axios

Database:
MySQL 8

Build Tools:
Maven
Vite
