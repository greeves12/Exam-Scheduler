import java.util.ArrayList;

/*
*
* */

public class Vertex {
    private String courseName;
    private int color;
    private Course course;
    private ArrayList<Edge> edges = new ArrayList<>();


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

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public void addEdge(Vertex otherCourse, ArrayList<Integer> students) {
        this.edges.add(new Edge(otherCourse, students));
    }

    public void addEdge(Vertex otherCourse) {
        this.edges.add(new Edge(otherCourse));
    }

    public boolean hasEdge(Vertex otherCourse) {
        for(int i = 0; i < edges.size(); i++)
            if(edges.get(i).getOtherVertex().equals(otherCourse))
                return true;
        return false;
    }

    public Edge getEdge(Vertex otherCourse) {
        for(int i = 0; i < this.edges.size(); i++)
            if(edges.get(i).getOtherVertex().equals(otherCourse))
                return edges.get(i);
        return null;
    }

    public ArrayList<Edge> getEdges() {
        this.sortEdges();
        return this.edges;
    }

    private void sortEdges() {
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for(int i = 0; i < this.edges.size()-1; i++)
                if(this.edges.get(i).numStudents() > this.edges.get(i+1).numStudents()) {
                    Edge temporaryEdge = this.edges.get(i);
                    this.edges.set(i, this.edges.get(i+1));
                    this.edges.set(i+1, temporaryEdge);
                    sorted = false;
                }
        }
    }

}
