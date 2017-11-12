import java.sql.*;
import java.util.*;


public class WolfHospital{
    private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/$USER$";
    private static final String user = "$USER$";
    private static final String password = "$PASSWORD$";

    private static Scanner scanner = null;

    public static void main(String[] args){
        try {
        Class.forName("org.mariadb.jdbc.Driver");

        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        statement = connection.createStatement();

        scanner =  new Scanner(System.in);

        setupTables(statement);

        logOn(statement);

        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    }

    private static void setupTables(Statement statement){

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