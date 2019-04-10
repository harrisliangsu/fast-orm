@echo off
:: 显示中文
CHCP 65001

:: write current position dir to txt
dir > bat_dir.txt

:: stderr to file
dir fdafasf 2> bat_dir.txt

:: discard output
dir fadfaf >NUL

:: input to file
type con > list.txt