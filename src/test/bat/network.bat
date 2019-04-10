@echo off
:: 显示中文
CHCP 65001

:: net accounts
net accounts

:: net config
net config

:: net computer
rem NET COMPUTER \\dxbtest /ADD

:: net user
net user
net user guest

:: net service
net stop spooler

:: net statistics
net statistics server

:: net use
net use z: \\file.thhd.local
