# Quest Analytics Service

## Overview
Quest Analytics Service is a REST microservice that provides analytics and statistics tracking for the Quest Gamification App. It handles quest completion tracking, user statistics, and analytics data aggregation. This is a critical component of the distributed quest gamification system.

## Technology Stack
- **Java**: 17
- **Spring Boot**: 3.4.0
- **Build Tool**: Maven
- **Database**: PostgreSQL (separate from main application)
- **Framework**: Spring Web (REST API)
- **Testing**: JUnit 5, Mockito
- **Logging**: SLF4J

<<<<<<< HEAD
=======
### REST API Endpoints
All endpoints follow REST constraints and use appropriate HTTP methods:

#### POST /api/analytics/quest-completion
Records a quest completion event.
- **Purpose**: Record XP earned from quest completion
- **Parameters**: userId (UUID), questId (UUID), experiencePoints (Long)
- **Returns**: 201 Created
- **Invoked by**: Main application when quest is completed

#### PUT /api/analytics/user-stats
Updates user statistics.
- **Purpose**: Update comprehensive user statistics
- **Body**: UserStatsUpdateDto containing userId, totalExperience, level, questsCompleted
- **Returns**: 200 OK
- **Invoked by**: Main application for periodic statistics sync

#### DELETE /api/analytics/user/{userId}
Deletes all analytics data for a user.
- **Purpose**: Clean up user data on account deletion
- **Path Variable**: userId (UUID)
- **Returns**: 204 No Content
- **Invoked by**: Main application on user account deletion

#### GET /api/analytics/user/{userId}
Retrieves analytics data for a specific user.
- **Purpose**: Get user's current stats and analytics
- **Path Variable**: userId (UUID)
- **Returns**: 200 OK with JSON analytics data
- **Invoked by**: Main application for stats display

### Database
- **PostgreSQL** (separate from main application)
- **Schema**: Automatically created on startup
- **UUID Primary Keys**: All entities use UUID for primary key
- **Relationships**: N/A (single entity microservice)

### Data Validation & Error Handling
- DTO validation with Jakarta Validation annotations
- Two exception handlers implemented:
  - `IllegalArgumentException` - Handles invalid input parameters
  - `CustomApplicationException` - Handles custom business logic errors
  - Generic `Exception` handler - Catches unexpected errors
- All error responses include meaningful error messages
- No application crashes or white-label error pages

### Logging
- All functionalities include logging statements:
  - Quest completion recording
  - Statistics updates
  - Data retrieval
  - Error conditions
- Uses SLF4J with appropriate log levels (INFO, ERROR)

### Testing
- **Unit Tests**: QuestAnalyticsServiceTest (service layer)
- **Integration Tests**: QuestAnalyticsIntegrationTest (repository layer)
- **API Tests**: QuestAnalyticsControllerApiTest (controller layer)
- **Coverage**: 80%+ line coverage

### Code Quality
- No dead code or unused imports
- Proper Java naming conventions (PascalCase for classes, camelCase for methods/variables)
- Consistent formatting and indentation
- No comments or TODOs in production code
- Thin controller principle: controllers delegate to services
- Layered architecture: controller → service → repository
- Non-static fields are private

>>>>>>> 50aa37ca0af800dad83f6d81e88bd79bce1aff4c
## Features

### Domain Entity
1. **QuestAnalytics** - Analytics data for users including:
   - Total XP earned
   - Completed quests count
   - Current level
   - Last updated timestamp

### Functionalities
1. **Record Quest Completion** (POST) - Records when a quest is completed with XP earned
   - Triggered: When user completes a quest in main application
   - State Change: Updates analytics record
   - Result: User sees XP/level update

2. **Update User Statistics** (PUT) - Updates user statistics (level, XP, quests completed)
   - Triggered: Periodic sync from main application
   - State Change: Updates analytics record with new values
   - Result: Analytics reflect current user state

