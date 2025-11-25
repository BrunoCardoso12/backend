# ============
#  BUILD STAGE
# ============
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copia apenas arquivos essenciais primeiro (cache de dependências)
COPY build.gradle settings.gradle gradle.properties ./ 
COPY gradle ./gradle

# Baixa dependências (melhora o cache)
RUN ./gradlew --no-daemon dependencies || true

# Agora copia o projeto completo
COPY . .

# Dá permissão para o gradlew
RUN chmod +x ./gradlew

# Build final
RUN ./gradlew clean build -x test --no-daemon


# =============
#  RUNTIME STAGE
# =============
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR do stage de build
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
