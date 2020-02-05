package webservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import webservice.Model.User;


@SpringBootApplication
@RestController
public class Application {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://192.168.0.2:3306/FrisO";

	static final String USER = "androidApp";
	static final String PASS = "frisOAPP123";
	
	@RequestMapping("/")
	public String home() {
		return "Service Is Running";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@RequestMapping(value = "/users")
	public static ArrayList<User> getUsers() {
		Connection conn = null;
		Statement stmt = null;
		ArrayList<User> arr = new  ArrayList<User>();
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");//Testing comment
	
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
	
			stmt = conn.createStatement();
	
			String sql = "SELECT * FROM Users";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int userID= rs.getInt("id");
				String userName= rs.getString("username");
				String email = rs.getString("email");
				String password = rs.getString("password");
				arr.add(new User(userID, userName, email, password));
			}
			rs.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			} // do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
		} 
		}
		
		return arr;
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public User addUser(@RequestBody User user) throws ClassNotFoundException, SQLException{
		
		
		String username = null;
		String email = null;
		String password = null;
		
		Connection conn = null;
		Statement stmt = null;
		
		if (user != null) {
	        username = "'" + user.getUsername() + "'";
	        email = "'" + user.getEmail() + "'";
	        password = "'" + user.getPassword() + "'";
	        Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();

			String sql = "INSERT INTO Users (username, email, password) VALUES ("+ username + "," + email + "," + password + ");";
			stmt.executeUpdate(sql);
	    }
		
		//Testing comment

	    // TODO: call persistence layer to update
	    return user;
	}

}