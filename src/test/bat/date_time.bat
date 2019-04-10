@echo off
:: 显示中文
CHCP 65001

echo %date%
echo %time%

:: date in format
echo /Today is: %year%-%month%-%day%
goto:eof
setlocal enableExtensions
set t=2&if "%date%" lss "A" set t=1

for /f "skip=1 tokens=2-4 delims=(-)" %%a in ('echo/^|date') do (
    for /f "token=%t%-4 delims=.-/ " %%d in ('date/t') do (
        set %%a=%%d & set %%b=%%e & set %%c=%%f
    )
)
endlocal &set %1=%yy% & set %2=%mm% & set %3=%dd% & goto :eof
