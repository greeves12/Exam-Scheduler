public class Vertex {
    private String courseName;
    private int color = 0;

    public Vertex(String courseName){
        this.courseName = courseName;
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
}
