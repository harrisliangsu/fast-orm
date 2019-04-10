@echo off
:: 显示中文
CHCP 65001

:: create String
set message1=Hello World
echo %message1%

:: create a empty String
set message2=
if [%message2%]==[] echo "message2 is empty"
echo %message2%

:: String interpolation
set message3=Hello
set message4=World
set /A n1=50
set message5=%message3% and %message4% %n1%
echo %message5%

:: String concatenation
set message6=Hello
set message7=World
set message8=%message6% and %message7%
echo %message8%

:: String length
set str1=Hello World
call :str_len str1 str_len
echo String is %str_len% characters long
exit /b

:str_len
setlocal enabledelayedexpansion

:str_len_loop
    if not "!%1:~%len%!"=="" set /A len+=1 & goto :str_len_loop
    (endlocal & set %2=%len%)
rem goto :eof

:: toInt
set var1=1314
set /A var1=%var1%+5
echo %var1%

:: align right
set x=1000
set y=1
set y=%y%
echo x %x%

set y=%y:~-4%
echo y %y%

:: left String
set str2=Helloworld
echo %str2%

set str2=%str2:~0,5%
echo %str2%

:: middle String
set str3=Helloworld
echo %str3%

set str3=%str3:~3,8%
echo %str3%

:: remove
set str4=Batch scripts is easy. It is really easy.
echo %str4%

set str4=%str4:is=%
echo %str4%

:: remove both ends
set str5=Batch scripts is easy. It is really easy.
echo %str5%

set str5=%str5:~1,-1%
echo %str5%

:: remove all spaces
set str6=This string has a lot of spaces
echo %str6%

set str6=%str6: =%
echo %str6%

:: replace a String
set str7=This message needs changed.
echo %str7%

set str7=%str7:needs=has%
echo %str7%

:: right String
set str8=This message needs changed.
echo %str%

set str8=%str8:~-8%
echo %str8%