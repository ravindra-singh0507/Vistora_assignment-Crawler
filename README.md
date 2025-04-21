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

## 🚀 Getting Started
1) Clone the repository:
git clone https://github.com/ravindra-singh0507/Vistora_assignment-Crawler
cd Vistora_assignment-Crawler
2) Run Backend (Spring Boot):
cd crawler-backend
./mvnw spring-boot:run
3) Run frontend(React):
cd crawler-frontend
npm install
npm start

📌 Technologies Used
Backend: Java 17, Spring Boot 3, JDBC, ZipOutputStream
Frontend: React 18, TypeScript, Tailwind CSS, Axios
Database: MySQL 8
Build Tools: Maven, Vite
