// run h2 server using command line
// java -cp h2-2.2.224.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -tcpPort 9092 -webPort 8082
// download h2 jar from https://www.h2database.com/html/download.html
https://github.com/h2database/h2database/releases/download/version-2.1.214/h2-2022-06-13.zip

// create a new folder using linux command

// extract zip file using linux command
unzip h2-2022-06-13.zip

// set java home using linux command
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

spring:
datasource:
#url: jdbc:h2:mem:./bookdb
url: jdbc:h2:file:./bookdb
driver-class-name: org.h2.Driver
username: sa
password:
h2:
console:
enabled: true

# Set root logging level to DEBUG
logging:
level:
root: DEBUG
# Set logging level for specific packages
org.springframework: DEBUG
com.yourpackage: DEBUG

server:
port: 28080

# Path: Note_working.md
## run h2 server
java -cp h2-2.2.224.jar org.h2.tools.Server -tcp -tcpPort 9092 -web -webPort 8082