@REM set TIMESTAMP=%date:~0,4%-%date:~5,2%%date:~8,2%-%time:~0,2%%time:~3,2%
ssh changclinic-server rm -rf ~/myclinic-scala-server/dev
scp -r server\target\pack changclinic-server:~/myclinic-scala-server/dev
