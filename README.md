# Webservice Documenation
This section of the repository will describe the basics of the code developed for the webservice.
It will include descriptions of the commands used in the virtual machine, the spring-boot application developed and the code integrated in the front end of the application
## Initial setup of the virtual machine on cloud
The virtual machine was primarily used through the command prompt in Windows by performing a connection through a Secure Shell protocol.  
The following command was used to connect which is formed by the protocol, username and ip address of the virtual machine:
```
ssh root@172.31.82.149
```
Once logged in the machine, a docker image of MySQL has been pulled with the following command:
```
docker mysql pull
```
With a docker image of mysql ready, all that was needed was to create a docker container.  
The following command was used which is formed by the name of the container, the port in which it will run through, the password for the mysql root user and the image that will be used to base the container on:
```
docker create --name SQL_DB -p 3306:3306 -e MYSQL_ROOT_PASSWORD=aPassword123 -e MYSQL_ROOT_HOST="%" -d mysql/mysql-server
```
As the container was created, it was started by using the following command:
```
docker start SQL_DB
```
Afterwards, I needed to access the container to create a database. Before doing so, I also needed to note down the IP address of the container with the following command:
```
docker inspect SQL_DB
```
I then executed the MySQL service running in the container to create a database:
```
docker exec -it SQL_DB mysql -uroot -p
```
The database was then created with a simple SQL Query:
```SQL
Create Database friso;
```
To make sure that the webservice will function properly, I also needed to make sure that the root user had all the privileges from all remote location:
```SQL
-- The percentage sign specifies all hosts
GRANT ALL PRIVILEGES ON friso.* TO 'root'@'%';
FLUSH PRIVILEGES;
```
The spring-boot webservice will automatically create tables, so I did not create them manually through SQL

## Setting Up the Webservice for Docker
I first needed to initialize a spring boot project by including the needed depndencies    
The dependencies I used were JPA (which will be described more in detail below), Web, Devtools, MySQL and Docker  
I used the following spring-boot application initializer to initialize the project:
```
http://start.spring.io
```
Here is an overview of the initialized spring-boot project:  
![image](https://drive.google.com/uc?export=view&id=19-m19C5hUnq7Gn7apuw8f-3rkFMdV1J-)

Once the project was created, I have added a "dockerfile" containing the following strings of code. This will be used to compile a jar file from the project and use it to build a docker image:
```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY webservicedb-1.jar webservicedb-1.jar
ENTRYPOINT ["java","-jar","/webservicedb-1.jar"]
```
The following was also added to the pom.xml file to make use of the dockerfile and build a docker image on the Virtual Machine (along with the dependencies added during the initialization of the project):  
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

## Insights on JPA and the code developed
JPA (Java persistence API) is a set of interfaces that is usually used with frameworks like spring-boot. It is an industry standard for Object-Relational-mapping which refers to the ability of using the object oriented programming paradigm to perform queries rather instead of using raw SQL queries.

### Connecting to the MySQL database
Spring and JPA allows to define a point of connection to the database into a single file called "application.properties" created inside the project.


Below is the code I have used to define the connection:
```
## Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.url = jdbc:mysql://192.168.0.2:3306/friso?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username = root
spring.datasource.password = aPassword123


## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update
```
The application.properties file defines a datasource for the whole webservice application which means that the point of connection is defined only once. In the example above, the url defined for the datasource is composed by the IP address of the docker container running the MySQL as well as the port that it is running through. It also requires the name of the database by also defining some parameters for accessing the database such as allowing public key retrievals.

### Project Structure
The project was primarily divided into three main packages where each package contains a specific set of classes. The most relevant packages are the models, repositories and controller packages. The classes defined inside those packages dictate the overall functionality of the webservice and they have been implemented by using JPA:  
![image](https://drive.google.com/uc?export=view&id=1avvwf8t_TxKhbJK5Q5Sd5ejN0YfTu1in)

#### Models Examples
Thanks to the JPA and the Spring framework, the models classes are going to be mapped into SQL tables.   
Here is a simple Entity Class that will be used to create and query a table in the MySQL database that it is connected to:
```Java
@Entity//the @Entity annotation specifies that this class is an entity
@Table(name = "games")//@Table annotation ties a table name to the entity, this name will be used to create the table in the database
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Games {
    @Id//@Id will make the following field to a primary key in the table
        @GeneratedValue(strategy = GenerationType.IDENTITY)//@GeneratedValue will set the field to auto increment just like in SQL
        private Long gameID;

        private String name;
    
        public Long getGameID() {
        return gameID;
        }
        
    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
}
```
All the tables in the webservice application are defined in a similar fashion. The example class provided above will generate a table with two fields named "gameID" and "name". The "gameID" field will be a BIGINT Primary key field in the table and the "name" field will be a simple Varchar(255) Field. Have a look at the repository to have a better look at the models I have created for the database.

#### Repositories Examples
The repository package contains interfaces for each model that were defined in the application. Each of these interfaces are extending from the JpaRepository<> interface which contains all the methods that are going to perform queries.  When extending from the JpaRepository, it is required to pass the model representing the entity that will be queried in the database.  
Here is an example of a repository to query the games model we previously created:
```java
public interface GamesRepo extends JpaRepository<Games, Long> {// you can see that "Games" is defined when extending from the repository
    
}
```
To perform basic queries such as deleting or adding a record, extending from the JpaRepository is enough. However, JPA still allows to perform custom SQL queries with the @Query notation:
```java
public interface GamesRepo extends JpaRepository<Games, Long> {
    @Query("SELECT g FROM Games g WHERE g.gameID = ?1")
    public Games findByGameID(Long gameID); //This method will return a single game where the ID is equal to the argument passed
}
```

#### Controller Examples



