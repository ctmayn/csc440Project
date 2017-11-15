import java.sql.*;
import java.util.*;


public class WolfHospital{
    private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/ctmaynar";
    private static final String user = "ctmaynar";
    private static final String password = "y69o53";

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
            "start_date VARCHAR(128) NOT NULL, "  +
            "end_date VARCHAR(128), "  +
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

    private static void actions(Statement statement, ResultSet result){
        System.out.println("Hello and Welcome to the Wolf Hospital System!");
        while(true){
            System.out.println("Your actions are: \n<Enter> info about patients, staff, and wards." +
            "\n<Update> info about patients, staff, and wards."+
            "\n<Delete> info about patients, staff, and wards." + 
            "\n<Check-in> patients." + 
            "\n<Check-out> patients." + 
            "\n<Reserve> beds for patients." + 
            "\nRelease reservations for beds for patients." + 
            "\nGerneate <reports>." + 
            "\n<Quit>.");
            String action = scanner.next();
            if(action.toLowerCase().equals("quit")){
                return;
            } else if(action.toLowerCase().equals("enter")){
                System.out.println("Choices for entering info: Patient, Doctor, Nurse, Operator, Ward, or Bed.");
                String type = scanner.next();
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
                        social = Integer.parseInt(scanner.next());
                        System.out.println("status: ");
                        status = scanner.next();
                        System.out.println("Gender");
                        gender = scanner.next();
                        System.out.println("Name: ");
                        name = scanner.next();
                        System.out.println("Date of Birth: ");
                        dob = scanner.next();
                        System.out.println("Age:");
                        age = Integer.parseInt(scanner.next());
                        System.out.println("Contact Info: ");
                        contact = scanner.next();
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
                        name = scanner.next();
                        System.out.println("Age: ");
                        age = Integer.parseInt(scanner.next());
                        System.out.println("gender: ");
                        gender = scanner.next();
                        System.out.println("Job Title: ");
                        job = scanner.next();
                        System.out.println("Professional Title: ");
                        professional = scanner.next();
                        System.out.println("Department: ");
                        department = scanner.next();
                        System.out.println("Contact Info: ");
                        contact = scanner.next();
                        System.out.println("Specialist: ");
                        specialist = scanner.next();
                        
                    } catch (Exception e) {
                        System.out.println("Invalid Input.");
                        break;
                    }
                    try {
                        statement.executeUpdate("INSERT INTO staff(name ,age, job_title, professional_title, dept, contact_info) VALUES ('" + name + "', '" + age + "', '" + job + "', '" + professional + "', '" + department + "', '" + contact + "')");
                        result = statement.executeQuery("SELECT MAX(id) AS max_id FROM staff");
                        result.next();
                        int id = result.getInt("max_id");
                        statement.executeUpdate("INSERT INTO doctor(staff_id, specialist) VALUES ('" + id + "', '" + specialist + "')");
                    } catch (SQLException e) {
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
                        name = scanner.next();
                        System.out.println("Age: ");
                        age = Integer.parseInt(scanner.next());
                        System.out.println("gender: ");
                        gender = scanner.next();
                        System.out.println("Job Title: ");
                        job = scanner.next();
                        System.out.println("Professional Title: ");
                        professional = scanner.next();
                        System.out.println("Department: ");
                        department = scanner.next();
                        System.out.println("Contact Info: ");
                        contact = scanner.next();
					} catch (Exception e) {
						System.out.println("invalid input.");
						break;
					}
					try {
                        statement.executeUpdate("INSERT INTO staff(name ,age, job_title, professional_title, dept, contact_info) VALUES ('" + name + "', '" + age + "', '" + job + "', '" + professional + "', '" + department + "', '" + contact + "')");
                        result = statement.executeQuery("SELECT MAX(id) AS max_id FROM staff");
                        result.next();
                        int id = result.getInt("max_id");
						statement.executeUpdate("INSERT INTO nurse(staff_id) VALUES ( '" + id + "')");
					} catch (SQLException e) {
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
                        name = scanner.next();
                        System.out.println("Age: ");
                        age = Integer.parseInt(scanner.next());
                        System.out.println("gender: ");
                        gender = scanner.next();
                        System.out.println("Job Title: ");
                        job = scanner.next();
                        System.out.println("Professional Title: ");
                        professional = scanner.next();
                        System.out.println("Department: ");
                        department = scanner.next();
                        System.out.println("Contact Info: ");
                        contact = scanner.next();
					} catch (Exception e) {
						System.out.println("invalid input.");
						break;
					}
					try {
						statement.executeUpdate("INSERT INTO staff(name ,age, job_title, professional_title, dept, contact_info) VALUES ('" + name + "', '" + age + "', '" + job + "', '" + professional + "', '" + department + "', '" + contact + "')");
                        result = statement.executeQuery("SELECT MAX(id) AS max_id FROM staff");
                        result.next();
                        int id = result.getInt("id");
                        statement.executeUpdate("INSERT INTO operator(staff_id) VALUES ('" + id + "')");
					} catch (SQLException e) {
						e.printStackTrace();
					}
                    
                } else if(type.toLowerCase().equals("wards")){
                    int charges;
                    int nurse;
                    int capacity;
					try {
                        System.out.println("charges per day: ");
                        charges = Integer.parseInt(scanner.next());
                        System.out.println("responsible nurse: ");
                        nurse = Integer.parseInt(scanner.next());
                        System.out.println("capacity: ");
                        capacity = Integer.parseInt(scanner.next());
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
						ward = Integer.parseInt(scanner.next());
						System.out.println("Patient ID:");
						patient = Integer.parseInt(scanner.next());
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
                String type = scanner.next();
                if(type.toLowerCase().equals("patient")){
                    System.out.println("Patient ID for update: ");
                    int id = Integer.parseInt(scanner.next());
                    while (true) {
                        
                        System.out.println("Options for patient update: quit, social, status, gender, name, dob, age, contact");
                        String option = scanner.next();
                        if (option.equals("social")) {
                            String social = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE patient SET ssn=" + social + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("status")) {
                            String status = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE patient SET status=" + status + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("gender")) {
                            String gender = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE patient SET gender=" + gender + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("name")) {
                            String name = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE patient SET name=" + name + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("dob")) {
                            String dob = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE patient SET DOB=" + dob + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("age")) {
                            int age = Integer.parseInt(scanner.next());
                            try {
                                statement.executeUpdate("UPDATE patient SET age=" + age + " WHERE patient_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating patient");
                                break;
                            }
                        }else if (option.equals("contact")) {
                            String contact = scanner.next();
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
                    int id = Integer.parseInt(scanner.next());
                    while (true) {
                        System.out.println("Options for doctor/staff update: quit, name, age, job, professional, dept, contact, specialist");
                        String option = scanner.next();
                        if (option.equals("quit")) {
                            break;
                        } else if (option.equals("name")) {
                            String name = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET name=" + name + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("age")) {
                            int age = Integer.parseInt(scanner.next());
                            try {
                                statement.executeUpdate("UPDATE staff SET age=" + age + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("job")) {
                            String job = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET job_title=" + job + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("professional")) {
                            String professional = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET professional_title=" + professional + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("dept")) {
                            String dept = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET dept=" + dept + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("contact")) {
                            String contact = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET contact_info=" + contact + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("specialist")) {
                            String specialist = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE doctor SET specialist=" + specialist + " WHERE doctor_id=" + id);
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
                    int id = Integer.parseInt(scanner.next());
                    while (true) {
                        System.out.println("Options for nurse/staff update: quit, name, age, job, professional, dept, contact");
                        String option = scanner.next();
                        if (option.equals("quit")) {
                            break;
                        } else if (option.equals("name")) {
                            String name = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET name=" + name + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("age")) {
                            int age = Integer.parseInt(scanner.next());
                            try {
                                statement.executeUpdate("UPDATE staff SET age=" + age + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("job")) {
                            String job = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET job_title=" + job + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("professional")) {
                            String professional = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET professional_title=" + professional + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("dept")) {
                            String dept = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET dept=" + dept + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("contact")) {
                            String contact = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET contact_info=" + contact + " WHERE staff_id=" + id);
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
                    int id = Integer.parseInt(scanner.next());
                    while (true) {
                        System.out.println("Options for Operator/staff update: quit, name, age, job, professional, dept, contact");
                        String option = scanner.next();
                        if (option.equals("quit")) {
                            break;
                        } else if (option.equals("name")) {
                            String name = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET name=" + name + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("age")) {
                            int age = Integer.parseInt(scanner.next());
                            try {
                                statement.executeUpdate("UPDATE staff SET age=" + age + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("job")) {
                            String job = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET job_title=" + job + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("professional")) {
                            String professional = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET professional_title=" + professional + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("dept")) {
                            String dept = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET dept=" + dept + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else if (option.equals("contact")) {
                            String contact = scanner.next();
                            try {
                                statement.executeUpdate("UPDATE staff SET contact_info=" + contact + " WHERE staff_id=" + id);
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
                    int ward = Integer.parseInt(scanner.next());
                    System.out.println("Options for ward update: quit, charges, responsible, capacity");
                    String option = scanner.next();
                    if (option.equals("quit")) {
                        break;
                    } else if (option.equals("charges")) {
                        int charges = Integer.parseInt(scanner.next());
                        try {
                                statement.executeUpdate("UPDATE ward SET charges_per_day=" + charges + " WHERE ward_num=" + ward);
                            } catch (SQLException e) {
                                System.out.println("Error updating ward");
                                break;
                            }
                    } else if (option.equals("responsible")) {
                        int responsible = Integer.parseInt(scanner.next());
                        try {
                                statement.executeUpdate("UPDATE ward SET res_nurse=" + responsible + " WHERE ward_num=" + ward);
                            } catch (SQLException e) {
                                System.out.println("Error updating ward");
                                break;
                            }
                    } else if (option.equals("capacity")) {
                        int capacity = Integer.parseInt(scanner.next());
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
                    int bed = Integer.parseInt(scanner.next());
                    System.out.println("ward number for bed update:");
                    int ward = Integer.parseInt(scanner.next());
                    System.out.println("Options for bed update: quit, patient");
                    String option = scanner.next();
                    if (option.equals("quit")) {
                        break;
                    } else if (option.equals("patient")) {
                        int patient = Integer.parseInt(scanner.next());
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
                String type = scanner.next();
                if(type.toLowerCase().equals("patient")){
                    System.out.println("Patient id for deletion:");
                    int id = Integer.parseInt(scanner.next());
                    try {
                        statement.executeUpdate("DELETE FROM patient WHERE patient_id=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting patient.");
                    }
                    
                } else if(type.toLowerCase().equals("doctor")){
                    System.out.println("doctor id for deletion:");
                    int id = Integer.parseInt(scanner.next());
                     try {
                        statement.executeUpdate("DELETE FROM doctor WHERE doctor_id=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting doctor.");
                    }
                    
                } else if(type.toLowerCase().equals("nurse")){
                    System.out.println("nurse id for deletion:");
                    int id = Integer.parseInt(scanner.next());
                     try {
                        statement.executeUpdate("DELETE FROM nurse WHERE nurse_id=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting nurse.");
                    }
                } else if(type.toLowerCase().equals("operator")){
                    System.out.println("operator id for deletion:");
                    int id = Integer.parseInt(scanner.next());
                    try {
                        statement.executeUpdate("DELETE FROM operator WHERE operator_id=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting operator.");
                    }
                    
                } else if(type.toLowerCase().equals("wards")){
                    System.out.println("ward num for deletion:");
                    int id = Integer.parseInt(scanner.next());
                    try {
                        statement.executeUpdate("DELETE FROM ward WHERE ward_num=" + id);
                    } catch (SQLException e) {
                        System.out.println("Error deleting ward.");
                    }
                    
                } else if(type.toLowerCase().equals("bed")){
                    System.out.println("bed id for deletion:");
                    int bedid = Integer.parseInt(scanner.next());
                    System.out.println("ward num for deletion of bed");
                    int wardid = Integer.parseInt(scanner.next());
                    try {
                        statement.executeUpdate("DELETE FROM bed WHERE bed_id=" + bedid + "AND WHERE ward_num=" + wardid);
                    } catch (SQLException e) {
                        System.out.println("Error deleting bed.");
                    }
                    
                } else {
                    System.out.println("Not a valid object to delete.");
                    continue;
                }
            } else if(action.toLowerCase().equals("check-in")){
                System.out.println("start date:");
                String startdate = scanner.next();
                System.out.println("end date:");
                String enddate = scanner.next();
                System.out.println("bed number:");
                int bed = Integer.parseInt(scanner.next());
                System.out.println("ward number:");
                int ward= Integer.parseInt(scanner.next());
                try {
                    statement.executeUpdate("INSERT INTO check_in_information( start_date, end_date, bed_num, ward_num) VALUES (" + startdate + "," + enddate + "," + bed + "," + ward + ")");
                } catch (SQLException e) {
                    System.out.println("Error creating check_in");
                }
                
            } else if(action.toLowerCase().equals("check-out")){
                System.out.println("Check in information ID:");
                int id = Integer.parseInt(scanner.next());
                System.out.println("date: ");
                String enddate = scanner.next();
                try {
                    statement.executeUpdate("UPDATE check_in_information SET end_date=" + enddate + " WHERE check_in_id=" + id);
                } catch (SQLException e) {
                    System.out.println("Error checking out");
                }
            } else if(action.toLowerCase().equals("reserve")){
                System.out.println("Patient id for reserving bed:");
                int patid = Integer.parseInt(scanner.next());
                System.out.println("bed num for reservation:");
                int bednum = Integer.parseInt(scanner.next());
                System.out.println("ward num for reservation");
                int wardnum = Integer.parseInt(scanner.next());
                try {
                    statement.executeUpdate("UPDATE bed patient_id=" + patid + "WHERE bed_num =" + bednum + "AND WHERE ward_num=" + wardnum + "AND patient_id=NULL"); 
                } catch (SQLException e) {
                    System.out.println("Error reserving bed.");
                }
            } else if(action.toLowerCase().equals("release")){
                System.out.println("Patient id for release:");
                int patid = Integer.parseInt(scanner.next());
                System.out.println("bed num for release:");
                int bednum = Integer.parseInt(scanner.next());
                System.out.println("ward num for release");
                int wardnum = Integer.parseInt(scanner.next());
                try {
                    statement.executeUpdate("UPDATE bed patient_id=NULL WHERE bed_num =" + bednum + "AND WHERE ward_num=" + wardnum); 
                } catch (SQLException e) {
                    System.out.println("Error reserving bed.");
                }
            } else if(action.toLowerCase().equals("reports")){
                String type = scanner.next();
                if(type.toLowerCase().equals("history")){
                    
                } else if(type.toLowerCase().equals("wards")){
                
                } else if(type.toLowerCase().equals("ppm")){
                    
                } else if(type.toLowerCase().equals("wup")){
                    
                } else if(type.toLowerCase().equals("doctor")){
                    
                } else if(type.toLowerCase().equals("staff")){
                    String staff = scanner.next();
                    if(staff.toLowerCase().equals("doctor")){
                    
                    } else if(staff.toLowerCase().equals("specialist")){
                        
                    } else if(staff.toLowerCase().equals("nurse")){
                        
                    } else if(staff.toLowerCase().equals("operator")){
                        
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