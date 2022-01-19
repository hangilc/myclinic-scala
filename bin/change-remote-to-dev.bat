ssh -t changclinic-server -t "sudo systemctl stop myclinic-scala"
ssh -t changclinic-server -t "cd myclinic-scala-server && rm -f current && ln -s dev current"
ssh -t changclinic-server -t "sudo systemctl start myclinic-scala"