
java -cp build\libs\etdb-all.jar org.h2.tools.Server -baseDir .\support\db -tcp -tcpAllowOthers -web -webAllowOthers

exit

REM
REM From the web console: jdbc:h2:tcp://localhost/C:/data/git-work/etdb/support/db/etdb
REM
REM Starts the H2 Console (web-) server, TCP, and PG server.
REM Usage: java org.h2.tools.Server <options>
REM When running without options, -tcp, -web, -browser and -pg are started.
REM Options are case sensitive. Supported options are:
REM [-help] or [-?]         Print the list of options
REM [-web]                  Start the web server with the H2 Console
REM [-webAllowOthers]       Allow other computers to connect - see below
REM [-webDaemon]            Use a daemon thread
REM [-webPort <port>]       The port (default: 8082)
REM [-webSSL]               Use encrypted (HTTPS) connections
REM [-browser]              Start a browser connecting to the web server
REM [-tcp]                  Start the TCP server
REM [-tcpAllowOthers]       Allow other computers to connect - see below
REM [-tcpDaemon]            Use a daemon thread
REM [-tcpPort <port>]       The port (default: 9092)
REM [-tcpSSL]               Use encrypted (SSL) connections
REM [-tcpPassword <pwd>]    The password for shutting down a TCP server
REM [-tcpShutdown "<url>"]  Stop the TCP server; example: tcp://localhost
REM [-tcpShutdownForce]     Do not wait until all connections are closed
REM [-pg]                   Start the PG server
REM [-pgAllowOthers]        Allow other computers to connect - see below
REM [-pgDaemon]             Use a daemon thread
REM [-pgPort <port>]        The port (default: 5435)
REM [-properties "<dir>"]   Server properties (default: ~, disable: null)
REM [-baseDir <dir>]        The base directory for H2 databases (all servers)
REM [-ifExists]             Only existing databases may be opened (all servers)
REM [-trace]                Print additional trace information (all servers)
REM [-key <from> <to>]      Allows to map a database name to another (all servers)
REM The options -xAllowOthers are potentially risky.
REM For details, see Advanced Topics / Protection against Remote Access.
REM See also http://h2database.com/javadoc/org/h2/tools/Server.html
