REM ###########################
REM invoke-it.bat
REM ###########################

java -jar build\libs\etdb-all.jar create

java -jar build\libs\etdb-all.jar populate 

java -jar build\libs\etdb-all.jar dump


