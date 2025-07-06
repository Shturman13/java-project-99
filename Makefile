setup:
	npm install
	./gradlew wrapper --gradle-version 8.14.1
	./gradlew build

frontend:
	make -C frontend start

backend:
	./gradlew bootRun --args='--spring.profiles.active=dev'

clean:
	./gradlew clean

build:
	./gradlew clean build

dev:
	heroku local

reload-classes:
	./gradlew -t classes

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew installDist

# start-dist:
# 	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

# report:
# 	./gradlew jacocoTestReport

update-js-deps:
	cd frontend && npx ncu -u

update-deps:
	./gradlew refreshVersions

report:
	./gradlew jacocoTestReport

sonar: build
	./gradlew sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.token=$(SONAR_TOKEN) -Dsonar.gradle.skipCompile=true

# generate-migrations:
# 	gradle diffChangeLog

# db-migrate:
# 	./gradlew update


.PHONY: build frontend clean lint config test



