#!/bin/sh
#############################################################################
# Copyright (C) 2010 VeriSign Corporation
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#
# VeriSign Corporation. 
# 505 Huntmar Park Dr.
# Herndon, VA 20170
#
##############################################################################
#
# The EPP, APIs and Software are provided "as-is" and without any warranty
# of any kind. VeriSign Corporation EXPRESSLY DISCLAIMS ALL WARRANTIES 
# AND/OR CONDITIONS, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, 
# THE IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY OR SATISFACTORY 
# QUALITY AND FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD 
# PARTY RIGHTS. VeriSign Corporation DOES NOT WARRANT THAT THE FUNCTIONS 
# CONTAINED IN THE EPP, APIs OR SOFTWARE WILL MEET REGISTRAR'S REQUIREMENTS, 
# OR THAT THE OPERATION OF THE EPP, APIs OR SOFTWARE WILL BE UNINTERRUPTED 
# OR ERROR-FREE,OR THAT DEFECTS IN THE EPP, APIs OR SOFTWARE WILL BE CORRECTED.
# FURTHERMORE, VeriSign Corporation DOES NOT WARRANT NOR MAKE ANY 
# REPRESENTATIONS REGARDING THE USE OR THE RESULTS OF THE EPP, APIs, SOFTWARE 
# OR RELATED DOCUMENTATION IN TERMS OF THEIR CORRECTNESS, ACCURACY, 
# RELIABILITY, OR OTHERWISE.  SHOULD THE EPP, APIs OR SOFTWARE PROVE DEFECTIVE,
# REGISTRAR ASSUMES THE ENTIRE COST OF ALL NECESSARY SERVICING, REPAIR OR 
# CORRECTION.
#
# $Id: build.sh,v 1.1.1.1 2003/12/05 17:36:21 jim Exp $
#
##############################################################################

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

LOCALCLASSPATH=$JAVA_HOME/lib/tools.jar:$LIB/xercesImpl-2.6.0.jar:$LIB/xmlParserAPIs-2.6.0.jar:$ANT_LIB/ant-launcher-1.7.1.jar:$ANT_LIB/ant-1.7.1.jar:$ANT_LIB/ant-junit-1.7.1.jar:$LIB/junit-3.8.1.jar


echo Building with classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath $LOCALCLASSPATH:$ADDITIONALCLASSPATH org.apache.tools.ant.Main $*
