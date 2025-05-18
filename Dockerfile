# syntax=docker/dockerfile:1

# Comments are provided throughout this file to help you get started.
# If you need more help, visit the Dockerfile reference guide at
# https://docs.docker.com/go/dockerfile-reference/

# Want to help us make this template better? Share your feedback here: https://forms.gle/ybq9Krt8jtBL3iCk7

################################################################################

# Create a stage for resolving and downloading dependencies.
FROM eclipse-temurin:17-jdk-jammy as build

WORKDIR /app

# Copy maven executable
COPY mvnw ./
COPY .mvn/ .mvn/
COPY pom.xml ./

# Make the mavnw script executable
RUN chmod +x ./mvnw

# Download all dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Package the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

################################################################################

# Create a new stage for running the application that contains the minimal
# runtime dependencies for the application. This often uses a different base
# image from the install or build stage where the necessary files are copied
# from the install stage.
#
# The example below uses eclipse-turmin's JRE image as the foundation for running the app.
# By specifying the "17-jre-jammy" tag, it will also use whatever happens to be the
# most recent version of that tag when you build your Dockerfile.
# If reproducibility is important, consider using a specific digest SHA, like
# eclipse-temurin@sha256:99cede493dfd88720b610eb8077c8688d3cca50003d76d1d539b0efc8cca72b4.
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy application from build stage
COPY --from=build /app/target/*.jar app.jar

# Set active profile to docker
ENV SPRING_PROFILES_ACTIVE=docker

# Default values for environment variables (sẽ bị ghi đè bởi biến môi trường từ compose)
ENV DB_USERNAME=default_user
ENV DB_PASSWORD=default_password
ENV MAIL_USERNAME=default@example.com
ENV MAIL_PASSWORD=default_mail_password

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
