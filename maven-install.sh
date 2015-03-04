#!/bin/bash
verisignGroupId=com.verisign.epp
verisignVersion=1.4.0.0
modules="contact domain host gen idn secdns"

for module in $modules; do (
	cd $module
	chmod +x build.sh
	./build.sh
	cd ..
	mvn install:install-file -Dpackaging=jar -DgroupId=$verisignGroupId -Dversion=$verisignVersion -DartifactId=$module -Dfile=lib/epp/epp-$module.jar
	jar -cvf lib/epp/epp-$module-src.jar -C $module/java .
	mvn install:install-file -Dpackaging=jar -Dclassifier=sources -DgroupId=$verisignGroupId -Dversion=$verisignVersion -DartifactId=$module -Dfile=lib/epp/epp-$module-src.jar
)
done
