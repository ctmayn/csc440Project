import java.sql.*;
import java.util.*;


public class WolfHospital{
    private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/$USER$";
    private static final String user = "$USER$";
    private static final String password = "$PASSWORD$";

    private static Scanner scanner = null;

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

        logOn(statement);

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

    static void close(Connection conn) {
        if(conn != null) {
            try { conn.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(Statement st) {
        if(st != null) {
            try { st.close(); } catch(Throwable whatever) {}
        }
    }

    static void close(ResultSet rs) {
        if(rs != null) {
            try { rs.close(); } catch(Throwable whatever) {}
        }
    }

    private static void setupTables(Statement statement) throws SQLException{
        statement.executeUpdate("CREATE TABLE staff (" +
            "id INT PRIMARY KEY, "  +
            "name NVARCHAR2(128) NOT NULL, " +
            "age INT NOT NULL, " +
            "job_title NVARCHAR2(128) NOT NULL, "  +
            "professional_title NVARCHAR2(128) NOT NULL, "  +
            "dept NVARCHAR2(128) NOT NULL, " +
            "contact_info NVARCHAR2(128) NOT NULL)");
            
        statement.executeUpdate("CREATE TABLE doctor( "  +
            "staff_id INT PRIMARY KEY, "  +
            "specialist NVARCHAR2(128) "  +
            " CONSTRAINT doctors_fk FOREIGN KEY(staff_id) REFERENCES staff(id) "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE nurse( "  +
            "staff_id INT PRIMARY KEY, "  +
            "CONSTRAINT nurse_fk FOREIGN KEY(staff_id) REFERENCES staff(id)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE operator( "  +
            "staff_id INT PRIMARY KEY, "  +
            "CONSTRAINT operator_fk FOREIGN KEY(staff_id) REFERENCES staff(id) "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE patient( "  +
            "patient_id INT PRIMARY KEY, "  +
            "status NVARCHAR2(128) NOT NULL, "  +
            "gender NVARCHAR2(128) NOT NULL, "  +
            "ssn INT, "  +
            "name NVARCHAR2(128) NOT NULL, "  +
            "DOB NVARCHAR2(128) NOT NULL, "  +
            "age INT NOT NULL, "  +
            "contact_info NVARCHAR2(128) NOT NULL)");
            
        statement.executeUpdate("CREATE TABLE medical_record( "  +
            "record_num INT PRIMARY KEY, "  +
            "res_doctor INT NOT NULL, "  +
            "diagnosis_details NVARCHAR2(128), "  +
            "prescription NVARCHAR2(128), "  +
            "CONSTRAINT doctor_fk FOREIGN KEY(res_doctor) REFERENCES doctor(staff_id) "  +
            "ON DELETE CASCADE )");
            
        statement.executeUpdate("CREATE TABLE billing_account( "  +
            "billing_id INT PRIMARY KEY, "  +
            "billing_addr NVARCHAR2(128) NOT NULL, "  +
            "payment_info NVARCHAR2(128) NOT NULL)");
            
        statement.executeUpdate("CREATE TABLE ward( "  +
            "ward_num INT PRIMARY KEY, "  +
            "charges_per_day INT NOT NULL, "  +
            "res_nurse INT NOT NULL, "  +
            "capacity INT NOT NULL, "  +
            "CONSTRAINT nurse_fk FOREIGN KEY(res_nurse) REFERENCES nurse(staff_id)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE bed( "  +
            "bed_num INT PRIMARY KEY, "  +
            "ward_num INT PRIMARY KEY, "  +
            "patient_id INT, "  +
            "CONSTRAINT ward_fk FOREIGN KEY(ward_num) REFERENCES ward(ward_num)  "  +
            "ON DELETE CASCADE "  +
            "CONSTRAINT patient_fk FOREIGN KEY(patient_id) REFERENCES patient(patient_id)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE check_in_information( "  +
            "check_in_id INT PRIMARY KEY, "  +
            "start_date NVARCHAR2(128) NOT NULL, "  +
            "end_date NVARCHAR2(128), "  +
            "bed_num INT NOT NULL, "  +
            "ward_num INT NOT NULL, "  +
            "CONSTRAINT bed_fk FOREIGN KEY(bed_num) REFERENCES bed(bed_num)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT ward_fk FOREIGN KEY(ward_num) REFERENCES ward(ward_num)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE test( "  +
            "test_id INT PRIMARY KEY, "  +
            "record_num INT NOT NULL, "  +
            "test_name NVARCHAR2(128) NOT NULL, "  +
            "specialist INT NOT NULL, "  +
            "patient_id INT NOT NULL, "  +
            "test_results NVARCHAR2(128) NOT NULL "  +
            "CONSTRAINT specialist_fk FOREIGN KEY(specialist) REFERENCES doctor(staff_id)  "  +
            "ON DELETE CASCADE "  +
            "CONSTRAINT record_num_fk FOREIGN KEY(record_num) REFERENCES medical_record(record_num)  "  +
            "ON DELETE CASCADE "  +
            "CONSTRAINT patient_fk FOREIGN KEY(patient_id) REFERENCES patient(patient_id)  "  +
            "ON DELETE CASCADE)");
            
        statement.executeUpdate("CREATE TABLE office_visit( "  +
            "visit_num INT PRIMARY KEY "  +
            "record_num INT, "  +
            "billling_id INT, "  +
            "check_in_id INT, "  +
            "patient_id INT, "  +
            "CONSTRAINT record_num_fk FOREIGN KEY(record_num) REFERENCES medical_record(record_num)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT billing_fk FOREIGN KEY(billing_id) REFERENCES billing_accountf(billing_id)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT check_in_fk FOREIGN KEY(check_in_id) REFERENCES check_in(check_in_id)  "  +
            "ON DELETE CASCADE, "  +
            "CONSTRAINT patient_fk FOREIGN KEY(patient_id) REFERENCES patient(patient_id)  "  +
            "ON DELETE CASCADE)");
            
    }

    private static void logOn(Statement statement){
        System.out.println("Please Enter Staff ID: ");
        String id = scanner.next();

        //TODO check if is is in the staff info then check which title they have to give them certain actions

        if(/*Operator*/true){
            operatorActions(statement);
        }else if(/*Doctor*/true){
            doctorActions(statement);
        }else if(/*Specialist*/true){
            specialistActions(statement);
        }else if(/*Nurse*/true){
            nurseActions(statement);                  
        } else {
            //error
        }
    }

    private static void operatorActions(Statement statement){


        while(true){
            String action = scanner.next();
            if(action.toLowerCase().equals("quit")){
                return;
            }
        }
    }

    private static void doctorActions(Statement statement){
        while(true){
            String action = scanner.next();
            if(action.toLowerCase().equals("quit")){
                return;
            }
        }
    }

    private static void specialistActions(Statement statement){
        while(true){
            String action = scanner.next();
            if(action.toLowerCase().equals("quit")){
                return;
            }
        }
    }

    private static void nurseActions(Statement statement){
        while(true){
            String action = scanner.next();
            if(action.toLowerCase().equals("quit")){
                return;
            }
        }
    }
}