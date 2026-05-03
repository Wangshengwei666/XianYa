# XianYa

XianYa (闲鸭) is a campus second-hand trading platform built with Spring Boot, Vue 2, MyBatis, MySQL, and Element UI. It includes user accounts, item publishing, image upload, favorites, messages, orders, administrator management, and optional AI-assisted item description generation through Alibaba Cloud DashScope.

## Online Demo

- Website: [xianya.site](http://xianya.site/#/index)

## Tech Stack

- Backend: Spring Boot 2.4.0, MyBatis, Maven, Java 8+
- Frontend: Vue 2, Vue CLI, Element UI, Axios
- Database: MySQL 5.7+
- Optional AI: DashScope / Qwen API

## Project Structure

```text
XianYa/
  DB_School_Trade.sql        # Database schema and demo data
  school-trade-backend/      # Spring Boot backend
  school-trade-frontend/     # Vue frontend
```

## Prerequisites

- JDK 8 or newer
- Maven 3.6+
- Node.js 16 is recommended. If you use Node.js 17 or newer with this old Vue CLI/Webpack stack, set `NODE_OPTIONS=--openssl-legacy-provider` before running frontend commands.
- MySQL 5.7 or newer

## Database Setup

Create the database and import the initialization script:

```sql
CREATE DATABASE db_school_trade
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

```bash
mysql -u root -p db_school_trade < DB_School_Trade.sql
```

The script includes demo accounts:

- User: `18800000001` / `123456`
- Admin: `19900000001` / `123456`

## Backend Setup

```bash
cd school-trade-backend
```

Configure the database password and optional AI key with environment variables.

PowerShell:

```powershell
$env:SPRING_DATASOURCE_PASSWORD="your_mysql_password"
$env:DASHSCOPE_API_KEY="your_dashscope_api_key"
mvn spring-boot:run
```

Bash:

```bash
export SPRING_DATASOURCE_PASSWORD="your_mysql_password"
export DASHSCOPE_API_KEY="your_dashscope_api_key"
mvn spring-boot:run
```

Useful backend environment variables:

| Variable | Default | Description |
| --- | --- | --- |
| `SERVER_PORT` | `8080` | Backend server port |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/db_school_trade...` | MySQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `root` | MySQL username |
| `SPRING_DATASOURCE_PASSWORD` | empty | MySQL password |
| `APP_BASE_URL` | `http://localhost:8080` | Base URL used when returning uploaded image URLs |
| `APP_UPLOAD_DIR` | `uploads` | Upload directory |
| `DASHSCOPE_API_KEY` | empty | DashScope API key for AI description generation |

The backend runs at `http://localhost:8080` by default.

## Frontend Setup

```bash
cd school-trade-frontend
npm install
npm run serve
```

PowerShell users on Node.js 17 or newer can run:

```powershell
$env:NODE_OPTIONS="--openssl-legacy-provider"
npm run serve
```

The frontend runs at `http://localhost:8081` by default.

To point the frontend to a different backend, copy `.env.example` to `.env.local` and edit:

```env
VUE_APP_API_BASE_URL=http://localhost:8080
```

## Build

Backend:

```bash
cd school-trade-backend
mvn clean package
```

Frontend:

```bash
cd school-trade-frontend
npm run build
```

## Security Notes

Do not commit local database passwords, API keys, uploaded files, or `.env.local` files. Use environment variables for secrets.
