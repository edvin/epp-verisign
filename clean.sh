#!/bin/bash

modules="contact domain host gen idn secdns"

for module in $modules; do (
        cd $module
        ./build.sh clean
        cd ..
)
done
