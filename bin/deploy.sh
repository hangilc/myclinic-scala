scp -r -i ~/.ssh/changclinic-ec2.pem \
  server/target/scala-3.0.2/server-assembly-0.1.0-SNAPSHOT.jar \
  server/web/ \
  ubuntu@$MYCLINIC_REMOTE_SERVER:~/myclinic-scala-server/
