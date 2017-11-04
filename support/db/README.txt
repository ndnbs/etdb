Home of any H2 DBs used

#####
# Can use the H2 "Shell" like this.
#####
C:\data\git-work\etdb>java -cp build\libs\etdb-all.jar org.h2.tools.Shell

NOTE: A remote connection may look something like this:

   jdbc:h2:tcp://192.168.14.184:9092/C:/data/git-work/etdb/support/db/etdb
	
Welcome to H2 Shell 1.4.196 (2017-06-10)
Exit with Ctrl+C
[Enter]   jdbc:h2:./support/db/etdb
URL
[Enter]   org.h2.Driver
Driver
[Enter]   sa
User
[Enter]   Hide
Password
Password
Connected
Commands are case insensitive; SQL statements end with ';'
help or ?      Display this help
list           Toggle result list / stack trace mode
maxwidth       Set maximum column width (default is 100)
autocommit     Enable or disable autocommit
history        Show the last 20 statements
quit or exit   Close the connection and exit

sql> show columns from os;
FIELD      | TYPE          | NULL | KEY | DEFAULT
ID         | INTEGER(10)   | NO   | PRI | NULL
........
(20 rows, 88 ms)
sql>


#####
# Can use the H2 "Server" like this. Notice, with the all-inclusive JAR, the -cp switches work out nicely.
#####
C:\data\git-work\etdb>java -cp build\libs\etdb-all.jar org.h2.tools.Server -web -browser
Web Console server running at http://192.168.14.104:8082 (only local connections)


C:\data\git-work\etdb>java -cp build\libs\etdb-all.jar org.h2.tools.Server -help
Starts the H2 Console (web-) server, TCP, and PG server.
Usage: java org.h2.tools.Server <options>
When running without options, -tcp, -web, -browser and -pg are started.
Options are case sensitive. Supported options are:
[-help] or [-?]         Print the list of options
[-web]                  Start the web server with the H2 Console
[-webAllowOthers]       Allow other computers to connect - see below
[-webDaemon]            Use a daemon thread
[-webPort <port>]       The port (default: 8082)
[-webSSL]               Use encrypted (HTTPS) connections
[-browser]              Start a browser connecting to the web server
[-tcp]                  Start the TCP server
[-tcpAllowOthers]       Allow other computers to connect - see below
[-tcpDaemon]            Use a daemon thread
[-tcpPort <port>]       The port (default: 9092)
[-tcpSSL]               Use encrypted (SSL) connections
[-tcpPassword <pwd>]    The password for shutting down a TCP server
[-tcpShutdown "<url>"]  Stop the TCP server; example: tcp://localhost
[-tcpShutdownForce]     Do not wait until all connections are closed
[-pg]                   Start the PG server
[-pgAllowOthers]        Allow other computers to connect - see below
[-pgDaemon]             Use a daemon thread
[-pgPort <port>]        The port (default: 5435)
[-properties "<dir>"]   Server properties (default: ~, disable: null)
[-baseDir <dir>]        The base directory for H2 databases (all servers)
[-ifExists]             Only existing databases may be opened (all servers)
[-trace]                Print additional trace information (all servers)
[-key <from> <to>]      Allows to map a database name to another (all servers)
The options -xAllowOthers are potentially risky.
For details, see Advanced Topics / Protection against Remote Access.
See also http://h2database.com/javadoc/org/h2/tools/Server.html


