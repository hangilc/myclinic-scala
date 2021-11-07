set -e
STAMP=$(date +%Y-%m%d-%H%M%S)
SRC=server/web/
DEPLOY=server/deploy
if [[ -d $DEPLOY ]]; then
  rm -rf $DEPLOY/*
else
  mkdir $DEPLOY
fi
cp -r $SRC/* $DEPLOY
TARGET=$DEPLOY/appoint
mv $TARGET/scalajs/main.js $TARGET/scalajs/main-$STAMP.js
mv $TARGET/scalajs/main.js.map $TARGET/scalajs/main-$STAMP.js.map
cat $TARGET/index.html | sed s/scalajs\\\/main.js/scalajs\\\/main-$STAMP.js/ >$TARGET/index.html

