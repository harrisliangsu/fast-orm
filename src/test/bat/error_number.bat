@echo off
:: 显示中文
CHCP 65001

echo %errorLevel%

call Find.cmd

if errorLevel gtr 0 exit
echo "successful completion"
