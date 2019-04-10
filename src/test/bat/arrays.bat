@echo off
:: 显示中文
CHCP 65001

:: creat array
:: option 1
set array1[0]=1
:: option 2
set list=1 2 3 4
(for %%a in (%list%) do (
   echo %%a
))

:: access element
set list1[0]=1
echo list10 %list1[0]%

:: iterating over an array
setlocal enableDelayedExpansion
set topic[0]=comments
set topic[1]=variables
set topic[2]=Arrays
set topic[3]=Decision making
set topic[4]=Time and date
set topic[5]=Operators
set topic[6]=Rem
for /l %%n in (0,2,6) do (
    :: echo index %%n
    echo !topic[%%n]!
)

:: length of an array
set Arr[0]=1
set Arr[1]=2
set Arr[2]=3
set Arr[3]=4
set "x=0"

:SymLoop
if defined Arr[%x%] (
    call echo %%Arr[%x%]%%
    set /a "x+=1"
    GOTO :SymLoop
)
echo "The length of the array is" %x%

:: creating structures in arrays
:: obj[name,id]
set len=3
set obj[0].Name=Joe
set obj[0].ID=1
set obj[1].Name=Mark
set obj[1].ID=2
set obj[2].Name=Mohan
set obj[2].ID=3
set i=0
:loop

if %i% equ %len% goto :eof
set cur.Name=
set cur.ID=

for /f "usebackq delims==.tokens=1-3" %%j in (`set obj[%i%]`) do (
   set cur.%%k=%%l
)
echo Name=%cur.Name%
echo Value=%cur.ID%
set /a i=%i%+1
goto loop