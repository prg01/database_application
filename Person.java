package registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; //to accept input from user

public class Person {
	
	
	static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String URL = "jdbc:mysql://localhost:3306";
	static final String URL1 = "jdbc:mysql://localhost:3306/CANDIDATE";
	static final String USERNAME = "root";
	static final String PASSWORD = "jh*Ds5$kna";
	public static Connection connect;
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) throws Exception
	{
		getConnection();
		createDB();
		getConnection1();
		deleteTables();
		createTables();
		int value = 0;
		do {
		System.out.println("Select the action: \n"
				+ "1.	Add Person \n"
				+ "2.	Edit Person \n"
				+ "3.	Delete Person \n"
				+ "4.	Add Address to person \n"
				+ "5.	Edit Address \n"
				+ "6.	Delete Address \n"
				+ "7.	Count Number of Persons\n"
				+ "8.	List Persons\n"
			    + "9.   Exit");
		
		value = sc.nextInt(); 
		switch (value) {
		case 1:
			System.out.println("Enter User Id: ");
			int id = sc.nextInt();
			System.out.println("Enter First Name: ");
			String fname = sc.next();
			System.out.println("Enter Last Name: ");
			String lname = sc.next();
			addPerson(id,fname,lname);
			break;
		case 2:
			System.out.println("Enter User Id: ");
			id = sc.nextInt();
			System.out.println("Enter First Name: ");
			fname = sc.next();
			System.out.println("Enter Last Name: ");
			lname = sc.next();
			updatePerson(id,fname,lname);
			
			break;
		case 3:
			System.out.println("Enter User Id: ");
			id = sc.nextInt();
			delPerson(id);
			break;
		case 4:
			System.out.println("Enter User Id: ");
			id = sc.nextInt();
			System.out.println("Enter street: ");
			String street = sc.nextLine();
			System.out.println("Enter city: ");
			String city = sc.nextLine();
			System.out.println("Enter state: ");
			String state = sc.nextLine();
			System.out.println("Enter Postal Code: ");
			int postal_code = sc.nextInt();
			addAddress(id,street,city,state,postal_code);
			break;
		case 5:
			System.out.println("Enter User Id: ");
			id = sc.nextInt();
			updateAddress(id);
			break;
		case 6:
			System.out.println("Enter User Id: ");
			id = sc.nextInt();
			delAddress(id);
			break;
		case 7:
			countPerson();
			break;
		case 8:
			listPerson();
			break;
		default:
			System.out.println("Select the action: \n"
					+ "1.	Add Person \n"
					+ "2.	Edit Person \n"
					+ "3.	Delete Person \n"
					+ "4.	Add Address to person \n"
					+ "5.	Edit Address \n"
					+ "6.	Delete Address \n"
					+ "7.	Count Number of Persons\n"
					+ "8.	List Persons\n");
			break;
		}
		}while (value != 9);
		
	}


//establish connection with the database. This establishes connection prior to Database creation
public static Connection getConnection() throws Exception{
	try {
		Class.forName(DRIVER);
		
		connect = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		System.out.println("connected");
		return connect;
	} catch(Exception e)
	{
		System.out.println((e));
	}
	return null;
}

// To connect to Database. The previous connection is when there is no database.
public static Connection getConnection1() throws Exception{
	try {
		Class.forName(DRIVER);
		
		connect = DriverManager.getConnection(URL1, USERNAME, PASSWORD);
		System.out.println("connected");
		return connect;
	} catch(Exception e)
	{
		System.out.println((e));
	}
	return null;
}

//create database
public static void createDB() throws Exception{
	try {
		Statement stm1 = connect.createStatement();
		{		      
       String sql = "CREATE DATABASE CANDIDATE";
       stm1.executeUpdate(sql);
       System.out.println("Database created successfully");
		} 
		}
		catch (SQLException e) {
       e.printStackTrace();
	}
}

//create tables
//creating two tables, 
//first table (REGISTER table): id, first name and last name
//second table (ADDRESS table): id and address (address is is referenced from the register table
public static void createTables() throws Exception{
	try {
	     Statement stm2 = connect.createStatement();
	    {		
	    	  //creating REGISTER table
	          String sql = "CREATE TABLE PERSON " +
	                   "(User_ID int not NULL, " +
	                   " First_Name VARCHAR(255), " + 
	                   " Last_Name VARCHAR(255), " + 
	                   " PRIMARY KEY ( User_ID )) "; 
	         stm2.executeUpdate(sql);
	         System.out.println("Created PERSON table in database");
	         
	         //creating ADDRESS table
	         //add_id is address id
	         sql = "CREATE TABLE ADDRESS " +
	                   "(add_id int not NULL AUTO_INCREMENT, " + 
	        		 // AUTO INCREMENT is used to auto assign and increment address id for every new entry.
	                 // The add_id starts with 1 in this case and increments by 1. 
	        		   " User_ID int not NULL, "  +
	                   " street VARCHAR(255), "  +
	        		   " city VARCHAR(255), "   +
	                   " state VARCHAR(255), "  +
	        		   " postal_code int(6), "  +
	                   " FOREIGN KEY (User_ID) REFERENCES PERSON(User_ID)," +
	                   " PRIMARY KEY ( add_id )) "; 
	         stm2.executeUpdate(sql);
	         System.out.println("Created ADDRESS table in database");
	         
	      }} catch (SQLException e) {
	         e.printStackTrace();
	      } 
	   }

