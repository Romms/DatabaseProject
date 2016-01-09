@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\javax\ws\rs\jsr311-api\1.1.1\jsr311-api-1.1.1.jar;"%REPO%"\org\jetbrains\annotations\15.0\annotations-15.0.jar;"%REPO%"\com\google\guava\guava\18.0\guava-18.0.jar;"%REPO%"\org\springframework\boot\spring-boot-starter-web\1.3.0.RELEASE\spring-boot-starter-web-1.3.0.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot-starter\1.3.0.RELEASE\spring-boot-starter-1.3.0.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot\1.3.0.RELEASE\spring-boot-1.3.0.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot-autoconfigure\1.3.0.RELEASE\spring-boot-autoconfigure-1.3.0.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot-starter-logging\1.3.0.RELEASE\spring-boot-starter-logging-1.3.0.RELEASE.jar;"%REPO%"\ch\qos\logback\logback-classic\1.1.3\logback-classic-1.1.3.jar;"%REPO%"\ch\qos\logback\logback-core\1.1.3\logback-core-1.1.3.jar;"%REPO%"\org\slf4j\slf4j-api\1.7.7\slf4j-api-1.7.7.jar;"%REPO%"\org\slf4j\jcl-over-slf4j\1.7.13\jcl-over-slf4j-1.7.13.jar;"%REPO%"\org\slf4j\jul-to-slf4j\1.7.13\jul-to-slf4j-1.7.13.jar;"%REPO%"\org\slf4j\log4j-over-slf4j\1.7.13\log4j-over-slf4j-1.7.13.jar;"%REPO%"\org\springframework\spring-core\4.2.3.RELEASE\spring-core-4.2.3.RELEASE.jar;"%REPO%"\org\yaml\snakeyaml\1.16\snakeyaml-1.16.jar;"%REPO%"\org\springframework\boot\spring-boot-starter-tomcat\1.3.0.RELEASE\spring-boot-starter-tomcat-1.3.0.RELEASE.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-websocket\8.0.28\tomcat-embed-websocket-8.0.28.jar;"%REPO%"\org\springframework\boot\spring-boot-starter-validation\1.3.0.RELEASE\spring-boot-starter-validation-1.3.0.RELEASE.jar;"%REPO%"\org\hibernate\hibernate-validator\5.2.2.Final\hibernate-validator-5.2.2.Final.jar;"%REPO%"\javax\validation\validation-api\1.1.0.Final\validation-api-1.1.0.Final.jar;"%REPO%"\org\jboss\logging\jboss-logging\3.2.1.Final\jboss-logging-3.2.1.Final.jar;"%REPO%"\com\fasterxml\classmate\1.1.0\classmate-1.1.0.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-databind\2.6.3\jackson-databind-2.6.3.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-annotations\2.6.0\jackson-annotations-2.6.0.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.6.3\jackson-core-2.6.3.jar;"%REPO%"\org\springframework\spring-web\4.2.3.RELEASE\spring-web-4.2.3.RELEASE.jar;"%REPO%"\org\springframework\spring-aop\4.2.3.RELEASE\spring-aop-4.2.3.RELEASE.jar;"%REPO%"\aopalliance\aopalliance\1.0\aopalliance-1.0.jar;"%REPO%"\org\springframework\spring-beans\4.2.3.RELEASE\spring-beans-4.2.3.RELEASE.jar;"%REPO%"\org\springframework\spring-context\4.2.3.RELEASE\spring-context-4.2.3.RELEASE.jar;"%REPO%"\org\springframework\spring-webmvc\4.2.3.RELEASE\spring-webmvc-4.2.3.RELEASE.jar;"%REPO%"\org\springframework\spring-expression\4.2.3.RELEASE\spring-expression-4.2.3.RELEASE.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-core\7.0.57\tomcat-embed-core-7.0.57.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-logging-juli\7.0.57\tomcat-embed-logging-juli-7.0.57.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-jasper\7.0.57\tomcat-embed-jasper-7.0.57.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-el\7.0.57\tomcat-embed-el-7.0.57.jar;"%REPO%"\org\eclipse\jdt\core\compiler\ecj\4.4\ecj-4.4.jar;"%REPO%"\org\apache\tomcat\tomcat-jasper\7.0.57\tomcat-jasper-7.0.57.jar;"%REPO%"\org\apache\tomcat\tomcat-servlet-api\7.0.57\tomcat-servlet-api-7.0.57.jar;"%REPO%"\org\apache\tomcat\tomcat-juli\7.0.57\tomcat-juli-7.0.57.jar;"%REPO%"\org\apache\tomcat\tomcat-el-api\7.0.57\tomcat-el-api-7.0.57.jar;"%REPO%"\org\apache\tomcat\tomcat-api\7.0.57\tomcat-api-7.0.57.jar;"%REPO%"\org\apache\tomcat\tomcat-util\7.0.57\tomcat-util-7.0.57.jar;"%REPO%"\org\apache\tomcat\tomcat-jasper-el\7.0.57\tomcat-jasper-el-7.0.57.jar;"%REPO%"\org\apache\tomcat\tomcat-jsp-api\7.0.57\tomcat-jsp-api-7.0.57.jar;"%REPO%"\me\dblab\dblab\1.0\dblab-1.0.jar

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS%  -classpath %CLASSPATH% -Dapp.name="webapp" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" me.dblab.server.WebServer %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