3. **Delete Analytics Data** (DELETE) - Deletes analytics data for a user
   - Triggered: User account deletion
   - State Change: Removes analytics record
   - Result: User data cleaned up

4. **Get Analytics Data** (GET) - Retrieves analytics data for a specific user
   - Triggered: Stats page view in main application
   - Result: Analytics displayed to user

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

### Database Setup

**Automatic Database Creation:**
The service will automatically create the database if it doesn't exist when you first run it.
1. Ensure PostgreSQL is running on localhost:5432
2. Default credentials: username=`postgres`, password=`postgres`
3. The service will create `quest_analytics_db` automatically
4. Run: `mvn spring-boot:run`

**Manual Database Setup:**
If automatic creation fails, execute manually:
```sql
CREATE DATABASE quest_analytics_db;
```

### Configuration
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/quest_analytics_db
    username: postgres
    password: postgres
```

### Running the Service
```bash
cd quest-analytics-service
mvn clean spring-boot:run
```
Service runs on: `http://localhost:8081`

### Testing
```bash
mvn test
```

## Project Structure
```
src/
├── main/
│   ├── java/com/questanalytics/
│   │   ├── config/
│   │   │   └── DatabaseInitializer.java
│   │   ├── controller/
│   │   │   └── QuestAnalyticsController.java
│   │   ├── domain/
│   │   │   └── entity/
│   │   │       └── QuestAnalytics.java
│   │   ├── exception/
│   │   │   ├── CustomApplicationException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── repository/
│   │   │   └── QuestAnalyticsRepository.java
│   │   ├── service/
│   │   │   ├── QuestAnalyticsService.java
│   │   │   └── UserStatsUpdateDto.java
│   │   └── QuestAnalyticsServiceApplication.java
│   └── resources/
│       └── application.yml
└── test/
    ├── java/com/questanalytics/
    │   ├── controller/
    │   │   └── QuestAnalyticsControllerApiTest.java
    │   ├── integration/
    │   │   └── QuestAnalyticsIntegrationTest.java
    │   └── service/
    │       └── QuestAnalyticsServiceTest.java
    └── resources/
        └── application-test.yml
```

## API Documentation

### Request/Response Examples

#### Record Quest Completion
```http
POST /api/analytics/quest-completion?userId=550e8400-e29b-41d4-a716-446655440000&questId=6ba7b810-9dad-11d1-80b4-00c04fd430c8&experiencePoints=100 HTTP/1.1
Content-Type: application/x-www-form-urlencoded

Response: 201 Created
```

#### Update User Statistics
```http
PUT /api/analytics/user-stats HTTP/1.1
Content-Type: application/json

{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "totalExperience": 1500,
  "level": 6,
  "questsCompleted": 12
}

Response: 200 OK
```

#### Get Analytics Data
```http
GET /api/analytics/user/550e8400-e29b-41d4-a716-446655440000 HTTP/1.1

Response: 200 OK
Content-Type: application/json

{
  "totalExperienceEarned": 1500,
  "totalQuestsCompleted": 12,
  "currentLevel": 6,
  "lastUpdated": "2025-01-15T14:30:00"
}
```

#### Delete Analytics Data
```http
DELETE /api/analytics/user/550e8400-e29b-41d4-a716-446655440000 HTTP/1.1

Response: 204 No Content
```

## Integration with Main Application
The Quest Gamification App consumes this microservice via Feign Client:
- Calls POST endpoint when quests are completed
- Calls PUT endpoint to synchronize user statistics
- Calls GET endpoint to retrieve analytics for stats page
- Calls DELETE endpoint when user accounts are deleted

Both applications run independently on separate ports (8080 and 8081).

## Git Commits
The project includes 5+ valid commits following Conventional Commits format:
- feat: implement analytics data persistence with UUID primary keys
- feat: add comprehensive error handling and validation layer
- feat: add QuestAnalytics REST controller
- test: fix deprecated MockBean annotations and unused imports
- docs: update README with comprehensive API documentation

