set Dir=%~p0%
call %DIR%deploy-prepare.bat
call %DIR%remote-stop.bat
call %DIR%deploy-as-dev.bat
call %DIR%remote-start.bat

