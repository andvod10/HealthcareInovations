#!/bin/bash

liquibase --diffTypes="tables,views,columns,indexes,foreignkeys,primarykeys,uniqueconstraints" \
  --excludeObjects="spatial_ref_sys,geography_columns,geometry_columns,raster_columns,raster_overviews" \
  generate-changeLog
