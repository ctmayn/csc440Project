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
                        statement.executeUpdate("INSERT INTO patient(patient_id, status, gender, ssn, name, DOB, age, contact_info) VALUES (patient.seq.nextval," + status + ", " + gender + ", " + social + ", " + name + ", " + dob + ", " + age + ", " + contact + ")");
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
                        statement.executeUpdate("INSERT INTO staff(id, name ,age, job_title, professional_title, dept, contact_info) VALUES (staff.seq.nextval," + name + ", " + age + ", " + job + ", " + professional + ", " + department + ", " + contact + ")");
                        statement.executeUpdate("INSERT INTO doctor(staff_id, specialist) VALUES (staff.seq.currval, " + specialist + ")");
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
						statement.executeUpdate("INSERT INTO staff(id, name ,age, job_title, professional_title, dept, contact_info) VALUES (staff.seq.nextval," + name + ", " + age + ", " + job + ", " + professional + ", " + department + ", " + contact + ")");
						statement.executeUpdate("INSERT INTO nurse(staff_id) VALUES (staff.seq.currval)");
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
						statement.executeUpdate("INSERT INTO staff(id, name ,age, job_title, professional_title, dept, contact_info) VALUES (staff.seq.nextval," + name + ", " + age + ", " + job + ", " + professional + ", " + department + ", " + contact + ")");
						statement.executeUpdate("INSERT INTO operator(staff_id) VALUES (staff.seq.currval)");
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
						statement.executeUpdate("INSERT INTO ward(ward_num, charges_per_day, res_nurse, capacity) VALUES (ward.seq.nextval," + charges + "," + nurse + "," + capacity + ")");
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
						statement.executeUpdate("INSERT INTO ward(bed_num, ward_num, patient_id) VALUES (bed.seq.nextval," + ward + "," + patient + ")");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
                } else {
                    System.out.println("Not a valid object to enter information about.");
                    continue;
                }
            } else if(action.toLowerCase().equals("update")){
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
                            int age = Integeter.parseInt(scanner.next());
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
                                statement.executeUpdate("UPDATE staff SET contact_info=" + name + " WHERE staff_id=" + id);
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
                                statement.executeUpdate("UPDATE staff SET contact_info=" + name + " WHERE staff_id=" + id);
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
                                statement.executeUpdate("UPDATE staff SET contact_info=" + name + " WHERE staff_id=" + id);
                            } catch (SQLException e) {
                                System.out.println("Error updating staff");
                                break;
                            }
                        } else {
                            System.out.println("Invalid Input.");
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
                String type = scanner.next();
                if(type.toLowerCase().equals("patient")){
                    System.out.println("Patient id for deletion:");
                    int id = Integer.parseInt(scanner.next());
                    statement.executeUpdate("DELETE FROM patient WHERE patient_id=" + id);
                } else if(type.toLowerCase().equals("doctor")){
                    System.out.println("doctor id for deletion:");
                    int id = Integer.parseInt(scanner.next());
                    statement.executeUpdate("DELETE FROM doctor WHERE doctor_id=" + id);
                } else if(type.toLowerCase().equals("nurse")){
                    System.out.println("nurse id for deletion:");
                    int id = Integer.parseInt(scanner.next());
                    statement.executeUpdate("DELETE FROM nurse WHERE nurse_id=" + id);
                } else if(type.toLowerCase().equals("operator")){
                    System.out.println("operator id for deletion:");
                    int id = Integer.parseInt(scanner.next());
                    statement.executeUpdate("DELETE FROM operator WHERE operator_id=" + id);
                } else if(type.toLowerCase().equals("wards")){
                    System.out.println("ward num for deletion:");
                    int id = Integer.parseInt(scanner.next());
                    statement.executeUpdate("DELETE FROM ward WHERE ward_num=" + id);
                } else if(type.toLowerCase().equals("bed")){
                    System.out.println("bed id for deletion:");
                    int bedid = Integer.parseInt(scanner.next());
                    System.out.println("ward num for deletion of bed");
                    int wardid = Integer.parseInt(scanner.next());
                    statement.executeUpdate("DELETE FROM bed WHERE bed_id=" + bedid + "AND WHERE ward_num=" + wardid);
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
                    statement.executeUpdate("INSERT INTO check_in_information(check_in_id, start_date, end_date, bed_num, ward_num) VALUES (check_in_seq.nextval," + startdate + "," + enddate + "," + bed + "," + ward")");
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