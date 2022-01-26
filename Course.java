import java.util.ArrayList;

public class Course {
    String name;
    ArrayList<Integer> students;

    public Course(String name, ArrayList<Integer> students){
        this.name = name;
        this.students = students;
    }

    public String getName(){
        return name;
    }

    public boolean addStudent(int studentID){
        if(studentID < 0){
            return false;
        }

        if(!students.contains(studentID))
            students.add(studentID);

        return true;
    }

    public ArrayList<Integer> getStudents(){
        return students;
    }
}
