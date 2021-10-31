#FROM maven:3.6.3-openjdk-17 AS BUILDER
#COPY pom.xml /app/
#COPY feeds /app/feeds
#COPY shared-kernel /app/shared-kernel
#COPY users /app/users
#WORKDIR /app
#
#FROM openjdk:17
#WORKDIR /app
#COPY --from=builder /app/feeds/target/feeds-0.0.1-SNAPSHOT.jar .
#EXPOSE 9090
#CMD [ "java", "-jar", "/app/feeds-0.0.1-SNAPSHOT.jar" ]
#
#FROM openjdk:17
#WORKDIR /app
#COPY --from=builder /app/users/target/users-0.0.1-SNAPSHOT.jar .
#EXPOSE 9091
#CMD [ "java", "-jar", "/app/users-0.0.1-SNAPSHOT.jar" ]

# cache as most as possible in this multistage dockerfile.
FROM maven:3.6-alpine as DEPS

WORKDIR /opt/app
COPY feeds/pom.xml feeds/pom.xml
COPY shared-kernel/pom.xml shared-kernel/pom.xml
COPY users/pom.xml users/pom.xml

# you get the idea:
# COPY moduleN/pom.xml moduleN/pom.xml

COPY pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=shared-kernel

# if you have modules that depends each other, you may use -DexcludeArtifactIds as follows
# RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=module1

# Copy the dependencies from the DEPS stage with the advantage
# of using docker layer caches. If something goes wrong from this
# line on, all dependencies from DEPS were already downloaded and
# stored in docker's layers.
FROM maven:3.6.3-openjdk-17 as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app/ /opt/app
COPY feeds/src /opt/app/feeds/src
COPY shared-kernel/src /opt/app/shared-kernel/src
COPY users/src /opt/app/users/src

# use -o (--offline) if you didn't need to exclude artifacts.
# if you have excluded artifacts, then remove -o flag
RUN mvn -B -e clean install -DskipTests=true

# At this point, BUILDER stage should have your .jar or whatever in some path
FROM openjdk:17
WORKDIR /opt/app
COPY --from=builder /opt/app/users/target/users-0.0.1-SNAPSHOT.jar .
COPY --from=builder /opt/app/feeds/target/feeds-0.0.1-SNAPSHOT.jar .
EXPOSE 9090-9091
#CMD [ "java", "-jar", "/opt/app/users-0.0.1-SNAPSHOT.jar", "&" ,"java", "-jar", "/opt/app/feeds-0.0.1-SNAPSHOT.jar", ";" ,"fg" ]
ENTRYPOINT ["java", "-jar"]
CMD ["/opt/app/users-0.0.1-SNAPSHOT.jar"]