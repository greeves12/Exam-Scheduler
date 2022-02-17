import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Group: Drew and Tate
 * Date: February 11, 2021
 * Source: Main.java
 *
 * Project Goal: To generate a time conflict free schedule (with room assignment) from a variety of courses, rooms and students.
 */
public class Main {
    private static boolean errorFlag = false;
    private static String errorMessage = "";

    /*
     * Function: main
     * Purpose: the main function of the program. It is an infinite loop until the user types stop into
     *          console. This means that the program can handle an infinite amount of schedules.
     *
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Exam Scheduler");

        while (true){
            System.out.print("$ sched ");
            String line = scanner.nextLine();

            if(line.equalsIgnoreCase("exit")){
                break;
            }

            runDataFetch(line.split(" "));

        }
        scanner.close();
    }

    /*
    * Function: runDataFetch
    * Purpose: A helper function to read input files (course and rooms) and
    *          interpret the data correctly.
    *
    * Inputs: arg - takes in an array of strings called arguments. Arg[0] = course file
    *                                                              Arg[1] = rooms file
    *                                                              Arg[2] = amount of time slots
    * */
    private static void runDataFetch(String[] arg){
        ArrayList<Room> rooms;
        ArrayList<Course> courses;

        File courseFile = new File("test_data/" + arg[0]);
        File roomFile = new File("test_data/" + arg[1]);
        int slots = Integer.parseInt(arg[2]);
        if(courseFile.exists()){
            if(roomFile.exists()){
                if(slots > 0){
                    courses = fetchCourses(courseFile);

                    if(!errorFlag) {
                        rooms = fetchRooms(roomFile);

                        if(!errorFlag){
                            new Schedule(new Graph(courses, rooms), slots);

                            System.out.println("------ Time Slots ------");
                        }
                    }else{
                        System.out.println(errorMessage);
                    }

                    errorFlag = false;
                }else{
                    System.out.println("ERROR: Invalid timeslot #" + slots);
                }
            }else{
                System.out.println("ERROR: Room file does not exist.");
            }
        }else{
            System.out.println("ERROR: Course file does not exist.");
        }
    }

    /*
     * Function: fetchCourses
     * Purpose: to fetch the information from the courses file and handle the data.
     *          This function will read until end of file and will read course name, and all the students
     *          and then assign it a course object that will be appended into the arraylist.
     *
     * Inputs: courseFile - the course file to read from
     */
    private static ArrayList<Course> fetchCourses(File courseFile){
        ArrayList<Course> courses = new ArrayList<>();

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
                                errorFlag = true;
                                errorMessage = "ERROR: Duplicate course name found.";
                                break;
                            }
                        }
                        if(errorFlag)
                            break;

                        data = sc.next();

                        while (Character.isDigit(data.charAt(0))) {
                            if(hasErrors(students, courseName, data)){
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
                        errorFlag = true;
                        errorMessage = "ERROR: Non Letter Course";
                        break;
                    }
                }

                if(Character.isLetter(data.charAt(0))){
                    courses.add(new Course(data, new ArrayList<>()));
                    System.out.println("PASS: Loaded Course " + data + " with size " + 0);
                }

            }else{
                errorFlag = true;
                errorMessage = "ERROR: Courses file is empty";
            }

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return courses;
    }

    /*
    * Function: hasErrors
    * Purpose: to verify that the data has no errors, such as a negative student number or duplicate courses.
    * */
    private static boolean hasErrors(ArrayList<Integer> students, String courseName, String data){
        if(!allNumbers(data)){
            errorFlag = true;
            errorMessage = "ERROR: StudentID contains non-digit";
            return true;
        }

        if (Integer.parseInt(data) < 0) {
            errorFlag = true;
            errorMessage = "ERROR: Negative studentID in " + courseName;
            return true;
        }

        if(students.contains(Integer.valueOf(data))){
            errorFlag = true;
            errorMessage = "ERROR: Duplicate students in a class found.";
            return true;
        }
        return false;
    }

    /*
    * Function: fetchRooms
    * Purpose: to fetch the information from the rooms file and handle the data.
     *          This function will read until end of file and will read room name, and the room capacity
     *          and then assign it a room object that will be appended into the arraylist.
     *
     * Inputs: roomFile - the room file to read from
    * */
    private static ArrayList<Room> fetchRooms(File roomFile){
        ArrayList<Room> rooms = new ArrayList<>();

        Scanner sc = null;
        try {
            sc = new Scanner(roomFile);

            if(!sc.hasNext()){
                errorFlag = true;
                errorMessage = "ERROR: Rooms file is empty.";
            }else {
                while (sc.hasNextLine()) {
                    String[] roomLine = sc.nextLine().split("\\s+|\\t+");

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

                    System.out.println("PASS: Loaded room " + roomLine[0] + " with capacity " + roomLine[1]);
                    rooms.add(new Room(roomLine[0],Integer.parseInt(roomLine[1])));
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return rooms;
    }

    /*
    * Function: allNumbers
    * Purpose: a helper function to determine if a string is all digits
    *
    * Inputs: s - the string to test
    *
    * Returns either true or false.
    * */
    private static boolean allNumbers(String s){
        for(int x = 0; x < s.length(); x++){
            if(!Character.isDigit(s.charAt(x))){
                return false;
            }
        }
        return true;
    }
}
