@echo off
IF "%JAVA_HOME%"=="" (set JAVA_HOME=D:/java/jdk1.7.0)

set jvmsettings=-XX:+HeapDumpOnOutOfMemoryError -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8

set parameters=-jar teb.jar

setlocal enabledelayedexpansion

set cmdable=%JAVA_HOME%/bin/java %jvmsettings% %parameters%
echo %cmdable%
echo "-----------------------------------------------------------------"

%cmdable%

pause
endlocal