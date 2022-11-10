#!/bin/bash

liquibase --diffTypes="data,tables,views,columns,indexes,foreignkeys,primarykeys,uniqueconstraints" \
  --excludeObjects="spatial_ref_sys,geography_columns,geometry_columns,raster_columns,raster_overviews" \
  diff-changelog
