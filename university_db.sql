-- Create the database
CREATE DATABASE university_db;
USE university_db;
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    date_of_birth DATE,
    enrollment_date DATE,
    INDEX idx_lastname (last_name)
);
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100),
    course_code VARCHAR(20) UNIQUE,
    credits INT NOT NULL
);
CREATE TABLE professors (
    professor_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    department VARCHAR(100),
    INDEX idx_professor_department (department)
);
CREATE TABLE enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrollment_date DATE,
    grade VARCHAR(2),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    UNIQUE KEY unique_enrollment (student_id, course_id)
);
CREATE TABLE course_professors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT,
    professor_id INT,
    assigned_date DATE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (professor_id) REFERENCES professors(professor_id),
    UNIQUE KEY unique_assignment (course_id, professor_id)
);
CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100),
    location VARCHAR(100),
    UNIQUE KEY unique_department_name (department_name)
);
ALTER TABLE professors
    ADD department_id INT,
    ADD CONSTRAINT fk_professors_department FOREIGN KEY (department_id) REFERENCES departments(department_id);

-- Insert into departments
INSERT INTO departments (department_name, location) VALUES
('Computer Science', 'Building A'),
('Mathematics', 'Building B'),
('Physics', 'Building C');

-- Insert into professors
INSERT INTO professors (first_name, last_name, email, department_id) VALUES
('Alice', 'Johnson', 'alice.johnson@univ.edu', 1),
('Bob', 'Smith', 'bob.smith@univ.edu', 2),
('Carol', 'Brown', 'carol.brown@univ.edu', 3);

-- Insert into students
INSERT INTO students (first_name, last_name, email, date_of_birth, enrollment_date) VALUES
('David', 'Miller', 'david.miller@student.univ.edu', '2000-01-15', '2020-09-01'),
('Emma', 'Davis', 'emma.davis@student.univ.edu', '1999-07-22', '2019-09-01');

-- Insert into courses
INSERT INTO courses (course_name, course_code, credits) VALUES
('Data Structures', 'CS201', 4),
('Calculus I', 'MATH101', 3),
('Quantum Mechanics', 'PHYS301', 4);

-- Insert into enrollments
INSERT INTO enrollments (student_id, course_id, enrollment_date, grade) VALUES
(1, 1, '2020-09-10', 'A'),
(1, 2, '2020-09-10', 'B'),
(2, 3, '2019-09-10', 'A');

-- Insert into course_professors
INSERT INTO course_professors (course_id, professor_id, assigned_date) VALUES
(1, 1, '2020-08-20'),
(2, 2, '2020-08-20'),
(3, 3, '2020-08-20');
