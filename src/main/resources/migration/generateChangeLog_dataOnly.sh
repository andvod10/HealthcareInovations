#!/bin/bash

liquibase --diffTypes="data" generateChangeLog
