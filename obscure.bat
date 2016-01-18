@echo off
setlocal

set BASE=io.github.saka1029

java -cp bin %BASE%.script.CommandRunner -l %BASE%.obscure.core.ObscureProcessor

endlocal
