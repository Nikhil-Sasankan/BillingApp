@echo off
REM Set the path to the JRE directory relative to the batch file location
setlocal
set "JRE_DIR=%~dp0jre"

REM Check if the JRE directory exists
if not exist "%JRE_DIR%" (
    echo JRE directory not found: %JRE_DIR%
    exit /b 1
)
echo Java Runtime Folder present :  %JRE_DIR%
REM Set the path to the java executable
set "JAVA_EXE=%JRE_DIR%\bin\java.exe"

REM Check if the java executable exists
if not exist "%JAVA_EXE%" (
    echo Java executable not found: %JAVA_EXE%
    exit /b 1
)
%JAVA_EXE% -version
REM Set the path to the JAR file
set "JAR_FILE=%~dp0billerApp.jar"

REM Check if the JAR file exists
if not exist "%JAR_FILE%" (
    echo JAR file not found: %JAR_FILE%
    exit /b 1
)
echo Starting Biller App
REM Execute the JAR file using the specified JRE
"%JAVA_EXE%" -jar "%JAR_FILE%"

REM End the script
endlocal
