#!/bin/bash
#java -cp ../target/dblab.jar:../target/rmi-classes me.dblab.server.RmiIiopServer
#java -classpath /home/user/Projects/DatabaseProject/target/classes:/home/user/Projects/DatabaseProject/target/repo/commons-cli/commons-cli/1.3.1/commons-cli-1.3.1.jar me.dblab.server.RmiIiopServer --port=7000 --database=/home/user/Users.db
./run_orbd.sh &
java -classpath ../target/dblab.jar:../target/rmi-classes me.dblab.server.RmiIiopServer
