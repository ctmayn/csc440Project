import java.sql.*;
import java.util.*;

/**
 * This class is the main class of the Wolf Hospital system. It allows users to  enter, update, and delete 
 * staff and hospital information, check-in and check-out patients, reserve and release beds,
 * and generate reports.
 * 
 * @author Christopher Maynard
 * @author Marshall Skelton
 * @author Daniel Deans
 * @author Caeman Toombs
 */
public class WolfHospital{
    private static final String user = "";
    private static final String password = "";
    private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/" + user;

    private static Scanner scanner = null;

    /**
     * Starting point of the project sets up many of the objects needed in the program and
     * calls the functions that cause behvaior of the program
     */
    public static void main(String[] args){
        
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        try {
        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection(jdbcURL, user, password);
        statement = connection.createStatement();

        scanner =  new Scanner(System.in);

        setupTables(statement);

        actions(statement, result);

        } catch(Throwable oops) {
            oops.printStackTrace();
        }
        if(scanner != null){
            scanner.close();
        }
        close(result);
        close(statement);
        close(connection);
    }

    /**
     * Closes the connection for the project
     *
     * @param conn The connection
     */
    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    /**
     * Closes the statement object for the project
     * 
     * @param st The statement object
     */
    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }

    /**
     * Closes the result set for the project
     * 
     * @param rs The result set
     */
    static void close(ResultSet rs) {
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }

    /**
     * Sets up the tables in the database as well as the sequences for the database.
     * 
     * @param statement Sends sql statements to the DBMS
     * @throws SQLException
     */
    private static void setupTables(Statement statement) throws SQLException{
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS staff (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, "  +
            "name VARCHAR(128) NOT NULL, " +
            "age INT NOT NULL, " +
            "gender VARCHAR(128) NOT NULL," + 
            "job_title VARCHAR(128) NOT NULL, "  +
            "professional_title VARCHAR(128) NOT NULL, "  +
            "dept VARCHAR(128) NOT NULL, " +
            "contact_info VARCHAR(128) NOT NULL)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS doctor( "  +
            "staff_id INT PRIMARY KEY, "  +
            "specialist VARCHAR(128), "  +
            "CONSTRAINT doctor_fk FOREIGN KEY(staff_id) REFERENCES staff(id) "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS nurse( "  +
            "staff_id INT PRIMARY KEY, "  +
            "CONSTRAINT nurse_fk FOREIGN KEY(staff_id) REFERENCES staff(id)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS operator( "  +
            "staff_id INT PRIMARY KEY, "  +
            "CONSTRAINT operator_fk FOREIGN KEY(staff_id) REFERENCES staff(id) "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS patient( "  +
            "patient_id INT AUTO_INCREMENT PRIMARY KEY, "  +
            "status VARCHAR(128) NOT NULL, "  +
            "gender VARCHAR(128) NOT NULL, "  +
            "ssn INT, "  +
            "name VARCHAR(128) NOT NULL, "  +
            "DOB VARCHAR(128) NOT NULL, "  +
            "age INT NOT NULL, "  +
            "contact_info VARCHAR(128) NOT NULL)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS medical_record( "  +
            "record_num INT AUTO_INCREMENT PRIMARY KEY, "  +
            "res_doctor INT NOT NULL, "  +
            "diagnosis_details VARCHAR(128), "  +
            "prescription VARCHAR(128), "  +
            "CONSTRAINT doctor_medical_fk FOREIGN KEY(res_doctor) REFERENCES doctor(staff_id) "  +
            "ON DELETE CASCADE )");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS billing_account( "  +
            "billing_id INT AUTO_INCREMENT PRIMARY KEY, "  +
            "billing_addr VARCHAR(128) NOT NULL, "  +
            "payment_info VARCHAR(128) NOT NULL)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS ward( "  +
            "ward_num INT AUTO_INCREMENT PRIMARY KEY, "  +
            "charges_per_day INT NOT NULL, "  +
            "res_nurse INT NOT NULL, "  +
            "capacity INT NOT NULL, "  +
            "CONSTRAINT nurse_ward_fk FOREIGN KEY(res_nurse) REFERENCES nurse(staff_id)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS bed( "  +
            "bed_num INT AUTO_INCREMENT PRIMARY KEY, "  +
            "ward_num INT, "  +
            "patient_id INT, "  +
            "CONSTRAINT ward_bed_fk FOREIGN KEY(ward_num) REFERENCES ward(ward_num)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT patient_bed_fk FOREIGN KEY(patient_id) REFERENCES patient(patient_id)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS check_in_information( "  +
            "check_in_id INT AUTO_INCREMENT PRIMARY KEY, "  +
            "start_date DATE NOT NULL, "  +
            "end_date DATE, "  +
            "bed_num INT NOT NULL, "  +
            "ward_num INT NOT NULL, "  +
            "CONSTRAINT bed_check_fk FOREIGN KEY(bed_num) REFERENCES bed(bed_num)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT ward_check_fk FOREIGN KEY(ward_num) REFERENCES ward(ward_num)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS test( "  +
            "test_id INT AUTO_INCREMENT PRIMARY KEY, "  +
            "record_num INT NOT NULL, "  +
            "test_name VARCHAR(128) NOT NULL, "  +
            "specialist INT NOT NULL, "  +
            "patient_id INT NOT NULL, "  +
            "test_results VARCHAR(128) NOT NULL, "  +
            "CONSTRAINT specialist_test_fk FOREIGN KEY(specialist) REFERENCES doctor(staff_id)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT record_num_test_fk FOREIGN KEY(record_num) REFERENCES medical_record(record_num)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT patient_test_fk FOREIGN KEY(patient_id) REFERENCES patient(patient_id)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS office_visit( "  +
            "visit_num INT AUTO_INCREMENT PRIMARY KEY, "  +
            "record_num INT, "  +
            "billing_id INT, "  +
            "check_in_id INT, "  +
            "patient_id INT, "  +
            "CONSTRAINT record_num_ov_fk FOREIGN KEY(record_num) REFERENCES medical_record(record_num)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT billing_ov_fk FOREIGN KEY(billing_id) REFERENCES billing_account(billing_id)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT check_in_ov_fk FOREIGN KEY(check_in_id) REFERENCES check_in_information(check_in_id)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT patient_ov_fk FOREIGN KEY(patient_id) REFERENCES patient(patient_id)  "  +
            "ON DELETE CASCADE)");
    }

    /**
     * This method is responsible for a use's behavior. It will conintue to take requests
     * from the operator until they decide to quit out. Users can enter, update, and delete 
     * staff and hospital information, check-in and check-out patients, reserve and release beds,
     * and generate reports.
     * 
     * @param statement Sends sql statements to the DBMS 
     * @param result Records the result of an SQL statement
     */
    private static void actions(Statement statement, ResultSet result) throws SQLException{
        System.out.println("Hello and Welcome to the Wolf Hospital System!");
        while(true){
            System.out.println("Your actions are: \n<Enter> info about patients, staff, and wards." +
            "\n<Update> info about patients, staff, and wards."+
            "\n<Delete> info about patients, staff, and wards." + 
            "\n<Check-in> patients." + 
            "\n<Check-out> patients." + 
            "\n<Reserve> beds for patients." + 
            "\n<Release> reservations for beds for patients." + 
            "\nGenerate <reports>." + 
            "\n<Quit>.");
            String action = scanner.nextLine();
            if(action.toLowerCase().equals("quit")){
                return;
            } else if(action.toLowerCase().equals("enter")){
                System.out.println("Choices for entering info: Patient, Doctor, Nurse, Operator, Ward, or Bed.");
                String type = scanner.nextLine();
                if(type.toLowerCase().equals("patient")){
                    int social;
                    String status;
                    String gender;
                    String name;
                    String dob;
                    int age;
                    String contact;
                    try {
                        System.out.println("social security number: ");
                        social = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("status: ");
                        status = scanner.nextLine();
                        System.out.println("Gender");
                        gender = scanner.nextLine();
                        System.out.println("Name: ");
                        name = scanner.nextLine();
                        System.out.println("Date of Birth: ");
                        dob = scanner.nextLine();
                        System.out.println("Age:");
                        age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Contact Info: ");
                        contact = scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                        break;
                    }
                    try {
                        statement.executeUpdate("INSERT INTO patient(status, gender, ssn, name, DOB, age, contact_info) VALUES ('" + status + "', '" + gender + "', '" + social + "', '" + name + "', '" + dob + "', '" + age + "', '" + contact + "')");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    
                } else if(type.toLowerCase().equals("doctor")){
                    String name;
                    int age;
                    String gender;
                    String job;
                    String professional;
                    String department;
                    String contact;
                    String specialist;
                    try {
                        System.out.println("Name: ");
                        name = scanner.nextLine();
                        System.out.println("Age: ");
                        age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("gender: ");
                        gender = scanner.nextLine();
                        System.out.println("Job Title: ");
                        job = scanner.nextLine();
                        System.out.println("Professional Title: ");
                        professional = scanner.nextLine();
                        System.out.println("Department: ");
                        department = scanner.nextLine();
                        System.out.println("Contact Info: ");
                        contact = scanner.nextLine();
                        System.out.println("Specialist? (Yes/No): ");
                        specialist = scanner.nextLine();
                    } catch (Exception e) {
                        System.out.println("Invalid Input.");
                        break;
                    }
                    try {
			            statement.executeUpdate("START TRANSACTION;");
                        statement.executeUpdate("INSERT INTO staff(name ,age, gender, job_title, professional_title, dept, contact_info) VALUES ('" + name + "', '" + age + "', '"+ gender + "', '" + job + "', '" + professional + "', '" + department + "', '" + contact + "')");
                        result = statement.executeQuery("SELECT MAX(id) AS max_id FROM staff");
                        result.next();
                        int id = result.getInt("max_id");
                        statement.executeUpdate("INSERT INTO doctor(staff_id, specialist) VALUES ('" + id + "', '" + specialist + "')");
                    	statement.executeUpdate("COMMIT;");
                    } catch (SQLException e) {
                        statement.executeUpdate("ROLLBACK;");
                        e.printStackTrace();
                    }
                
                } else if(type.toLowerCase().equals("nurse")){
                    String name;
                    int age;
                    String gender;
                    String job;
                    String professional;
                    String department;
                    String contact;
					try {
					    System.out.println("Name: ");
                        name = scanner.nextLine();
                        System.out.println("Age: ");
                        age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("gender: ");
                        gender = scanner.nextLine();
                        System.out.println("Job Title: ");
                        job = scanner.nextLine();
                        System.out.println("Professional Title: ");
                        professional = scanner.nextLine();
                        System.out.println("Department: ");
                        department = scanner.nextLine();
                        System.out.println("Contact Info: ");
                        contact = scanner.nextLine();
					} catch (Exception e) {
						System.out.println("invalid input.");
						break;
					}
					try {
						statement.executeUpdate("START TRANSACTION;");
                        statement.executeUpdate("INSERT INTO staff(name ,age, gender, job_title, professional_title, dept, contact_info) VALUES ('" + name + "', '" + age + "', '"+ gender + "', '" + job + "', '" + professional + "', '" + department + "', '" + contact + "')");
                        result = statement.executeQuery("SELECT MAX(id) AS max_id FROM staff");
                        result.next();
                        int id = result.getInt("max_id");
						statement.executeUpdate("INSERT INTO nurse(staff_id) VALUES ( '" + id + "')");
						statement.executeUpdate("COMMIT;");
					} catch (SQLException e) {
						statement.executeUpdate("ROLLBACK;");
						e.printStackTrace();
					}
                        
                    
                } else if(type.toLowerCase().equals("operator")){
                    String name;
                    int age;
                    String gender;
                    String job;
                    String professional;
                    String department;
                    String contact;
					try {
					    System.out.println("Name: ");
                        name = scanner.nextLine();
                        System.out.println("Age: ");
                        age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("gender: ");
                        gender = scanner.nextLine();
                        System.out.println("Job Title: ");
                        job = scanner.nextLine();
                        System.out.println("Professional Title: ");
                        professional = scanner.nextLine();
                        System.out.println("Department: ");
                        department = scanner.nextLine();
                        System.out.println("Contact Info: ");
                        contact = scanner.nextLine();
					} catch (Exception e) {
						System.out.println("invalid input.");
						break;
					}
					try {
						statement.executeUpdate("INSERT INTO staff(name ,age, gender, job_title, professional_title, dept, contact_info) VALUES ('" + name + "', '" + age + "', '"+ gender + "', '" + job + "', '" + professional + "', '" + department + "', '" + contact + "')");
                        result = statement.executeQuery("SELECT MAX(id) AS max_id FROM staff");
                        result.next();
                        int id = result.getInt("max_id");
                        statement.executeUpdate("INSERT INTO operator(staff_id) VALUES ('" + id + "')");
					} catch (SQLException e) {
						e.printStackTrace();
					}
                    
                } else if(type.toLowerCase().equals("ward")){
                    int charges;
                    int nurse;
                    int capacity;
					try {
                        System.out.println("charges per day: ");
                        charges = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("responsible nurse: ");
                        nurse = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("capacity: ");
                        capacity = scanner.nextInt();
                        scanner.nextLine();
					} catch (Exception e) {
						System.out.println("Invalid input.");
						break;
					}
					try {
						statement.executeUpdate("INSERT INTO ward( charges_per_day, res_nurse, capacity) VALUES ('" + charges + "', '" + nurse + "', '" + capacity + ")");
					} catch (SQLException e) {
						e.printStackTrace();
					}
                } else if(type.toLowerCase().equals("bed")){
                    int ward;
                    int patient;
					try {
						System.out.println("Ward Numer: ");
                        ward = scanner.nextInt();
                        scanner.nextLine();
						System.out.println("Patient ID:");
                        patient = scanner.nextInt();
                        scanner.nextLine();
					} catch (Exception e) {
						System.out.println("Invalid Input.");
						break;
					}
					try {
						statement.executeUpdate("INSERT INTO bed ( ward_num, patient_id) VALUES ('" + ward + "', '" + patient + "')");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
                } else {
                    System.out.println("Not a valid object to enter information about.");
                    continue;
                }
            } else if(action.toLowerCase().equals("update")){
                System.out.println("Choices for updating info: Patient, Doctor, Nurse, Operator, Ward, or Bed.");
                String type = scanner.nextLine();
                if(type.toLowerCase().equals("patient")){
                    System.out.println("Patient ID for update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    while (true) {
                        
                        System.out.println("Options for patient update: quit, social, status, gender, name, dob, age, contact");
                        String option = scanner.nextLine();
                        if (option.equals("social")) {
                            String social = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE patient SET ssn=" + social + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("status")) {
                            String status = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE patient SET status=" + status + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("gender")) {
                            String gender = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE patient SET gender=" + gender + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("name")) {
                            String name = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE patient SET name=" + name + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("dob")) {
                            String dob = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE patient SET DOB=" + dob + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("age")) {
                            int age = scanner.nextInt();
                            scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE patient SET age=" + age + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("contact")) {
                            String contact = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE patient SET contact_info=" + contact + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("quit")) {
                            break;
                        } else {
                            System.out.println("Invalid Input. Try again");
                        }
                    }
                } else if(type.toLowerCase().equals("doctor")){
                    System.out.println("doctor/staff ID for update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    while (true) {
                        System.out.println("Options for doctor/staff update: quit, name, age, job, professional, dept, contact, specialist");
                        String option = scanner.nextLine();
                        if (option.equals("quit")) {
                            break;
                        } else if (option.equals("name")) {
                            String name = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET name='" + name + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("age")) {
                            int age = scanner.nextInt();
                            scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET age=" + age + " WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        }else if (option.equals("gender")) {
                            String gender = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE doctor SET gender='" + gender + "' WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        } else if (option.equals("job")) {
                            String job = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET job_title='" + job + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("professional")) {
                            String professional = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET professional_title='" + professional + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("dept")) {
                            String dept = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET dept='" + dept + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("contact")) {
                            String contact = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET contact_info='" + contact + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("specialist")) {
                            String specialist = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE doctor SET specialist='" + specialist + "' WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating doctor");
                                break;
                            }
                        } else {
                            System.out.println("Invalid Input.");
                        }
                    }
                
                } else if(type.toLowerCase().equals("nurse")){
                    System.out.println("nurse/staff ID for update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    while (true) {
                        System.out.println("Options for nurse/staff update: quit, name, age, job, professional, dept, contact");
                        String option = scanner.nextLine();
                        if (option.equals("quit")) {
                            break;
                        } else if (option.equals("name")) {
                            String name = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET name='" + name + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println(e);
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("age")) {
                            int age = scanner.nextInt();
                            scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET age=" + age + " WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        }else if (option.equals("gender")) {
                            String gender = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET gender='" + gender + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        } else if (option.equals("job")) {
                            String job = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET job_title='" + job + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("professional")) {
                            String professional = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET professional_title='" + professional + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("dept")) {
                            String dept = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET dept='" + dept + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("contact")) {
                            String contact = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET contact_info='" + contact + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else {
                            System.out.println("Invalid Input.");
                        }
                    }
                    
                } else if(type.toLowerCase().equals("operator")){
                    System.out.println("Operator/staff ID for update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    while (true) {
                        System.out.println("Options for Operator/staff update: quit, name, age, job, professional, dept, contact");
                        String option = scanner.nextLine();
                        if (option.equals("quit")) {
                            break;
                        } else if (option.equals("name")) {
                            String name = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET name='" + name + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("age")) {
                            int age = scanner.nextInt();
                            scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET age=" + age + " WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        }else if (option.equals("gender")) {
                            String gender = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE doctor SET gender='" + gender + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        } else if (option.equals("job")) {
                            String job = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET job_title='" + job + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("professional")) {
                            String professional = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET professional_title='" + professional + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("dept")) {
                            String dept = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET dept=" + dept + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("contact")) {
                            String contact = scanner.nextLine();
                            try {
                                statement.executeUpdate("UPDATE staff SET contact_info='" + contact + "' WHERE id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else {
                            System.out.println("Invalid Input.");
                        }
                    }
                } else if(type.toLowerCase().equals("wards")){
                    System.out.println("Ward number for update: ");
                    int ward = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Options for ward update: quit, charges, responsible, capacity");
                    String option = scanner.nextLine();
                    if (option.equals("quit")) {
                        break;
                    } else if (option.equals("charges")) {
                        int charges = scanner.nextInt();
                        scanner.nextLine();
                        try {
                                statement.executeUpdate("UPDATE ward SET charges_per_day=" + charges + " WHERE ward_num=" + ward);
                            } catch (SQLException e) {
                                System.out.println("Error updating ward");
                                break;
                            }
                    } else if (option.equals("responsible")) {
                        int responsible = scanner.nextInt();
                        scanner.nextLine();
                        try {
                                statement.executeUpdate("UPDATE ward SET res_nurse=" + responsible + " WHERE ward_num=" + ward);
                            } catch (SQLException e) {
                                System.out.println("Error updating ward");
                                break;
                            }
                    } else if (option.equals("capacity")) {
                        int capacity = scanner.nextInt();
                        scanner.nextLine();
                        try {
                                statement.executeUpdate("UPDATE ward SET capacity=" + capacity + " WHERE ward_num=" + ward);
                            } catch (SQLException e) {
                                System.out.println("Error updating ward");
                                break;
                            }
                    } else {
                        System.out.println("Invalid Input");
                    }
                } else if(type.toLowerCase().equals("bed")){
                    System.out.println("Bed number for update: ");
                    int bed = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("ward number for bed update:");
                    int ward = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Options for bed update: quit, patient");
                    String option = scanner.nextLine();
                    if (option.equals("quit")) {
                        break;
                    } else if (option.equals("patient")) {
                        int patient = scanner.nextInt();
                        scanner.nextLine();
                        try {
                                statement.executeUpdate("UPDATE bed SET patient_id=" + patient + " WHERE bed_num=" + bed + "AND WHERE ward_num=" + ward);
                            } catch (SQLException e) {
                                System.out.println("Error updating ward");
                                break;
                            }
                    } else {
                        System.out.println("Invalid information for update");
                    }
                } else {
                    System.out.println("Not a valid object to update information about.");
                    continue;
                }
            } else if(action.toLowerCase().equals("delete")){
                System.out.println("Choices for deleting info: Patient, Doctor, Nurse, Operator, Ward, or Bed.");
                String type = scanner.nextLine();
                if(type.toLowerCase().equals("patient")){
                    System.out.println("Patient id for deletion:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        statement.executeUpdate("DELETE FROM patient WHERE patient_id=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting patient.");
                    }
                    
                } else if(type.toLowerCase().equals("doctor")){
                    System.out.println("doctor id for deletion:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                     try {
                        statement.executeUpdate("DELETE FROM doctor WHERE staff_id=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting doctor.");
                    }
                    
                } else if(type.toLowerCase().equals("nurse")){
                    System.out.println("nurse id for deletion:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                     try {
                        statement.executeUpdate("DELETE FROM nurse WHERE staff_id=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting nurse.");
                    }
                } else if(type.toLowerCase().equals("operator")){
                    System.out.println("operator id for deletion:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        statement.executeUpdate("DELETE FROM operator WHERE operator_id=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting operator.");
                    }
                    
                } else if(type.toLowerCase().equals("wards")){
                    System.out.println("ward num for deletion:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        statement.executeUpdate("DELETE FROM ward WHERE ward_num=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting ward.");
                    }
                    
                } else if(type.toLowerCase().equals("bed")){
                    System.out.println("bed id for deletion:");
                    int bedid = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("ward num for deletion of bed");
                    int wardid = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        statement.executeUpdate("DELETE FROM bed WHERE bed_num=" + bedid + "AND WHERE ward_num=" + wardid);
                    } catch (SQLException e) {
                        System.out.println("Error deleting bed.");
                    }
                    
                } else {
                    System.out.println("Not a valid object to delete.");
                    continue;
                }
            } else if(action.toLowerCase().equals("check-in")){
                System.out.println("Check-in Date (YYYY-MM-DD):");
                String startdate = scanner.nextLine();
                System.out.println("bed number:");
                int bed = scanner.nextInt();
                scanner.nextLine();
                System.out.println("ward number:");
                int ward= scanner.nextInt();
                scanner.nextLine();
                System.out.println("patient id number:");
                int patient= scanner.nextInt();
                scanner.nextLine();
                try {
                    statement.executeUpdate("INSERT INTO check_in_information( start_date, end_date, bed_num, ward_num) VALUES ('" + startdate + "', NULL,'" + bed + "','" + ward + "')'");
                    result = statement.executeQuery("SELECT MAX(id) AS max_id FROM check_in_information");
                    result.next();
                    int id = result.getInt("max_id");
                    statement.executeUpdate("INSERT INTO office_visit(record_num, billing_id, check_in_id, patient_id) VALUES(NULL, NULL, " + id + ", " + patient + ")");
                } catch (SQLException e) {
                    System.out.println("Error creating check_in");
                }
                
            } else if(action.toLowerCase().equals("check-out")){
                System.out.println("Office Visit ID:");
                int id = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Check-out date (YYYY-MM-DD): ");
                String enddate = scanner.nextLine();

                System.out.println("Medical Record Info");
                System.out.println("Responsible Doctor ID:");
                int doctor_id = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Diagnosis Details:");
                String diagnosis_details = scanner.nextLine();

                System.out.println("Prescription: ");
                String prescription = scanner.nextLine();
                
                System.out.println("Billing Info");
                System.out.println("Billing Address:");
                String billing_addr = scanner.nextLine();

                System.out.println("Payment Info:");
                String payment_info = scanner.nextLine();
                try {
                    result = statement.executeQuery("SELECT check_in_id FROM office_visit WHERE visit_num = " + id);
                    result.next();
                    int check_in_id = result.getInt("check_in_id");
                    statement.executeUpdate("UPDATE check_in_information SET end_date=" + enddate + " WHERE check_in_id=" + check_in_id);
                    statement.executeUpdate("INSERT INTO medical_record( res_doctor, diagnosis_details, prescription) VALUES ('" + doctor_id + "','" + diagnosis_details + "','" + prescription + "')'");
                    statement.executeUpdate("INSERT INTO billing_account( billing_addr, payment_info) VALUES ('" + billing_addr + "','" + payment_info  + "')'");
                    result = statement.executeQuery("SELECT MAX(id) AS max_id FROM medical_record");
                    result.next();
                    int record_num = result.getInt("max_id");
                    result = statement.executeQuery("SELECT MAX(id) AS max_id FROM billing_account");
                    result.next();
                    int billing_id = result.getInt("max_id");
                    statement.executeUpdate("UPDATE office_visit SET record_num=" + record_num + ", billing_id = " + billing_id + " WHERE visit_num = " + id);

                    System.out.println("Was a test run? (Yes/No)");
                    String test = scanner.nextLine();
                    if(test.toLowerCase().startsWith("y")){
        
                        System.out.println("Test Name: ");
                        String test_name = scanner.nextLine();
        
                        System.out.println("Specialist:");
                        int specialist_id = scanner.nextInt();
                        scanner.nextLine();
        
                        System.out.println("Test Results:");
                        String test_results = scanner.nextLine();
                        
                        result = statement.executeQuery("SELECT patient_id FROM office_visit WHERE vist_num = " + id);
                        result.next();
                        int patient_id = result.getInt("patient_id");

                        statement.executeUpdate("INSERT INTO test( record_num, test_name, specialist, patient_id, test_results) VALUES ('" + record_num + "','" + test_name + "','" + specialist_id +"','" + patient_id + "','" + test_results + "')'");
                    }
                } catch (SQLException e) {
                    System.out.println("Error checking out");
                }
                

            } else if(action.toLowerCase().equals("reserve")){
                System.out.println("Patient id for reserving bed:");
                int patid = scanner.nextInt();
                scanner.nextLine();
                System.out.println("bed num for reservation:");
                int bednum = scanner.nextInt();
                scanner.nextLine();
                System.out.println("ward num for reservation");
                int wardnum = scanner.nextInt();
                scanner.nextLine();
                try {
                    statement.executeUpdate("UPDATE bed patient_id=" + patid + "WHERE bed_num =" + bednum + "AND WHERE ward_num=" + wardnum + "AND patient_id=NULL"); 
                } catch (SQLException e) {
                    System.out.println("Error reserving bed.");
                }
            } else if(action.toLowerCase().equals("release")){
                System.out.println("bed num for release:");
                int bednum = scanner.nextInt();
                scanner.nextLine();
                System.out.println("ward num for release");
                int wardnum = scanner.nextInt();
                scanner.nextLine();
                try {
                    statement.executeUpdate("UPDATE bed patient_id=NULL WHERE bed_num =" + bednum + "AND WHERE ward_num=" + wardnum); 
                } catch (SQLException e) {
                    System.out.println("Error releasing bed.");
                }
            } else if(action.toLowerCase().equals("reports")){
                System.out.println("What report? Patient <history>, <ward> status, patients per month<ppm>, ward-usage percentage<wup>, a <doctor>'s patients, <staff> info."); 
                String type = scanner.nextLine();                               
                if(type.toLowerCase().equals("history")){
                    System.out.println("What is the patient's id?");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Beginning Check-in Date (YYYY-MM-DD):");
                    String startdate = scanner.nextLine();

                    System.out.println("Ending Check-in Date (YYYY-MM-DD):");
                    String enddate = scanner.nextLine();

                    result = statement.executeQuery("SELECT medical_record.record_number, medical_record.res_doctor, medical_record.diagnosis_details, medical_record.prescription, test.test_name, test.specialist, test.test_results" +
                    "JOIN medical_record ON office_vist.record_num = medical_record.record_num " +
                    "JOIN check_in_information ON office_visit.check_in_id = check_in_information.check_in_id " +
                    "LEFT JOIN test ON medical_record.record_num = test.record_num" +
                    "WHERE office_vist.patient_id = "+ id + " && check_in_id.start_date >= " + startdate + " && check_in_id.start_date <= " + enddate + " )");
                    while (result.next()) {
                        int record_num = result.getInt("medical_record.record_number");
                        String res_doctor = result.getString("medical_record.res_doctor");
                        String diagnosis_details = result.getString("medical_record.diagnosis_details");
                        String prescription = result.getString("medical_record.prescription");
                        String test_name = result.getString("test.test_name");
                        int specialist = result.getInt("test.specialist");
                        String test_results = result.getString("test.test_results");
                        System.out.println("Record Number: " + record_num + "  Responsible Doctor" + res_doctor + "  Diagnosis Detials:" + diagnosis_details + "  Prescription: " + prescription + "  Test Name: " + test_name + "  Test Specialist: " + specialist + "  Test Results: " + test_results );
                    }
                } else if(type.toLowerCase().equals("wards")){
                    result = statement.executeQuery("SELECT * FROM bed");
                    while (result.next()) {
                        int ward_num = result.getInt("bed.ward_num");
                        int bed_num = result.getInt("bed.bed_num");
                        int patient_id = result.getInt("bed.patient_id");
                        System.out.println("Ward Number: " + ward_num + "  Bed Number: " + bed_num + "Patient ID: " + patient_id);
                    }
                } else if(type.toLowerCase().equals("ppm")){
                    System.out.println("start date:");
                    String start = scanner.nextLine();
                    System.out.println("end date:");
                    String end = scanner.nextLine();
                    try {
                        result = statement.executeQuery("SELECT COUNT(*) as patients FROM check_in_information WHERE start_date > " + start + " AND end_date < " + end);
                        System.out.println("patients per month: " + result.getInt("patients"));
                    } catch (SQLException e) {
                        System.out.println("Error getting patients per month");
                    }
                    
                } else if(type.toLowerCase().equals("wup")){
                    System.out.println("Which ward do you want the percentage usage for? <id>: ");
                    int ward = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        result = statement.executeQuery("Select Count(*) as total, capacity FROM ward WHERE patient_id != NULL");
                        while (result.next()) {
                            int total = result.getInt("total");
                            int capacity = result.getInt("ward.capacity");
                            double print = total / capacity;
                            System.out.println("Ward percentage usage for: " + ward + " is " + print);
                        }
                    } catch (SQLException e) {
                        System.out.println("Error getting percentage for that ward.");
                    }
                } else if(type.toLowerCase().equals("doctor")){
                    System.out.println("What is the doctor's staff id?");
                    int id = scanner.nextInt();
                    try {
						result = statement.executeQuery("SELECT patient_id as patient JOIN medical_record ON office_vist.record_num = medical_record.record_num " +
                    "JOIN check_in_information ON office_visit.check_in_id = check_in_information.check_in_id " +
                    "LEFT JOIN doctor ON medical_record.res_doctor = doctor.staff_id" +
                    "WHERE doctor.staff_id = " + id);
                    System.out.println("" + id + " is in charge of: ");
                    while (result.next()) {
                        int patient = result.getInt("patient");
                        System.out.println("" + patient);
                    }
                    
					} catch (SQLException e) {
						System.out.println("Error getting doctors responsible patients");
					}
                    
                } else if(type.toLowerCase().equals("staff")){
                    System.out.println("Choose a type: Doctor,  Nurse, Operator.");
                    String staff = scanner.nextLine();
                    if(staff.toLowerCase().equals("doctor")){
                        result = statement.executeQuery("SELECT staff.id, staff.name, staff.age, staff.gender, staff.job_title, staff.professional_title, staff.dept, staff.contact_info, doctor.specialist FROM doctor INNER JOIN staff ON doctor.staff_id = staff.id");
                        while (result.next()) {
                            int id = result.getInt("staff.id");
                            String name = result.getString("staff.name");
                            int age = result.getInt("staff.age");
                            String gender = result.getString("staff.gender");
                            String job_title = result.getString("staff.job_title");
                            String professional_title = result.getString("staff.professional_title");
                            String dept = result.getString("staff.dept");
                            String contact_info = result.getString("staff.contact_info");
                            String specialist = result.getString("doctor.specialist");
                            System.out.println("ID: " + id + "  Name: " + name + "  Age: " + age + "', '"+ gender + "  Job Title: " + job_title + " Professional Title: " + professional_title + "  Department: " + dept + "  Contact Info: " + contact_info + "  Specialist:  " + specialist );
                        }
                    } else if(staff.toLowerCase().equals("nurse")){
                        result = statement.executeQuery("SELECT staff.id, staff.name, staff.age, staff.gender, staff.job_title, staff.professional_title, staff.dept, staff.contact_info FROM nurse INNER JOIN staff ON nurse.staff_id = staff.id");
                        while (result.next()) {
                            int id = result.getInt("staff.id");
                            String name = result.getString("staff.name");
                            int age = result.getInt("staff.age");
                            String gender = result.getString("staff.gender");
                            String job_title = result.getString("staff.job_title");
                            String professional_title = result.getString("staff.professional_title");
                            String dept = result.getString("staff.dept");
                            String contact_info = result.getString("staff.contact_info");
                            System.out.println("ID: " + id + "  Name: " + name + "  Age: " + age + "', '"+ gender + "  Job Title: " + job_title + " Professional Title: " + professional_title + "  Department: " + dept + "  Contact Info: " + contact_info);
                        }
                    } else if(staff.toLowerCase().equals("operator")){
                        result = statement.executeQuery("SELECT staff.id, staff.name, staff.age, staff.gender, staff.job_title, staff.professional_title, staff.dept, staff.contact_info FROM operator INNER JOIN staff ON operator.staff_id = staff.id");
                        while (result.next()) {
                            int id = result.getInt("staff.id");
                            String name = result.getString("staff.name");
                            int age = result.getInt("staff.age");
                            String gender = result.getString("staff.gender");
                            String job_title = result.getString("staff.job_title");
                            String professional_title = result.getString("staff.professional_title");
                            String dept = result.getString("staff.dept");
                            String contact_info = result.getString("staff.contact_info");
                            System.out.println("ID: " + id + "  Name: " + name + "  Age: "  + age + "', '"+ gender + "  Job Title: " + job_title + " Professional Title: " + professional_title + "  Department: " + dept + "  Contact Info: " + contact_info);
                        }
                    } else {
                        System.out.println("Not a staff member.");
                        continue;
                    }
                } else {
                    System.out.println("Not a valid report");
                    continue;
                }
            } else {
                System.out.println("Not a valid operator action");
                continue;
            }
        }
    }
}
