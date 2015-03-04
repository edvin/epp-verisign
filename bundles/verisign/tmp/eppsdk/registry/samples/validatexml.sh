#!/bin/sh
XERCESJ_LIB=../../lib
export CLASSPATH=$XERCESJ_LIB/xercesImpl-2.11.0.jar:$XERCESJ_LIB/xercesSamples-2.11.0.jar:$XERCESJ_LIB/xmlParserAPIs-2.11.0.jar
date
java dom.Counter -n -v -s -f $@ 
date
