@echo off
:: 显示中文
CHCP 65001

:: create file
echo hello >e:\test.txt

:: writing file
echo hello >e:\test.txt

:: appending to file
echo how are you >>e:\test.txt

:: reading file
for /f "tokens=* delims=" %%x in (e:\test.txt) do echo %%x

:: delete file
rem del e:\test.txt

:: rename file
rename e:\test.txt tests.txt

:: move file
move /y e:\tests.txt e:\Temp\tests.txt

:: pipes
dir |sort
dir /s /b find "txt"

:: taskList
taskList |find "QQ"