//Delete table
public static void deleteTables() throws Exception{
	try {
	     Statement stm2 = connect.createStatement();
	    {
	    	String sql = "DROP TABLE ADDRESS";
	         stm2.executeUpdate(sql);
	         System.out.println("ADDRESS Table deleted in given database...");
	         
	         sql = "DROP TABLE PERSON";
	         stm2.executeUpdate(sql);
	         System.out.println("PERSON Table deleted in given database...");
	         
	    }} catch (SQLException e) {
	         e.printStackTrace();
	      } 
	   }

//add entry to the PERSON table
public static void addPerson(int id, String fname, String lname) throws Exception{
	try {
		Statement stm2 = connect.createStatement();
		{
		// Checking if the user id is already present to avoid duplicate.
		// Counting the number of entries with the id to be inserted.
		ResultSet rs = stm2.executeQuery("select count(*) from PERSON where User_ID ="+id);
		rs.next();
		// If id is repeated rs.getInt("count(*)") will return non zero value.
		// Add entry only if rs.getInt("count(*)") returns 0.
		if (rs.getInt("count(*)") == 0) {	
	   //entry in PERSON table
	   String sql = "INSERT IGNORE INTO PERSON(User_ID,First_Name,Last_Name)values (?,?,?)";
	   PreparedStatement stm3 = connect.prepareStatement(sql);
	   stm3.setInt(1, id);
	   stm3.setString(2, fname);
	   stm3.setString(3, lname);
       stm3.executeUpdate();
		}
		else {
			System.out.println("User ID already exists. Enter a unique user id");
		}
		}
	   
	 }
		catch (SQLException e) {
       e.printStackTrace();
	}
}

// Delete an entry from person table
public static void delPerson(int id) throws Exception{
	try {
		//Statement stm2 = connect.createStatement();
		{
	   //delete entry in PERSON table
	   String sql = "DELETE FROM PERSON WHERE User_ID = ?";
	   PreparedStatement stm3 = connect.prepareStatement(sql);
	   stm3.setInt(1, id);
       stm3.executeUpdate();
		} 
		}
		catch (SQLException e) {
       e.printStackTrace();
	}
}

// Function to list each person in the table
public static void listPerson() throws Exception{
	try {
		Statement stm2 = connect.createStatement();
		{
			String q="Select * from PERSON";

			//to execute query
			ResultSet rs=stm2.executeQuery(q);

			//to print the resultset on console
			if(rs.next()){ 
				do{
					System.out.println("User ID = " + rs.getString(1)+","+" First Name = " + rs.getString(2)+","+ " Last Name = " + rs.getString(3));
				}while(rs.next());
			}
			else{
				System.out.println("Record Not Found...");
			}
	   }
	   }
		catch (SQLException e) {
       e.printStackTrace();
	}
}

// Function to count total number of person entries
public static void countPerson() throws Exception{
	try {
		Statement stm2 = connect.createStatement();
		{
			//to execute query
			ResultSet rs = stm2.executeQuery("select count(*) from PERSON");
		      rs.next();
		      //Moving the cursor to the last row
		      System.out.println("Table contains "+rs.getInt("count(*)")+" persons");
	   }
	   }
		catch (SQLException e) {
       e.printStackTrace();
	}
}

//add entry to the Address table
public static void addAddress(int id, String street, String city, String state, int postal_code) throws Exception{
	try {
		Statement stm2 = connect.createStatement();
		{
			ResultSet rs = stm2.executeQuery("select count(*) from PERSON where User_ID ="+id);
			rs.next();
		if (rs.getInt("count(*)") != 0) {
	   //entry in Address table
	   String sql = "INSERT INTO ADDRESS(User_ID,street,city,state,postal_code)values (?,?,?,?,?)";
	   PreparedStatement stm3 = connect.prepareStatement(sql);
	   stm3.setInt(1, id);
	   stm3.setString(2, street);
	   stm3.setString(3, city);
	   stm3.setString(4, state);
	   stm3.setInt(5, postal_code);
       stm3.executeUpdate();
		}
		else {
			System.out.println("Person not present. Cannot add address");
		}
	   }
	   }
		catch (SQLException e) {
     e.printStackTrace();
	}
}

