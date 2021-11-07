#/bin/bash

STAMP=$(date +%Y-%m%d-%H%M%S)
mkdir arch/$STAMP
mv *.jar web arch/$STAMP
