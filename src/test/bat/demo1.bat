echo "hello world"
CHCP 65001

:: 关闭回显
@echo off

:: 接收变量
echo %1
echo %2
echo %3

:: 声明变量
set name=sulaing
echo %name%

:: 声明数字变量
set /A a=5
set /A b=6
set /A c=%a% + %b%
echo %c%

:: 声明局部变量
setlocal
set var=13145
set /A var=%var%+5
echo %var%
endlocal

:: 环境变量
echo %JAVA_HOME%

:: 调用外部命令
java --version

:: annotation
rem annotation

echo off