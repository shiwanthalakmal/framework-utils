@echo off

rem  ============================================
rem
rem  Used for Creating Idea Project files Command-line
rem  mode (WinNT/2K/Win7 only)
rem
rem  ============================================

echo Select a option to Clean and Create the Idea project

set install_dir="%~dp0.."

pushd %install_dir%

echo moved to %install_dir%

call mvn idea:clean idea:idea

pause
popd