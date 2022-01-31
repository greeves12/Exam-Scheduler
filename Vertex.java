public class Vertex {
    private String courseName;
    private int color = 0;
    private int capacity;

    public Vertex(String courseName, int capacity){
        this.courseName = courseName;
        this.capacity = capacity;
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

    public int getCapacity() {
        return capacity;
    }
}
