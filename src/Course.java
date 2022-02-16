import java.util.ArrayList;

/*
* File: Course.java
* Purpose: To create an object of a course that holds the course name and the array of students.
*
* Important variables: name - course name
*                      students - an arraylist of all students in a course
* */

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

    public ArrayList<Integer> getStudents(){
        return students;
    }

    /**
     * Determines whether there is any student in this course and "otherCourse", creating an exam conflict
     * @param otherCourse The other course
     * @return true if there is a conflict, false if there is no conflict
     */
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
