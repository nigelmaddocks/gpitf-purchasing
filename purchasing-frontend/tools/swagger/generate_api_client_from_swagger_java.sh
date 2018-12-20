#!/bin/sh

# not really intended to be run as a script although it can, but these are the commands
# user to regenerate the API client code from Swagger when it changes.

rm -rf swagger_temp_java
mkdir swagger_temp_java
#cp lib/catalogue-api/swagger.json swagger_temp_java/
cp swagger.json swagger_temp_java/
docker run --rm -v ${PWD}:/local swaggerapi/swagger-codegen-cli:unstable generate -i ./local/swagger_temp_java/swagger.json -l java --library=resttemplate -o /local/swagger_temp_java --additional-properties usePromises=true,projectName=catalogue-api
rm -rf ../../src/main/java/io
rm -rf ../../src/test/java/io

sed -i.bak 'N;s/[[:space:]]*if (string.endsWith("+0000")) {[[:space:]]*'\
'string \= string\.substring(0, string\.length() - 5) + "Z";[[:space:]]*'\
$'/ \\\n'\
$'        if \(string\.endsWith\("+0000"\)\) {\\\n'\
$'          string \= string\.substring\(0, string\.length\(\) - 5\) + "Z"\;\\\n'\
$'        }\\\n'\
$'        \/\/nima added via sed\\\n'\
$'        if \(string\.contains\("T"\) \&\& string\.length\(\) \=\= 19 \&\& !string\.contains\("Z"\)\) {\\\n'\
$'          string \+\= "Z"\;'\
$'/' swagger_temp_java/src/main/java/io/swagger/client/CustomInstantDeserializer.java

mv swagger_temp_java/src/main/java/io ../../src/main/java/io
mv swagger_temp_java/src/test/java/io ../../src/test/java/io
