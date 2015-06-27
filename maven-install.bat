@echo off
setlocal

set verisignGroupId="com.verisign.epp"
set verisignVersion="1.4.0.0"
set modules=contact,domain,host,gen,idn,secdns

FOR %%i IN (%modules%) DO (
	cd %%i
	call build.bat
	cd ..
	mvn install:install-file -Dpackaging=jar -DgroupId=%verisignGroupId% -Dversion=%verisignVersion% -DartifactId=%%i -Dfile=lib\epp\epp-%%i.jar
	jar -cvf lib/epp/epp-%%i-src.jar -C %%i\java .
	mvn install:install-file -Dpackaging=jar -Dclassifier=sources -DgroupId=%verisignGroupId% -Dversion=%verisignVersion% -DartifactId=%%i -Dfile=lib\epp\epp-%%i-src.jar
)

endlocal