#!/bin/bash

DRIVER=migration/postgresql-42.3.5.jar
URL=jdbc:postgresql://localhost/hr
USERNAME=postgres
PASSWORD=password
LOG_FILE=migration/db.changelog.xml

cd ..
liquibase --classpath=$DRIVER --url=$URL --username=$USERNAME --password=$PASSWORD --changeLogFile=$LOG_FILE drop-all
