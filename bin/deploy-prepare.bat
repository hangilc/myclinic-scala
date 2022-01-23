call sbt clean
call sbt server/pack
call sbt appointApp/fullLinkJS
call sbt receptionApp/fullLinkJS
xcopy /E /I server\web server\target\pack\web\
