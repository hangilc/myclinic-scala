@set TIMESTAMP=%date:~0,4%-%date:~5,2%%date:~8,2%-%time:~0,2%%time:~3,2%
ssh -t changclinic-server "mv ~/myclinic-scala-server/dev ~/myclinic-scala-server/arch/%TIMESTAMP%"
ssh -t changclinic-server "rm -f ~/myclinic-scala-server/current"
ssh -t changclinic-server "rm -f ~/myclinic-scala-server/save"
ssh -t changclinic-server "ln -s ~/myclinic-scala-server/arch/%TIMESTAMP% ~/myclinic-scala-server/save"
