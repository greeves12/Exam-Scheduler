public class Vertex {
    private String courseName;
    private int color;
    private Course course;


    public Vertex(String courseName, Course course){
        this.courseName = courseName;
        this.course = course;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getCourseName() {
        return courseName;
    }

    public Course getCourse() {return this.course;}
}
