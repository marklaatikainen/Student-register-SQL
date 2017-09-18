import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;


public class StudentRegister {
	  private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement pst = null;
    private ResultSet resultSet = null;
    
    

	public static void main(String[] args) {
    StudentRegister streg = new StudentRegister();
		try {
		 	streg.select();
		} catch (Exception e) {
			System.out.println("Error occured!");
		}

	}
	
	public Connection connection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String JDBCDriver = "com.mysql.jdbc.Driver";
    	Class.forName(JDBCDriver).newInstance();
    	String url = "jdbc:mysql://localhost:1234/students";
    	Connection connection = DriverManager.getConnection(url, "username","pswd");
    	return connection;
	}
	
	
	public void select() throws Exception {
		int selection = -1;
		Scanner input = new Scanner(System.in);
		do {
			System.out.println("1) Get all students");
			System.out.println("2) Find student");
			System.out.println("3) Add student");
			System.out.println("0) Exit");
			System.out.print("Select: ");
			selection = input.nextInt();
			
			switch (selection) {
			case 1:
				// Get all students
		        showAll();
		        System.out.println();
				break;
			case 2:
				// Find student by studentnumber
				findOne();
				System.out.println();
				break;
			case 3:
				// Add new student
				addNew();
				System.out.println();
				break;
			case 0:
				// Exit
				break;
			default:
				System.out.println("Wrong selection");
			}
		} while (selection != 0);
		System.out.println("Program closed");
		input.close();
	}
	
	// show all records
	private void showAll() throws SQLException, Exception {
            try {
            	Connection connection = connection();
            	pst = connection.prepareStatement("SELECT * FROM student ORDER BY studentnumber");
            	resultSet = pst.executeQuery();
            	
            	while (resultSet.next()) {
            		String studentnumber = resultSet.getString("studentnumber");
            		String firstname = resultSet.getString("firstname");
            		String lastname = resultSet.getString("lastname");
            		Date birthdate = resultSet.getDate("birthdate");
            		String address = resultSet.getString("address");
            		String index = resultSet.getString("index");
     
            		// Print all
            		System.out.println("Student ID: " + studentnumber + " Name: " + firstname + " " + lastname + " Birth date: " + birthdate + " Address: " + address + " Index: " + index);
            	}
            	
        	} catch (Exception e) {
            	throw e;
            } finally {
            	close();
            }
        }
	
	
    private void findOne() throws SQLException, Exception {
    	try {
        	Connection connection = connection();
        	Scanner input1 = new Scanner(System.in);
        	System.out.print("Enter student ID: ");
        	String stnr = input1.nextLine();

        	pst = connection.prepareStatement("SELECT * from student WHERE studentnumber = '" + stnr + "'");
        	resultSet = pst.executeQuery();
        	
			if(resultSet.next()) {
				if(resultSet.getString("oppilasnro").equals(stnr)) {
					String studentnumber = resultSet.getString("studentnumber");
            		String firstname = resultSet.getString("firstname");
            		String lastname = resultSet.getString("lastname");
            		Date birthdate = resultSet.getDate("birthdate");
            		String address = resultSet.getString("address");
            		String index = resultSet.getString("index");
            		
            		// print data
            		System.out.println("Student ID: " + studentnumber + " Name: " + firstname + " " + lastname + " Birth date: " + birthdate + " Address: " + address + " Index: " + index);
					}
			} else {
				System.out.println("Student by this ID doesn't exist");
			}
      	} catch (Exception e) {
        	throw e;
        } finally {
        	close();
        }
    }
    
    
    private void addNew() throws SQLException, Exception {
    	try {
            	Connection connection = connection();
            	Scanner input3 = new Scanner(System.in);
            	System.out.print("Enter student ID: ");
            	String stdnr = input3.nextLine();
            	
            	// checks if student number already exists
            	
            	pst = connection.prepareStatement("SELECT * from student WHERE studentnumber = '" + stdnr + "'");
            	resultSet = pst.executeQuery();
            	
				if(resultSet.next()) {
					if(resultSet.getString("studentnumber").equals(stdnr)) {
						System.out.println("Student already exists");
						}
				} else {
            	
            	System.out.print("Enter first name: ");
            	String firstname = input3.nextLine();
            	System.out.print("Enter last name: ");
            	String lastname = input3.nextLine();
            	System.out.print("Enter birth date (yyyy-MM-dd): ");
            	String birthdate = input3.nextLine();

            	SimpleDateFormat inTime = new SimpleDateFormat("yyyy-MM-dd");
            	java.util.Date date = inTime.parse(birthdate);
            	java.sql.Date sqlDate = new java.sql.Date(date.getTime()); 
          	
            	
        		System.out.print("Enter address: ");
            	String address = input3.nextLine();
            	System.out.print("Enter index: ");
            	String index = input3.nextLine();
            	
            	pst = connection.prepareStatement("INSERT INTO student (studentnumber,firstname,lastname,birthdate,address,index) VALUES(?,?,?,?,?,?)");
            	pst.setString(1, stdnr);
                pst.setString(2, firstname);
                pst.setString(3, lastname);
                pst.setDate(4, sqlDate);
                pst.setString(5, address);
                pst.setString(6, index);
                pst.executeUpdate();
                System.out.println("Student added succesfully!");
        	}
      	} catch (Exception e) {
        	throw e;
        } finally {
        	close();
        }
    }
    
    
    private void close() {
    	try {
        	if (resultSet != null) {
            	resultSet.close();
        	}

        	if (statement != null) {
            	statement.close();
        	}

        	if (connect != null) {
        		connect.close();
        	}
    	} catch (Exception e) {

    	}
    }

}
