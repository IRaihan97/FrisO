# Webservice Documenation
This section of the repository will describe the basics of the code developed for the webservice.
It will include descriptions of the commands used in the virtual machine, the spring-boot application developed and the code integrated in the front end of the application
## Initial setup of the virtual machine on cloud
The virtual machine was primarily used through the command prompt in Windows by performing a connection through a Secured Shell protocol.  
The following command was used to connect which is formed by the protocol, username and ip address of the virtual machine.
```
ssh root@172.31.82.149
```
Once logged in the machine, a docker image of MySQL has been pulled with the following command
```
docker mysql pull
```
With a docker image of mysql ready, all that was needed was to craete a docker container.  
The following command was used which is formed by the name of the container, the port in which it will run through, the password for the mysql root user and the image that will be used to base the container on
```
docker create --name SQL_DB -p 3306:3306 -e MYSQL_ROOT_PASSWORD=aPassword123 -e MYSQL_ROOT_HOST="%" -d mysql/mysql-server 
```
As the container was created, it was started by using the following command
```
docker start SQL_DB
```
Afterwards, I needed to access the container to create a database. Before doing so, I also needed to note down the IP address of the container with the following command
```
docker inspect SQL_DB
```
I then executed the MySQL service running in the container to create a database.
```
docker exec -it SQL_DB mysql -uroot -p
```
The database was then created with a simple SQL Query
```SQL
Create Database friso;
```
To make sure that the webservice will function properly, I also needed to make sure that the root user had all the priviledges from all remote location
```SQL
-- The percentage sign specifies all hosts
GRANT ALL PRIVILEGES ON friso.* TO 'root'@'%'; 
FLUSH PRIVILEGES;
```
The spring-boot webservice will automatically create tables, so I did not create them manually through SQL

## Setting Up the Webservice for Docker
I first needed to initialize a spring boot project by including the needed depndencies    
The dependencies I used were JPA (which will be described more in detail below), Web, Devtools, MySQL and Docker  
I used the following spring-boot application initializer to initialize the project
```
http://start.spring.io 
```
Here is an overview of the initialized spring-boot project  
![image](https://drive.google.com/uc?export=view&id=19-m19C5hUnq7Gn7apuw8f-3rkFMdV1J-)

Once the project was created, I have added a "dockerfile" containing the following strings of code. This will be used to compile a jar file from the project and use it to build a docker image
```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY webservicedb-1.jar webservicedb-1.jar
ENTRYPOINT ["java","-jar","/webservicedb-1.jar"]
```
The following was also added to the pom.xml file to make use of the dockerfile and build a docker image on the Virtual Machine (along with the dependencies added during the initialization of the project)  
```xml
<groupId>com.group20</groupId>
	<artifactId>webservicedb</artifactId>
	<version>1</version>
	<name>webservice</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<docker.image.prefix>frisbee</docker.image.prefix>
		<java.version>1.8</java.version>
	</properties>
  
  <build>
        <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.5.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
         <plugin>
         <groupId>com.spotify</groupId>
         <artifactId>docker-maven-plugin</artifactId>
         <version>1.0.0</version>
         
         <configuration>
            <imageName>${docker.image.prefix}/${project.artifactId}</imageName>
            <dockerDirectory>src/main/docker</dockerDirectory>
            <resources>
               <resource>
                  <directory>${project.build.directory}</directory>
                  <include>${project.build.finalName}.jar</include>
               </resource>
            </resources>
         </configuration>
      </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```
