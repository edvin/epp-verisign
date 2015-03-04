rem #########################################################################
rem # Copyright (C) 2010 VeriSign Corporation
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
rem # Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
rem #
rem # VeriSign Corporation.
rem # 505 Huntmar Park Dr.
rem # Herndon, VA 20170
rem #
rem #########################################################################
rem #
rem # The EPP, APIs and Software are provided "as-is" and without any
rem # warranty of any kind. VeriSign Corporation EXPRESSLY DISCLAIMS ALL
rem # WARRANTIES AND/OR CONDITIONS, EXPRESS OR IMPLIED, INCLUDING, BUT NOT
rem # LIMITED TO, THE IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY OR
rem # SATISFACTORY QUALITY AND FITNESS FOR A PARTICULAR PURPOSE AND
rem # NONINFRINGEMENT OF THIRD PARTY RIGHTS. VeriSign Corporation DOES NOT
rem # WARRANT THAT THE FUNCTIONS CONTAINED IN THE EPP, APIs OR SOFTWARE WILL
rem # MEET REGISTRAR'S REQUIREMENTS, OR THAT THE OPERATION OF THE EPP, APIs
rem # OR SOFTWARE WILL BE UNINTERRUPTED OR ERROR-FREE,OR THAT DEFECTS IN THE
rem # EPP, APIs OR SOFTWARE WILL BE CORRECTED.  FURTHERMORE, VeriSign
rem # Corporation DOES NOT WARRANT NOR MAKE ANY REPRESENTATIONS REGARDING THE
rem # USE OR THE RESULTS OF THE EPP, APIs, SOFTWARE OR RELATED DOCUMENTATION
rem # IN TERMS OF THEIR CORRECTNESS, ACCURACY, RELIABILITY, OR OTHERWISE.
rem # SHOULD THE EPP, APIs OR SOFTWARE PROVE DEFECTIVE, REGISTRAR ASSUMES THE
rem # ENTIRE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
rem #
rem # $Id: build.bat,v 1.1.1.1 2003/12/05 17:36:21 jim Exp $
rem #
rem #########################################################################

@echo off

echo EPP Build System
echo -------------------

if "%JAVA_HOME%" == "" goto error

set ANT_HOME=..
set LCP=%JAVA_HOME%\lib\tools.jar
set LCP=%LCP%;..\lib\poolman-2.1-b1.jar
set LCP=%LCP%;..\lib\xercesImpl-2.6.0.jar
set LCP=%LCP%;..\lib\junit-3.8.1.jar
set LCP=%LCP%;%ANT_HOME%\lib\ant\ant-1.7.1.jar
set LCP=%LCP%;%ANT_HOME%\lib\ant\ant-launcher-1.7.1.jar
set LCP=%LCP%;%ANT_HOME%\lib\ant\ant-junit-1.7.1.jar

echo Building with classpath %LCP%

echo Starting Ant...
"%JAVA_HOME%"\bin\java.exe -Dant.home="%ANT_HOME%" -classpath "%LCP%" org.apache.tools.ant.Main %1 %2 %3 %4 %5
goto end

:error
echo ERROR: JAVA_HOME not found in your environment.
echo Please, set the JAVA_HOME variable in your environment to match the
echo location of the Java Virtual Machine you want to use.

:end
set LCP=
