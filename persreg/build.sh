#!/bin/sh
#############################################################
# Copyright (C) 2004 VeriSign, Inc.
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Ê See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MAÊ 02111-1307Ê USA
# 
# http://www.verisign.com/nds/naming/namestore/techdocs.html
#############################################################

echo
echo "EPP Build System"
echo "-------------------"
echo

if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

LIB=../lib 
ANT_LIB=$LIB/ant

LOCALCLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dev.jar:$LIB/xercesImpl-2.6.0.jar::$LIB/xmlParserAPIs-2.6.0.jar:$ANT_LIB/ant-launcher-1.7.1.jar:$ANT_LIB/ant-1.7.1.jar:$ANT_LIB/ant-junit-1.7.1.jar:$LIB/junit-3.8.1.jar

echo Building with classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH org.apache.tools.ant.Main $*