// Count Total address for given Person. This Function will be useful for update address function.
// As the user can chose which address it wants to update.
public static void countAddress(int id) throws Exception{
	System.out.println("Enter count addresses");
	try {
		Statement stm2 = connect.createStatement();
		{
			//to execute query
			ResultSet rs = stm2.executeQuery("select * from ADDRESS WHERE User_ID =" + id);
		      rs.next();
		      //Moving the cursor to the last row
		      //count = rs.getInt("count(*)");
		      System.out.println("Table contains "+rs.getInt("count(*)")+" addresses");
	   }
	   }
		catch (SQLException e) {
       e.printStackTrace();
	}
	System.out.println("Exit count addresses");
	//return count;
}

//List all address for given Person. This Function will be useful for update address function.
//As the user can chose which address it wants to update.
public static int listAddress(int id) throws Exception{
	int no_add=0;
	try {
		Statement stm2 = connect.createStatement();
		{
			//to execute query
			ResultSet rs = stm2.executeQuery("select * from ADDRESS WHERE User_ID =" + id);
		      //rs.next();
		      //Moving the cursor to the last row
		      //System.out.println("Table contains "+rs.getInt("count(*)")+" addresses");
		      if(rs.next()){ 
					do{
						System.out.println("Addr"+ rs.getString(1) +" User ID = " + rs.getString(2)+","+" street = " + rs.getString(3)+","+ " city = " + rs.getString(4)+"," + " state = " + rs.getString(5)+","+ " postal code = " + rs.getString(6));
					}while(rs.next());
				}
				else{
					System.out.println("Record Not Found...");
					no_add = 1;
				}
	   }
	   }
		catch (SQLException e) {
       e.printStackTrace();
	}
	return no_add;
}

// Delete all address associated with a user ID.
public static void delAddress(int id) throws Exception{
	try {
		//Statement stm2 = connect.createStatement();
		{
	   //delete entry in PERSON table
	   String sql = "DELETE FROM ADDRESS WHERE User_ID = ?";
	   PreparedStatement stm3 = connect.prepareStatement(sql);
	   stm3.setInt(1, id);
       stm3.executeUpdate();
		} 
		}
		catch (SQLException e) {
       e.printStackTrace();
	}
}


//To Update first name, last name of the PERSON table
public static void updatePerson(int id, String fname, String lname) throws Exception{
	try {
		Statement stm2 = connect.createStatement();
		{
		// Checking if the user id is present. If not we cannot update.
		// Counting the number of entries with the id to be inserted.
		ResultSet rs = stm2.executeQuery("select count(*) from PERSON where User_ID ="+id);
		rs.next();
		// If id is present rs.getInt("count(*)") will return non zero value.
		// update entry only if rs.getInt("count(*)") returns non 0.
		if (rs.getInt("count(*)") != 0) {	
	   //entry in PERSON table
	   String sql = "UPDATE PERSON SET First_Name=?, Last_Name=? where User_ID ="+id;
	   PreparedStatement stm3 = connect.prepareStatement(sql);
	   stm3.setString(1, fname);
	   stm3.setString(2, lname);
     stm3.executeUpdate();
		}
		else {
			System.out.println("User ID does not exists. Enter a valid user id");
		}
		}
	   
	 }
		catch (SQLException e) {
     e.printStackTrace();
	}
}

// Update address for a given USER ID
//add entry to the Address table
public static void updateAddress(int id) throws Exception{
	try {
		Statement stm2 = connect.createStatement();
		{
			// Checking if the USER ID exists
			ResultSet rs = stm2.executeQuery("select count(*) from PERSON where User_ID ="+id);
			rs.next();
			// If rs.getInt("count(*)") returns non zero value, the USER ID exists.
		if (rs.getInt("count(*)") != 0) {
			// We will check if address exists for this user ID.
				int no_add = listAddress(id);
				if (no_add == 0) {
				System.out.println("Select addr to be updated from above list");
				System.out.println("Enter Address ID: ");
				int add_id = sc.nextInt();
				System.out.println("Enter street: ");
				String street = sc.nextLine();
				System.out.println("Enter city: ");
				String city = sc.nextLine();
				System.out.println("Enter state: ");
				String state = sc.nextLine();
				System.out.println("Enter Postal Code: ");
				int postal_code = sc.nextInt();
				//entry in Address table
				//String sql = "INSERT INTO ADDRESS(User_ID,street,city,state,postal_code)values (?,?,?,?,?)";
				String sql = "UPDATE ADDRESS SET street=?, city=?, state=?, postal_code=? where add_id ="+add_id;
				PreparedStatement stm3 = connect.prepareStatement(sql);
				stm3.setString(1, street);
				stm3.setString(2, city);
				stm3.setString(3, state);
				stm3.setInt(4, postal_code);
			    stm3.executeUpdate();
				}
				else {
					System.out.println("No Address present. Cannot update address");
				}
		}
		else {
			System.out.println("Person not present. Cannot update address");
		}
	   }
	   }
		catch (SQLException e) {
   e.printStackTrace();
	}
}



}

