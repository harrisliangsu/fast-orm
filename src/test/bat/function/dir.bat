@echo off
:: 显示中文
CHCP 65001

:: create
mkdir e:\batch\text
md e:\batch\text\script
md "Test A"

:: remove dir
rd /q /s  e:\batch\text\script
rd /q /s e:\batch\text
rd /q /s "Test A"