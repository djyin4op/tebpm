#!/bin/bash

# JVM 环境
JAVA_HOME=${JAVA_HOME-/opt/jdk1.7.0}

jvmsettings="-server -Xms2G -Xmx2G -Xss256k -XX:PermSize=64M -XX:MaxPermSize=64M -XX:+UseG1GC -XX:-RelaxAccessControlCheck  -XX:+UseCompressedOops  -XX:+HeapDumpOnOutOfMemoryError  -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"

# 运行参数
parameters="-jar teb.jar"

$JAVA_HOME/bin/java $jvmsettings $parameters
