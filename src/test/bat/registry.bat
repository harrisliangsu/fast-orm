@echo off
:: 显示中文
CHCP 65001

:: read from registry
EG QUERY HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Windows\

:: add to registry
REG ADD HKEY_CURRENT_USER\Console /v Test /d "Test Data"

:: delete from registry
REG DELETE HKEY_CURRENT_USER\Console /v Test /f

:: copy registry keys
REG COPY HKEY_CURRENT_USER\Console HKEY_CURRENT_USER\Console\Test

:: compare registry keys
REG COMPARE HKEY_CURRENT_USER\Console HKEY_CURRENT_USER\Console\Test