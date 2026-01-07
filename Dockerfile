# ===============================
# Etap 1: budowa Angulara
# ===============================
FROM node:20-alpine AS angular-build

WORKDIR /app/frontend

# Kopiujemy package.json i package-lock.json
COPY frontend/TattooMe/package*.json ./

# Instalacja zależności Angulara
RUN npm ci

# Kopiujemy resztę frontend
COPY frontend/TattooMe/ ./

# Budowa produkcyjna Angulara
RUN npm run build -- --output-path=dist

# ===============================
# Etap 2: budowa Spring Boot
# ===============================
FROM eclipse-temurin:24-jdk AS spring-build

WORKDIR /app/backend

# Kopiujemy Maven Wrapper i pom.xml
COPY backend/TattooMe/mvnw ./
COPY backend/TattooMe/.mvn ./.mvn
COPY backend/TattooMe/pom.xml ./

# Uprawnienia do mvnw
RUN chmod +x mvnw

# Pobranie zależności offline
RUN ./mvnw dependency:go-offline

# Kopiujemy źródła backendu
COPY backend/TattooMe/src ./src

# Kopiujemy zbudowanego Angulara do folderu, który Spring Boot serwuje
COPY --from=angular-build /app/frontend/dist ./src/main/resources/static

# Budowa jar (pomijamy testy)
RUN ./mvnw clean package -DskipTests

# ===============================
# Etap 3: finalny obraz
# ===============================
FROM eclipse-temurin:24-jdk

WORKDIR /app

# Kopiujemy jar z poprzedniego etapu
COPY --from=spring-build /app/backend/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Uruchomienie aplikacji
ENTRYPOINT ["java","-jar","app.jar"]
