@echo off
:: 显示中文
CHCP 65001

:: list process
taskList

:: start process
start "open java" "C:\Program Files (x86)\Notepad++\notepad++.exe" "E:\Project\SVN-Server\app\AccountServer\build.properties"

:: kill process
rem taskKill /pid 12140
rem taskKill /f /im notepad++.exe