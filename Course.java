import java.util.ArrayList;

public class Course {
    private String name;
    private ArrayList<Integer> students;

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

    public boolean conflict(Course otherCourse) {
        for(int i = 0; i < this.students.size(); i++)
            for(int x = 0; x < otherCourse.getStudents().size(); x++)
                if(this.students.get(i).equals(otherCourse.getStudents().get(x)))
                    return true;
        return false;
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
