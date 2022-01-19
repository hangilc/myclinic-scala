ssh -t changclinic-server -t "sudo systemctl stop myclinic-scala"
ssh -t changclinic-server -t "cd myclinic-scala-server && rm -f current && cp -P save current"
ssh -t changclinic-server -t "sudo systemctl start myclinic-scala"