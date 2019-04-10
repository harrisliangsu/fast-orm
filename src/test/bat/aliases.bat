@echo off
:: 显示中文
CHCP 65001

:: create alias
doskey cc=cd c:\

:: remove alias
doskey cc=

:: replace alias
cc=cd d:\