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


# todo
Enhance security by enabale security in every service.