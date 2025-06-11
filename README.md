# Management System - Users & Products

This is my Java Spring Boot assignment - an application built to demonstrate understanding of Java Spring Boot with both traditional web interfaces and REST APIs.

## Live Demo

**Application URL:** https://spring-managing-syetem-production.up.railway.app/UserManagement/

## Assignment Overview

This project combines two management systems into one comprehensive application:
- **User Management** - Traditional web interface with forms and server-side rendering
- **Product Management** - Hybrid approach with both web interface AND REST API

The assignment demonstrates the evolution from traditional Java web development to modern Spring Boot architecture.

## What This Assignment Covers

### User Management
- Complete CRUD operations for user data (names, emails, phone numbers, birth dates)
- Traditional form-based web interface with Thymeleaf templates
- Server-side validation and error handling

### Product Management
- Product catalog with CRUD operations
- **DUPLICATE** feature - Clone existing products
- **BUNDLE** feature - Create product bundles with relationship management
- Both web interface AND REST API endpoints
- Swagger documentation for API testing

## Technologies Demonstrated

**Backend:**
- Java 21 with Spring Boot 3.2
- Spring Data JPA for database operations
- Spring MVC for web layer
- Maven project management
- PostgreSQL database integration
- Embedded Tomcat server

**Frontend:**
- Thymeleaf template engine
- Custom CSS with dark theme
- Responsive web design
- Form validation and user feedback

**API & Testing:**
- REST API with OpenAPI/Swagger documentation
- MockMvc integration tests
- Comprehensive unit testing

**Architecture:**
- Clean layered architecture (Controller → Service → Repository → Entity)
- DTO pattern for data transfer
- Mapper pattern for entity conversions
- Domain-driven design structure

## My Learning Journey

Through this assignment, I gained experience with:

- **Spring Boot Migration** - Converted from traditional Java/JSP to Spring Boot
- **Clean Architecture** - Implemented proper separation of concerns
- **REST API Design** - Created comprehensive API with documentation
- **Domain Modeling** - Structured code by business domains (User/Product)
- **Testing Strategy** - MockMvc tests for API endpoints
- **Bundle Relationships** - Complex @ManyToMany JPA relationships
- **Railway Deployment** - Spring Boot cloud deployment

## Design Philosophy

The application showcases two different approaches:

### Traditional Web (Users)
- Server-side rendering with Thymeleaf
- Form submissions and redirects

### Hybrid (Products)
- Web interface for user interaction
- REST API for integration and testing

## Technical Challenges Solved

### Database Relationship Management
- Implemented product bundle relationships
- Prevented circular dependencies in bundle creation

## What's Under the Hood

**Backend Technologies:**
- Java 21 with Spring Boot 3.2
- Spring Data JPA with PostgreSQL
- Thymeleaf template engine
- Maven build system
- OpenAPI/Swagger documentation

**Architecture Patterns:**
- Domain-driven design structure
- Clean layered architecture
- DTO pattern for data transfer
- Repository pattern for data access
- Service layer for business logic

**Testing Framework:**
- Spring Boot Test
- MockMvc for API testing
- Mockito for mocking

## Setup Instructions

### Prerequisites
- Java 21+
- Maven 3.6+
- PostgreSQL database

### Local Development
1. **Clone the repository**
2. **Database setup** - Create PostgreSQL database named `usermanagement`
3. **Build the project**:
   ```bash
   mvn clean package
   ```
4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
5. **Access the application**:
    - Web Interface: `http://localhost:8080/UserManagement`
    - API Documentation: `http://localhost:8080/UserManagement/swagger-ui.html`

### Configuration

The application uses `application.properties` for configuration:

```properties
# Database Config
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/usermanagement}
spring.datasource.username=${PGUSER:postgres}
spring.datasource.password=${PGPASSWORD:root}

# Server Config
server.port=8080
server.servlet.context-path=/UserManagement
```

**Environment Variables** (for production deployment):
- `DATABASE_URL` - PostgreSQL connection URL
- `PGUSER` - Database username
- `PGPASSWORD` - Database password

## Application Features

### User Management
- **User listing** - View all users in responsive table
- **Add users** - Create new user records with validation
- **Edit users** - Update existing user information
- **Delete users** - Remove users with confirmation
- **Form validation** - Client and server-side validation

### Product Management
- **Product catalog** - View all products and bundles
- **Add products** - Create simple products
- **Edit products** - Update product information (bundles are readonly)
- **Delete products** - Remove products from catalog
- **Duplicate products** - Clone existing products with "(copy)" suffix
- **Create bundles** - Combine multiple products into bundles
- **Bundle management** - Automatic price calculation and relationship tracking
- **REST API** - Complete JSON API for all operations

## API Endpoints

### Products REST API
- `GET /api/products` - List all products
- `POST /api/products` - Create new product
- `GET /api/products/{id}` - Get product by ID
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `POST /api/products/{id}/duplicate` - Duplicate product
- `POST /api/products/bundle` - Create product bundle
- `GET /api/products/bundles` - List only bundles
- `GET /api/products/simple` - List only simple products
- `GET /api/products/search?name=term` - Search products

## Project Structure

```
src/main/java/com/example/management/
├── ManagementSystemApplication.java
├── user/                           # User domain
│   ├── controller/UserController.java
│   ├── service/UserService.java
│   ├── service/UserServiceImpl.java
│   ├── repository/UserRepository.java
│   ├── dto/UserDto.java
│   ├── mapper/UserMapper.java
│   └── model/User.java
├── product/                        # Product domain
│   ├── controller/ProductController.java
│   ├── controller/ProductApiController.java
│   ├── service/ProductService.java
│   ├── service/ProductServiceImpl.java
│   ├── repository/ProductRepository.java
│   ├── dto/ProductDto.java
│   ├── dto/CreateBundleRequest.java
│   ├── mapper/ProductMapper.java
│   └── model/Product.java
└── common/                         # Shared components
    └── config/

src/main/resources/
├── templates/                      # Thymeleaf templates
│   ├── index.html
│   ├── users/
│   │   ├── list.html
│   │   ├── add.html
│   │   └── edit.html
│   └── products/
│       ├── list.html
│       ├── add.html
│       ├── edit.html
│       └── bundle.html
├── static/css/                     # Stylesheets
│   ├── style.css
│   ├── forms.css
│   └── index.css
└── application.properties

src/test/java/                      # Test classes
└── product/controller/
    └── ProductApiControllerTest.java

Dockerfile                          # Container configuration
railway.toml                        # Railway deployment settings
```

## Testing

The project includes comprehensive testing:

### Controller Tests
- **MockMvc tests** for all API endpoints
- **Integration tests** for complete request/response cycles
- **Validation testing** for error scenarios

### Run Tests
```bash
mvn test
```

## Deployment

### Railway Platform
The application is deployed on Railway with automatic builds from Git repository.

### Docker Support
Includes Dockerfile for containerized deployment.

---

_Built with Spring Boot, curiosity, and lots of coffee_ ☕☕☕☕☕☕☕☕☕☕☕☕
