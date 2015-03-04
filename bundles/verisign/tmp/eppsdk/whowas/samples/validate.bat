@echo off

rem Use Xerces to parse and validate local xml files

if "%JAVA_HOME%" == "" goto error

set XERCES_HOME=xerces
set LOCALCLASSPATH=%XERCES_HOME%\xercesImpl-2.9.1.jar;%XERCES_HOME%\xercesSamples-2.9.1.jar;%XERCES_HOME%\xml-apis-2.9.1.jar

set CMD="%JAVA_HOME%\bin\java.exe"
set CP=-classpath "%LOCALCLASSPATH%"
set CLASS=dom.Counter -n -v -s -f

%CMD% %CP% %CLASS% *xsd
echo.
echo You can safely ignore the following error:
echo   Cannot find the declaration of element 'schema'
echo.
echo.
echo.

%CMD% %CP% %CLASS% %1
echo.
goto end


:error
echo Error: The JAVA_HOME variable must be set


:end


REM %JAVA_HOME%\bin\jar.exe cf ../SchemasForEclipse.jar ./*.xsd epp.config logconfig.xml
REM set LOCALCLASSPATH=


