STAMP=$(date +%Y-%m%d-%H%M%S)
BASE=server/web/appoint
mv $BASE/scalajs/main.js $BASE/scalajs/main-$STAMP.js
mv $BASE/scalajs/main.js.map $BASE/scalajs/main-$STAMP.js.map
cat $BASE/index.html | sed s/{{STAMP}}/$STAMP/g >$BASE/index.html
