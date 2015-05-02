#!/bin/bash
mvn clean install
cp target/jaxrs-analyzer/swagger.json src/main/webapp
