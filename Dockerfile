# ---- Build Stage ----
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# 複製 Maven Wrapper
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn

# 複製所有 pom.xml（根目錄 + 各模組）
COPY pom.xml .
COPY health/pom.xml health/
COPY common/pom.xml common/
COPY auth/pom.xml auth/
COPY finance-core/pom.xml finance-core/
COPY application/pom.xml application/

# 先下載依賴（利用 Docker cache）
RUN ./mvnw dependency:go-offline -B

# 複製原始碼
COPY health/src health/src
COPY common/src common/src
COPY auth/src auth/src
COPY finance-core/src finance-core/src
COPY application/src application/src

# 打包（跳過測試）
RUN ./mvnw package -DskipTests -B

# ---- Run Stage ----
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/application/target/novaledger.jar app.jar

EXPOSE 8111

ENTRYPOINT ["java", "-jar", "app.jar"]