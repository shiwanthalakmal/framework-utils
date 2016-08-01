@echo off

rem  ============================================
rem
rem  Used for running the AutoTestRunner in the Non-GUI
rem  mode (WinNT/2K/Win7 only)
rem
rem  ============================================

echo Select a option to Clean and Build the projects
echo.

set install_dir="%~dp0.."

pushd %install_dir%

echo moved to %install_dir%

call mvn clean install -DskipTests

pause
popd