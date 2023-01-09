rm -rf target
mkdir target
cp -r ../../server/target/pack ./target
docker build -t hangilc/myclinic-scala .
