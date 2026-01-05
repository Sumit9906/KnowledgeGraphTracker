@echo off
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"
mvn -q -DskipTests compile exec:java -Dexec.args="%*"
