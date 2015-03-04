@echo off

rem Use Xerces to parse and validate local xml files

if "%JAVA_HOME%" == "" goto error

set XERCES_HOME=..\..\lib\xerces-2_8_1
set LOCALCLASSPATH=%XERCES_HOME%\xercesImpl.jar;%XERCES_HOME%\xercesSamples.jar;%XERCES_HOME%\xml-apis.jar

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

%CMD% %CP% %CLASS% *xml
echo.
goto end


:error
echo Error: The JAVA_HOME variable must be set


:end
pause

cd ..
%JAVA_HOME%\bin\jar.exe cf SchemasForEclipse.jar schemas/*.xsd epp.config logconfig.xml
set LOCALCLASSPATH=


