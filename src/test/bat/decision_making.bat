@echo off
:: 显示中文
CHCP 65001

:: if
:: check integer variables
SET /A a=5
SET /A b=10
SET /A c=%a% + %b%
if %c%==15 echo "The value of variable c is 15"
if %c%==10 echo "The value of variable c is 10"
:: check string variables
SET str1=String1
SET str2=String2
if %str1%==String1 echo "The value of variable String1"
if %str2%==String3 echo "The value of variable c is String3"
:: check command line arguments
echo %1
echo %2
echo %3
if %1%==1 echo "The value is 1"
if %2%==2 echo "The value is 2"
if %3%==3 echo "The value is 3"

:: if else
:: check integer variables
set /A a=5
set /A b=10
set /A c=%a%+%b%
if %c%==15 (echo "The value of variable c is 15") else (echo "unknown value")
if %c%==10 (echo "The value of variable c is 10") else (echo "unknown value")

:: if defined
set str1=string1
set str2=string2
if defined str1 echo "variable str1 is defined"
if defined str3 (echo "variable str3 is defined") else (echo "variable str3 is not defined")

:: if exists
if exist c:\set2.txt echo "file exists"
if exist c:\logdata.dat (echo "file exists") else (echo "file dose not exist")