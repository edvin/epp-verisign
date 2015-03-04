rem #############################################################################

rem # Copyright (C) 2001 VeriSign Corporation

rem # 

rem # This library is free software; you can redistribute it and/or

rem # modify it under the terms of the GNU Lesser General Public

rem # License as published by the Free Software Foundation; either

rem # version 2.1 of the License, or (at your option) any later version.

rem #

rem # This library is distributed in the hope that it will be useful,

rem # but WITHOUT ANY WARRANTY; without even the implied warranty of

rem # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU

rem # Lesser General Public License for more details.

rem #

rem # You should have received a copy of the GNU Lesser General Public

rem # License along with this library; if not, write to the Free Software

rem # Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

rem #

rem # VeriSign Corporation. 

rem # 505 Huntmar Park Dr.

rem # Herndon, VA 20170

rem #

rem #############################################################

rem # Copyright (C) 2004 VeriSign, Inc.

rem #

rem # This library is free software; you can redistribute it and/or

rem # modify it under the terms of the GNU Lesser General Public

rem # License as published by the Free Software Foundation; either

rem # version 2.1 of the License, or (at your option) any later version.

rem #

rem # This library is distributed in the hope that it will be useful,

rem # but WITHOUT ANY WARRANTY; without even the implied warranty of

rem # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Ê See the GNU

rem # Lesser General Public License for more details.

rem #

rem # You should have received a copy of the GNU Lesser General Public

rem # License along with this library; if not, write to the Free Software

rem # Foundation, Inc., 59 Temple Place, Suite 330, Boston, MAÊ 02111-1307Ê USA

rem # 

rem # http://www.verisign.com/nds/naming/namestore/techdocs.html

rem #############################################################



@echo off



echo EPP Build System

echo -------------------



if "%JAVA_HOME%" == "" goto error



set LIB=..\lib

set ANT_LIB=%LIB%\ant



set LOCALCLASSPATH=%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\lib\dev.jar;%LIB%\xercesImpl-2.6.0.jar;%LIB%\xmlParserAPIs-2.6.0.jar;%ANT_LIB%\ant-launcher-1.7.1.jar;%ANT_LIB%\ant-1.7.1.jar;%ANT_LIB%\ant-junit-1.7.1.jar;%LIB%\junit-3.8.1.jar



echo Building with classpath %LOCALCLASSPATH%



echo Starting Ant...



%JAVA_HOME%\bin\java.exe -classpath "%LOCALCLASSPATH%" org.apache.tools.ant.Main %1 %2 %3 %4 %5



goto end



:error



echo ERROR: JAVA_HOME not found in your environment.

echo Please, set the JAVA_HOME variable in your environment to match the

echo location of the Java Virtual Machine you want to use.



:end



set LOCALCLASSPATH=



