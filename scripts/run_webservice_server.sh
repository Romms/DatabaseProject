#!/bin/bash
#java -cp ../target/dblab.jar me.dblab.server.DatabaseWebService --port=7777
java -classpath /home/user/labs/DatabaseProject/target/classes:/home/user/labs/DatabaseProject/target/repo/commons-cli/commons-cli/1.3.1/commons-cli-1.3.1.jar me.dblab.server.DatabaseWebService --port=7000 --database=../sample.db
