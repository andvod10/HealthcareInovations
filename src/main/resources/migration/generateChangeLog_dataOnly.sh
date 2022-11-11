#!/bin/bash

liquibase --diffTypes="data" generate-changeLog
