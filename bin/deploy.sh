SERVER='myclinic-remote-server:~/myclinic-scala-server/'
scp server/target/scala-3.0.2/server-assembly-0.1.0-SNAPSHOT.jar $SERVER
scp -r server/deploy $SERVER/web
