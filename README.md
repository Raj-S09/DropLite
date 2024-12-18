# Droplite - A Dockerized Full-Stack App

Droplite is a full-stack application for basic file handling that consists of a backend and frontend, with MySQL running in Docker containers. The app is designed to manage file uploads and provides a simple interface for users to upload, list and download files.

## Features
- **File Upload:** Upload files to the server.
- **File Download:** Download files that have been uploaded.
- **Database Integration:** A MySQL database stores metadata related to uploaded files.
- **Dockerized:** The entire app, including MySQL, backend, and frontend, is containerized using Docker.

## Technologies
- **Frontend:** React
- **Backend:** Java (Spring Boot)
- **Database:** MySQL
- **Containerization:** Docker, Docker Compose

## Prerequisites

Before you begin, ensure you have the following installed on your local machine:

- Docker
- Docker Compose

## Getting Started

### Clone the repository

Start by cloning this repository to your local machine:

```bash
git clone https://github.com/Raj-S09/DropLite.git
cd DropLite
```

## Run the Application

Build and start the containers with Docker Compose:

```bash
docker-compose up --build
```

This command will:

- Build the backend and frontend images.
- Start the MySQL container.
- Start the backend and frontend containers.

## Access the Application:

- Frontend: Navigate to http://localhost in your browser.
- Backend: The backend API is available at http://localhost:8080.

## Access the Database:

MySQL is running on port 3306. You can access the database through a MySQL client or use the following credentials:

- Username: droplite_user
- Password: droplite_password
- Database: droplite_db

## Access the Uploaded Files:

Uploaded files are saved at path: droplite-server/src/main/resources/data/uploads

## Access Logs:

Logs can be viewed for individual docker containers through these commands:

```bash
docker logs droplite-backend
docker logs droplite-frontend
docker logs droplite-db
```

## Stopping the Application

To stop the running containers, use:

```bash
docker-compose down
```