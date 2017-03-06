#!/bin/sh

VERSION=`./gradlew appVersion -q`

./gradlew clean

if [ ! -f build/libs/osori-admin-${VERSION}.jar ]; then
	./gradlew build -x test
fi

java \
	-XX:MaxMetaspaceSize=100m \
	-Xmx1024m \
	-Dspring.profiles.active=h2 \
	-jar build/libs/osori-admin-${VERSION}.jar


