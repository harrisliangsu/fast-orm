@echo off
:: 显示中文
CHCP 65001

:: call function
call :Display

:: function parameters
call :display 5 10

:: return value
setlocal
call :display value1,value2
echo %value1%
echo %value2%
rem exit /b %errorLevel%

:Display
set %~1=5
set %~2=10
echo the value of parameter 1 is %~1
echo the value of parameter 2 is %~2
exit /b 0

