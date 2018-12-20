#!/bin/sh

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


#sed 'N;s/[[:space:]]if \(string\.endsWith\("\+0000"\)\) {\n'\
#'[[:space:]]string \= string\.substring\(0\, string\.length\(\) \- 5\) + "Z"\;\n'\
#'[[:space:]]}\n'\
#'/a\ '\
#'        \/\/nima added via sed\n'\
#'        if \(string\.contains\("T"\) \&\& string\.length\(\) \=\= 19 \&\& !string\.contains\("Z"\)\) {\n'\
#'        	string \+\= "Z"\;\n'\
#'        }\n'\
#'/' swagger_temp_java/src/main/java/io/swagger/client/CustomInstantDeserializer.java

