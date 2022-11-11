#!/bin/bash

URL=jdbc:postgresql://localhost/hr
USERNAME=postgres
PASSWORD=password
LOG_FILE=migration/db.changelog.xml

cd ..
liquibase --url=$URL --username=$USERNAME --password=$PASSWORD --changeLogFile=$LOG_FILE drop-all
