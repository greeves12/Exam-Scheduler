import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean running = true;
        boolean errorFlag = false;
        String errorMessage = "";
        Scanner scanner = new Scanner(System.in);
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Course> courses = new ArrayList<>();


        System.out.println("Exam Scheduler");

        while (running){
            System.out.print("$ sched ");
            String line = scanner.nextLine();

            if(line.equalsIgnoreCase("exit")){
                running = false;
            }

            String[] arg = line.split(" ");
            File courseFile = new File("test data/" + arg[0]);
            File roomFile = new File("test data/" + arg[1]);
            int slots = Integer.parseInt(arg[2]);
            if(courseFile.exists()){
                if(roomFile.exists()){
                    if(slots > 0){
                        try {
                            Scanner sc = new Scanner(courseFile);

                            if(sc.hasNext()) {
                                String data = sc.next();
                                while (sc.hasNext()) {

                                    String courseName;
                                    ArrayList<Integer> students = new ArrayList<>();

                                    if (Character.isLetter(data.charAt(0))) {
                                        courseName = data;
                                        for (int x = 0; x < courses.size(); x++) {
                                            if (courses.get(x).getName().equals(data)) {
                                                // System.out.println("ERROR: Duplicate course name found.");
                                                errorFlag = true;
                                                errorMessage = "ERROR: Duplicate course name found.";
                                                break;
                                            }
                                        }
                                        if(errorFlag)
                                            break;

                                        data = sc.next();
                                        while (Character.isDigit(data.charAt(0))) {
                                            if(!allNumbers(data)){
                                                errorFlag = true;
                                                errorMessage = "ERROR: StudentID contains non-digit";

                                                break;
                                            }

                                            if (Integer.parseInt(data) < 0) {
                                                //  System.out.println("ERROR: Negative studentID in " + courseName);
                                                errorFlag = true;
                                                errorMessage = "ERROR: Negative studentID in " + courseName;
                                                break;
                                            }

                                            if(students.contains(Integer.valueOf(data))){
                                                errorFlag = true;
                                                errorMessage = "ERROR: Duplicate students in a class found.";
                                                break;
                                            }

                                            students.add(Integer.valueOf(data));
                                            if (!sc.hasNext()) {
                                                break;
                                            }
                                            data = sc.next();
                                        }
                                        if (errorFlag) {
                                            break;
                                        }
                                        courses.add(new Course(courseName, students));
                                        System.out.println("PASS: Loaded Course " + courseName + " with size " + students.size());
                                    } else {
                                        //   System.out.println("ERROR: Non Letter Course");
                                        errorFlag = true;
                                        errorMessage = "ERROR: Non Letter Course";
                                        break;
                                    }
                                }
                            }else{
                                errorFlag = true;
                                errorMessage = "ERROR: Courses file is empty";
                            }

                            sc.close();


                            if(!errorFlag){
                                sc = new Scanner(roomFile);
                                if(!sc.hasNext()){
                                    errorFlag = true;
                                    errorMessage = "ERROR: Rooms file is empty.";
                                }else {
                                    while (sc.hasNextLine()) {
                                        String[] roomLine = sc.nextLine().split("\s+|\t+");

                                        if(roomLine.length == 1){
                                            errorFlag = true;
                                            errorMessage = "ERROR: Invalid room.";
                                            break;
                                        }

                                        if(!allNumbers(roomLine[1])){
                                            errorFlag = true;
                                            errorMessage = "ERROR: Room has non digits.";
                                            break;
                                        }else if(Integer.parseInt(roomLine[1]) < 0){
                                            errorFlag = true;
                                            errorMessage = "ERROR: Room is a negative number.";
                                            break;
                                        }

                                        for(int x = 0; x < rooms.size(); x++){
                                            if(rooms.get(0).getName().equals(roomLine[0])){
                                                errorFlag = true;
                                                errorMessage = "ERROR: Duplicate rooms found.";
                                                break;
                                            }
                                        }

                                        if(errorFlag){
                                            break;
                                        }

                                        System.out.println("PASS: Loaded room " + roomLine[0] + " with capacity " + roomLine[1]);
                                        rooms.add(new Room(roomLine[0],Integer.parseInt(roomLine[1])));
                                    }

                                }

                            }

                            if(errorFlag) {
                                System.out.println(errorMessage);
                                System.out.println("----- Exam Maker Cancelled Due To Error -----");
                                errorFlag = false;
                            }else{
                                //execute exam formation
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println("ERROR: Invalid slot #" + slots);
                    }
                }else{
                    System.out.println("ERROR: Room file does not exist.");
                }
            }else{
                System.out.println("ERROR: Course file does not exist.");
            }

            courses.clear();
            rooms.clear();
        }
        scanner.close();
    }

    private static boolean allNumbers(String s){
        for(int x = 0; x < s.length(); x++){
            if(!Character.isDigit(s.charAt(x))){
                return false;
            }
        }
        return true;
    }
}
