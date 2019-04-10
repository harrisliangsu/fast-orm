@echo off
:: 显示中文
CHCP 65001

set "fst=0"
set "fib=1"
set "limit=1000000000"
call:myFibo fib,%fst%,%limit%
echo.The next Fibonacci number greater or equal %limit% is %fib%.
echo.&pause&goto:eof
:myFibo -- calculate recursively
:myFibo -- calculate recursively the next Fibonacci number greater or equal to a limit
SETLOCAL
set /a "Number1=%~1"
set /a "Number2=%~2"
set /a "Limit=%~3"
set /a "NumberN=Number1 + Number2"

if /i %NumberN% LSS %Limit% call:myFibo NumberN,%Number1%,%Limit%
(ENDLOCAL
   IF "%~1" NEQ "" SET "%~1=%NumberN%"
)goto:eof