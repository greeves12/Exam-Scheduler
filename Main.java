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
            File courseFile = new File("testdata/" + arg[0]);
            File roomFile = new File("testdata/" + arg[1]);
            int slots = Integer.parseInt(arg[2]);
            if(courseFile.exists()){
                if(roomFile.exists()){
                    if(slots > 0){
                        try {
                            Scanner sc = new Scanner(courseFile);
                            String data = sc.next();
                            while (sc.hasNext()){

                                String courseName;
                                ArrayList<Integer> students = new ArrayList<>();

                                if(Character.isLetter(data.charAt(0))){
                                    courseName = data;
                                    for(int x = 0; x < courses.size(); x++){
                                        if(courses.get(x).getName().equals(data)){
                                            return;
                                           // System.out.println("ERROR: Duplicate course name found.");
                                          /*  errorFlag = true;
                                            errorMessage = "ERROR: Duplicate course name found.";
                                            re

                                        */}
                                    }

                                    data = sc.next();
                                    while (Character.isDigit(data.charAt(0))) {
                                        if(Integer.parseInt(data) < 0 ){
                                            System.out.println("ERROR: Negative studentID in " + courseName);
                                            return;
                                        }
                                        students.add(Integer.valueOf(data));
                                        if(!sc.hasNext()){
                                            break;
                                        }
                                        data = sc.next();
                                    }
                                    if(errorFlag){
                                        System.out.println(errorMessage);
                                        break;
                                    }
                                    courses.add(new Course(courseName, students));
                                    System.out.println("PASS: Loaded Course " + courseName + " with size " + students.size());
                                }else{
                                    System.out.println("ERROR: Non Letter Course");
                                    return;
                                }
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

}
