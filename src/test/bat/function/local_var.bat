@echo off
:: 显示中文
CHCP 65001

:: local variable in function
set str=Outer
echo %str%
CALL :SetValue str
echo %str%
:: this clause is necessary,why?
EXIT /B %ERRORLEVEL%

:SetValue
SETLOCAL
set str=Inner
set "%~1=%str%"
ENDLOCAL
EXIT /B 0