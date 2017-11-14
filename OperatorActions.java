/**
     * This method is responsible for an operator's behavior. It will conintue to take requests
     * from the operator until they decide to quit out. Operators can enter, update, and delete 
     * staff and hospital information, check-in and check-out patients, reserve and release beds,
     * and generate reports.
     * 
     * @param statement Sends sql statements to the DBMS 
     * @param result Records the result of an SQL statement
     */
    private static void operatorActions(Statement statement, ResultSet result){
        System.out.println("operator Actions: enter, update, delete, check-in, check-out, reserve, release and reports");
        while(true){
            String action = scanner.next();
            if(action.toLowerCase().equals("quit")){
                return;
            } else if(action.toLowerCase().equals("enter")){
                String type = scanner.next();
                if(type.toLowerCase().equals("patient")){
                    try {
                        System.out.println("social security number: ");
                        int social = Integer.parseInt(scanner.next());
                        System.out.println("status: ");
                        String status = scanner.next();
                        System.out.println("Gender");
                        String gender = scanner.next();
                        System.out.println("Name: ");
                        String name = scanner.next();
                        System.out.println("Date of Birth: ");
                        String dob = scanner.next();
                        System.out.println("Age:");
                        int age = Integer.parseInt(scanner.next());
                        System.out.println("Contact Info: ");
                        String contact = scanner.next();
                    } catch (Exception) {
                        System.out.println("Invalid input.");
                        break;
                    }
                    try {
                        statement.executeUpdate("INSERT INTO patient VALUES (patient.seq.nextval, status, gender, social, name, dob, age, contact)");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    
                } else if(type.toLowerCase().equals("doctor")){
                    try {
                        System.out.println("Name: ");
                        String name = scanner.next();
                        System.out.println("Age: ");
                        int age = Integer.parseInt(scanner.next());
                        System.out.println("gender: ");
                        String gender = scanner.next();
                        System.out.println("Job Title: ");
                        String job = scanner.next();
                        System.out.println("Professional Title: ");
                        String professional = scanner.next();
                        System.out.println("Department: ");
                        String department = scanner.next();
                        System.out.println("Contact Info: ");
                        String contact = scanner.next();
                        System.out.println("Specialist: ");
                        String specialist = scanner.next();
                        
                    } catch (Exception e) {
                        System.out.println("Invalid Input.");
                        break;
                    }
                    try {
                        statement.executeUpdate("INSERT INTO staff VALUES (staff.seq.nextval, name, age, job, professional, department, contact)");
                        //select statement for getting staff_id for adding to doctor?
                        tatement.executeUpdate("INSERT INTO doctor VALUES (staff.seq.nextval, specialist)");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                        
                        
                    
                        statement.executeUpdate("INSERT INTO patient VALUES (patient.seq.nextval, status, gender, social, name, dob, age, contact)");
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    catch (Exception) {
                        System.out.println("Invalid input.");
                        continue;
                    }
                
                } else if(type.toLowerCase().equals("nurse")){
                    
                } else if(type.toLowerCase().equals("operator")){
                    
                } else if(type.toLowerCase().equals("wards")){
                    
                } else if(type.toLowerCase().equals("bed")){
                    
                } else {
                    System.out.println("Not a valid object to enter information about.");
                    continue;
                }
            } else if(action.toLowerCase().equals("update")){
                String type = scanner.next();
                if(type.toLowerCase().equals("patient")){
                    
                } else if(type.toLowerCase().equals("doctor")){
                
                } else if(type.toLowerCase().equals("nurse")){
                    
                } else if(type.toLowerCase().equals("operator")){
                    
                } else if(type.toLowerCase().equals("wards")){
                    
                } else if(type.toLowerCase().equals("bed")){
                    
                } else {
                    System.out.println("Not a valid object to update information about.");
                    continue;
                }
            } else if(action.toLowerCase().equals("delete")){
                String type = scanner.next();
                if(type.toLowerCase().equals("patient")){
                    
                } else if(type.toLowerCase().equals("doctor")){
                
                } else if(type.toLowerCase().equals("nurse")){
                    
                } else if(type.toLowerCase().equals("operator")){
                    
                } else if(type.toLowerCase().equals("wards")){
                    
                } else if(type.toLowerCase().equals("bed")){
                    
                } else {
                    System.out.println("Not a valid object to delete.");
                    continue;
                }
            } else if(action.toLowerCase().equals("check-in")){
                
            } else if(action.toLowerCase().equals("check-out")){
                
            } else if(action.toLowerCase().equals("reserve")){
                
            } else if(action.toLowerCase().equals("release")){
                
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