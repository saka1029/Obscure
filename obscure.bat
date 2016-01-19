@echo off
setlocal

set BASE=com.github.saka1029.obscure

java -cp bin %BASE%.command.CommandRunner -l %BASE%.core.ObscureCommand

endlocal
