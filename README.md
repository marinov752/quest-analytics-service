# Quest Analytics Service

## Overview
Quest Analytics Service is a REST microservice that provides analytics and statistics tracking for the Quest Gamification App. It handles quest completion tracking, user statistics, and analytics data aggregation.

## Technology Stack
- **Java**: 17
- **Spring Boot**: 3.4.0
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Framework**: Spring Web (REST API)

## Features

### Domain Entity
1. **QuestAnalytics** - Analytics data for users including total XP, completed quests, and current level

### Functionalities
1. **Record Quest Completion** (POST) - Records when a quest is completed with XP earned
2. **Update User Statistics** (PUT) - Updates user statistics (level, XP, quests completed)
3. **Delete Analytics Data** (DELETE) - Deletes analytics data for a user
4. **Get Analytics Data** (GET) - Retrieves analytics data for a specific user

### REST Endpoints

#### POST /api/analytics/quest-completion
Records a quest completion event.
- **Parameters**: userId, questId, experiencePoints
- **Returns**: 201 Created

#### PUT /api/analytics/user-stats
Updates user statistics.
- **Body**: UserStatsUpdateDto (userId, totalExperience, level, questsCompleted)
- **Returns**: 200 OK

#### DELETE /api/analytics/user/{userId}
Deletes analytics data for a user.
- **Path Variable**: userId
- **Returns**: 204 No Content

#### GET /api/analytics/user/{userId}
Retrieves analytics data for a user.
- **Path Variable**: userId
- **Returns**: 200 OK with analytics data (JSON)

### Database
- PostgreSQL database (separate from main application)
- UUID primary keys
- Tracks user analytics over time

### Validation & Error Handling
- DTO validation
- Global exception handlers for:
  - IllegalArgumentException (built-in)
  - CustomApplicationException (custom)
  - Generic Exception handler

### Logging
- All functionalities include logging statements
- Logs quest completions, statistics updates, and data retrieval

## Setup Instructions

### Prerequisites
- Java 17
- Maven 3.6+
- PostgreSQL 12+

### Database Setup

**Automatic Database Creation:**
The service will automatically create the database if it doesn't exist when you first run it. You just need to:
1. Make sure PostgreSQL is running
2. Ensure the `postgres` user has permission to create databases
3. Run the service - the database will be created automatically

**Manual Database Setup (if automatic creation fails):**
If automatic creation fails, you can create the database manually:
```sql
CREATE DATABASE quest_analytics_db;
```

**Schema Creation:**
The database schema (tables, columns, etc.) is automatically created/updated by Hibernate when the service starts (configured via `ddl-auto: update`).

**Update Configuration:**
Update `application.yml` with your database credentials if needed (default: username=`postgres`, password=`postgres`).

### Running the Service
1. Clone the repository
2. Navigate to the project directory
3. Run: `mvn spring-boot:run`
4. Service runs on: `http://localhost:8081`

### Testing
Run tests with: `mvn test`

## Project Structure
```
src/
├── main/
│   ├── java/com/questanalytics/
│   │   ├── controller/      # REST controllers
│   │   ├── domain/
│   │   │   └── entity/      # JPA entities
│   │   ├── exception/       # Exception handlers
│   │   ├── repository/      # JPA repositories
│   │   ├── service/         # Business logic services
│   │   └── QuestAnalyticsServiceApplication.java
│   └── resources/
│       └── application.yml  # Application configuration
└── test/                    # Test classes
```

## Integration
This microservice is consumed by the Quest Gamification App via Feign Client. The main application calls:
- POST endpoint when quests are completed
- PUT endpoint to update user statistics
- GET endpoint to retrieve analytics data

## API Documentation

### Request/Response Examples

#### Record Quest Completion
```http
POST /api/analytics/quest-completion?userId={uuid}&questId={uuid}&experiencePoints=100
```

#### Update User Statistics
```http
PUT /api/analytics/user-stats
Content-Type: application/json

{
  "userId": "uuid",
  "totalExperience": 1000,
  "level": 5,
  "questsCompleted": 10
}
```

#### Get Analytics Data
```http
GET /api/analytics/user/{userId}
```

Response:
```json
{
  "totalExperienceEarned": 1000,
  "totalQuestsCompleted": 10,
  "currentLevel": 5,
  "lastUpdated": "2025-01-01T12:00:00"
}
```

