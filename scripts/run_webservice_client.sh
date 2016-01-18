#!/bin/bash
#java -cp ../target/dblab.jar me.dblab.server.DatabaseWebService --port=7777
#java -classpath ../target/classes:../target/repo/commons-cli/commons-cli/1.3.1/commons-cli-1.3.1.jar me.dblab.server.DatabaseWebService --port=7000 --database=/home/user/Users.db
java -classpath ../target/classes me.dblab.client.GuiLauncher --use=WebServiceController
