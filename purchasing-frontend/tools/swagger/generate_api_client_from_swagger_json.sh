#!/bin/sh

# not really intended to be run as a script although it can, but these are the commands
# user to regenerate the API client code from Swagger when it changes.

rm -rf swagger_temp_json
mkdir swagger_temp_json
#cp lib/catalogue-api/swagger.json swagger_temp_json/
cp swagger.json swagger_temp_json/
docker run --rm -v ${PWD}:/local swaggerapi/swagger-codegen-cli:unstable generate -i ./local/swagger_temp_json/swagger.json -l javascript -o /local/swagger_temp_json --additional-properties usePromises=true,projectName=catalogue-api
#rm -rf lib/catalogue-api
#mv swagger_temp_json lib/catalogue-api
