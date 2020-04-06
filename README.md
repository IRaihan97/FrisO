# Webservice Documentation
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
I first needed to initialize a spring boot project by including the needed dependencies.    
The dependencies I used were JPA (which will be described more in detail below), Web, Devtools, MySQL and Docker  
I used the following spring-boot application initializer to initialize the project:
```
http://start.spring.io
```
Here is an overview of the initialized spring-boot project:  
![Please refresh Until you see the picture](https://drive.google.com/uc?export=view&id=19-m19C5hUnq7Gn7apuw8f-3rkFMdV1J-)

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
![Please refresh Until you see the picture](https://drive.google.com/uc?export=view&id=1avvwf8t_TxKhbJK5Q5Sd5ejN0YfTu1in)

#### Models Examples
Thanks to the JPA and the Spring framework, the models classes are going to be mapped into SQL tables.   
Here is a simple Entity Class that will be used to create and query a table in the MySQL database that it is connected to:
```Java
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

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
import org.springframework.data.jpa.repository.JpaRepository;
import com.group20.webservice.models.Games;

public interface GamesRepo extends JpaRepository<Games, Long> {// you can see that "Games" is defined when extending from the repository
    
}
```
To perform basic queries such as deleting or adding a record, extending from the JpaRepository is enough. However, JPA still allows to perform custom SQL queries with the @Query notation:
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.group20.webservice.models.Games;

public interface GamesRepo extends JpaRepository<Games, Long> {
    @Query("SELECT g FROM Games g WHERE g.gameID = ?1")
    public Games findByGameID(Long gameID); //This method will return a single game where the gameID is equal to the passed argument
}
```

#### Controller Examples
The controller classes are the core classes that define the overall functionalities of the webservice. It makes use of the other classes defined in the other packages. The classes defined here dictate how the server should respond to possible requests by clients. The following code represents a simple controller for the Games class (which is the entity in the SQL table) defined previously:
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController //This annotation makes this class as a Controller
@RequestMapping("/api") //This class will be considered whenever there's a request at URL "http://IPADDRESSOFTHEMACHNE:8080/api". The rest of the mappings will be executed based on what parameter is added to the URL
public class GamesController {
    	@Autowired
	GamesRepo gamesRepo;//gamesRepo obj to use methods from the jpa interface

    	@GetMapping("/games")//Following method responds to GET requests when "/games" is added to the Request mapping url
	public List<Games> getAllGames() {
	    return gamesRepo.findAll();//Finds all games in the entity table Games and returns a List which is automatically converted to a JsonArray
	    
	}
    
    	@PostMapping("/games")//Following method responds to POST requests on when "/games" is added to the RequestMapping url
	public Games createGame(@Valid @RequestBody Games game) {
	    return gamesRepo.save(game);//Takes the @RequestBody Games Object and adds it to the games entity table
	    //returns the added game as a JSON object to respond back
	}
	
	@PutMapping("/games/upName/{id}")//Following method responds to PUT requests when "/games/upName/{id}" is added to the RequestMapping url
	//The {id} represents the id of the record you want to modify with this mapping. 
	//If the url had "/api/games/4" it would update record with ID 4
	public Games updateGameName(@PathVariable(value = "id") Long gamesId,
	                                        @Valid @RequestBody Games gameDetails) {

	    Games game = gamesRepo.findById(gamesId)//Finds record by ID passed in the url
	            .orElseThrow(() -> new ResourceNotFound("Games", "id", gamesId));

	    game.setName(gameDetails.getName());//Gets the queries records and updates it with the value passed when performing the request
	   

	    Games updatedGame = gamesRepo.save(game);//saves the updated game
	    return updatedGame;//returns the updated game as a JSON object to respond back
	}
    
    	@DeleteMapping("/games/{id}")//This mapping will delete a record with ID equal to the value passed in the url {id}
	public ResponseEntity<?> deleteGame(@PathVariable(value = "id") Long gamesId) {
	    Games game = gamesRepo.findById(gamesId)
	            .orElseThrow(() -> new ResourceNotFound("User", "id", gamesId));

	    gamesRepo.delete(game);

	    return ResponseEntity.ok().build();
	}  
}
```
The code above contains mappings for GET, PUT, POST and DELETE http requests. Each mapping will run the method tied to it based on the http request types made by clients. For example, if the client makes a POST http request at URL "http://172.31.82.149:8080/api/games" (the IP address defined in this URL is the IP of the VM we used), the webservice will execute the "createGame()" method which will take the object passed during the request and assign it to a new Games object. That game object is than used to add a new game to the table with gamesRepo.save(). Please have a look at the github repository for more details on the mappings made for the application.    

The whole webservice is then run with this specific class:
```java
package com.group20.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableJpaAuditing
public class WebserviceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}

}
```

The following is an example of performing a simple GET request on the webservice using POSTMAN to get all the games currently saved on the database:
![Please refresh Until you see the picture](https://drive.google.com/uc?export=view&id=1fhyafc6YePvcna9QYVIr_XVAy06lxRph)

## Running the webservice application as a docker container
Once all the webservice was completed, I needed to keep the service running as a docker container. Otherwise, running the service as a regular java application would not keep the service running constantly and it would automatically shutdown whenever I logged out from the Virtual Machine. As I have set up the Maven dependecies and the dockerfile as describe in the previous sections, all I needed to do was to compile a docker image which I did with the following command on the virtual machine:
```
mvn package docker:build
```
This then built a docker image of my webservice which I called frisbee/webservicedb. I then created a docker container with the image doing using the following command:
```
docker create --name webservice -p 8080:8080 frisbee/webservicedb
```
All I had to do at this point, was to start the MySQL container and the webservice container.

# Setting up the Front End to Consume JSON objects from the webservice
This section will describe parts of the code used to make use of the webservice and get the data from the server.

## Classes overview
I have defined two classes and one interface in the android studio project. The classes are called VolleyRequest and OnlineQueries while the interface is called IResult.    

The IResult is a simple interface that will be used to get the responses of the HTTP requests from the server:  
```java
package com.example.fris_o.tools;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IResult {
    public void ObjSuccess(String requestType, JSONObject response);
    public void ArrSuccess(String requestType, JSONArray response);
    public void notifyError(String requestType, VolleyError error);
}
```

The VolleyRequest class deals with perfoming the actual Http requests to the server by using the Volley API, which was added in gradle.  

Here is a sample code of the VolleyRequest class:
```java
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class VolleyService {

    IResult mResultCallback = null; //Interface for callbacks, allows to receive response and perform specific action based on the response
    Context mContext;

    public VolleyService(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }
    
    public void getDataArrayVolley(final String requestType, String url){

        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonArrayRequest jsonObj = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if(mResultCallback != null)
                        mResultCallback.ArrSuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            });

            queue.add(jsonObj);

        }catch(Exception e){

        }
    }
	

    public void postDataVolley(final String requestType, String url, JSONObject sendObj){

        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);//Creates a new queue for requests

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, url,sendObj, new 				Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null)
                        mResultCallback.ObjSuccess(requestType,response);//Uses the result callback to get the responses and perform specific action.
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType,error);
                }
            });


            queue.add(jsonObj);//Adds the jsonObj Requests to the request queue and executes it

        }catch(Exception e){

        }
    }
```
This sample specifically, allows to perform a POST request by initializing a JSONObjectRequest object and passing the Request Method, the URL, the JSON object we want to post and a Response Listener to get the response from the server. It also allows to perform a GET request where no json objects are passed.

The OnlineQueries class is the main class where all the methods are defined to allow the app to perform queries to the online database. The following is a sample of code to create a game and post in the server:
```java
package com.example.fris_o.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.fris_o.data.DBHandler;
import com.example.fris_o.models.Games;
import com.example.fris_o.models.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class OnlineQueries {
    private VolleyService mVolleyService;
    private IResult result;
    private Context ctx;
    private DBHandler db;

    public OnlineQueries(Context ctx, DBHandler db) {
        this.ctx = ctx;
        this.db = db;
    }
    
    public void getGames(){
       	//Assign a new result callback by calling the getGameResp() method defined below
        getGamesResp();
        mVolleyService = new VolleyService(result, ctx);//Initializing a new VolleyService by passing the new result callback and the context of the application
        mVolleyService.getDataVolleyArray("GET", "http://172.31.82.149:8080/api/games");//Executing the http request at specified url

    }
    
    public void createGame(String gamename, String password, int difficulty, double speed){
        JSONObject obj = new JSONObject();
        Random rand = new Random();
        double[] array = {0.0001, 0.0002, 0.0003, 0.0004, 0.0005};
        int i = rand.nextInt(5);
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        double locationlat = preferences.getFloat("locationlat",0) + array[i];
        double locationlon = preferences.getFloat("locationlon",0) + array[i];
	//Creating the json object to post
        try {
            obj.put("name", gamename);
            obj.put("locationlat", locationlat);
            obj.put("locationlon", locationlon);
            obj.put("password", password);
            obj.put("difficulty", difficulty);
            obj.put("speed", speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        changeUserStatus("ingame");
        createGameResp();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.postDataVolley("Post", "http:172.31.82.149:8080/api/games", obj);//Posting the Json object at Defined url
    }
        
    private void getGamesResp(){
    //Assigning a new IResult to the result field
    //This result will take the JSONArray response and save to a local SQL Database I created
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {
                db.resetGames();//resets games table
                db.addAllGames(response);//adds all games from server response
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }
    
    private void createGameResp(){
    //Assigning a new IResult, in this case is null as we don't want to do anything with the reponse from the server
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {
		

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }
```

Most of the responses received by the webservice are stored in a local SQL Database that I have added. Look at the DBHandler class in the project repositories for a more detailed look.    

Once I had all the classes ready, all that was required was to initialize a DBHandler object and an OnlineQueries Object in the activity classes in android.    

The following is an example of performing an OnlineQuery in the MapsActivity class in the OnCreate() Method:
```java
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
	Context ctx = this;
    	DBHandler db = new DBHandler(this);//Initialize a new DB handler to store reponses in the database
    	OnlineQueries query = new OnlineQueries(ctx, db);//Initialize new OnlineQueries to perform queries to the online database
	
	protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_maps);
		
		query.getGames();//This will execute the HTTP request to get all the games from the webservice.
		//The list of games is stored in the local SQLLite Database
		
		query.createGame("gamename", "apassword", 1, 12//This will add a game to the online database
			
	}
}
```
I have made the online queries class to make the life of my other team members easier. All they need to do is to run the method from the an OnlineQueries object where all the method to perform requests are defined. Android Studio makes life even easier as it shows all the methods that a can accessed from a given object as follows:

![Please refresh until you see the picture](https://drive.google.com/uc?export=view&id=1JIIYHPrk4DDYdtM5Ym_URyqsf2ManC_o)
## Testing the webservice
To test the webservice, I installed a local MySQL Service along with MySQL Workbench:
![Please refresh Until you see the picture](https://drive.google.com/uc?export=view&id=1PhS2kDVFasziOGxwL9qP_aNk2RFWYCC8)

I have connected the webservice with the local database by changing the application.properties file. Here I passed the right url to connect to the webservice:
```
## Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.url = jdbc:mysql://localhost:3306/friso?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username = root
spring.datasource.password = racoon97


## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect

# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update
```
The webservice was then tested by performing requests with POSTMAN at "http://localhost:8080":
## Testing the front-end
To test the front end, I have made a separate activity called test. This activity contained different buttons with onClick listeners:
![Please refresh Until you see the picture](https://drive.google.com/uc?export=view&id=1Ebe17aIbI1dpa-IAVNoGNjVKQVxJThsm)

Here, I will show you how I tested the updateCurrentGame method defined in the OnlineQueries class. I have made an OnlineQuery object inside the tests class and I execute the updateCurrentGame in the onClickListener of the "Update" button:
```java
public class Tests extends AppCompatActivity {
	VolleyService mVolleyService;
    	IResult result;
    	Context ctx = this;
	private Button upBtn;
    	DBHandler db = new DBHandler(this);
    	OnlineQueries queries = new OnlineQueries(ctx, db);
    	@Override
    	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        upBtn = findViewById(R.id.update);
	upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queries.updateCurrentGame();
            }
        });
	}
}
    
```

Here is an overview of the updateCurrentGame method in the OnlineQueries class along with the callback method to get the responses:
```java
public class OnlineQueries {
    private VolleyService mVolleyService;
    private IResult result;
    private Context ctx;
    private DBHandler db;

    public OnlineQueries(Context ctx, DBHandler db) {
        this.ctx = ctx;
        this.db = db;
    }
    
    public void updateCurrentGame(){
        SharedPreferences preferences = ctx.getSharedPreferences("User_status", 0);
        long gameID = preferences.getLong("gameID", 1);
        updateGameResp();//calling method for callback
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.getDataVolley("GET", "http://172.31.82.149:8080/api/games/"+ String.valueOf(gameID));
    }
    
    private void updateGameResp(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {
                db.resetGames();
                try {
                    Log.d("Response", "Testing updateCurrentGame: " + response.getString("name"));//logs to check response
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                db.addGame(response);
                Games game = db.getGame(22);
                Log.d("Response", "Testing if game is added to the local db: " + game.getDestlat());

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }
 ```

As you saw above, there are LOGs in the callback methods. These were used to see if the app was getting any responses from the server. Now if we click on the "Update" Button, this is the result shown in the log:
![Please refresh Until you see the picture](https://drive.google.com/uc?export=view&id=1UXq1rsSYHNIXwhpL3RxhORbZTQyVZBWn)
# Conclusion
You have seen how the development of the webservice has been dealt with. Most of the code example provided here are representative of small part of how the webservice has been developed overall and there is much more involved in the code committed in this repository. Please have a look at the repository, with the explanation given here, it should be slightly easier to understand how it works. 




