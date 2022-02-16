import java.util.ArrayList;

/**
 * Represents a set of edges between two specific vertices
 */
public class Edge {

    private final Vertex otherVertex;
    private final ArrayList<Integer> students;

    public Edge(Vertex otherVertex, ArrayList<Integer> students) {
        this.otherVertex = otherVertex;
        this.students = students;
    }

    public Edge(Vertex otherVertex) {
        this.otherVertex = otherVertex;
        this.students = new ArrayList<>();
    }

    public void addStudent(Integer student) {
        if(!this.students.contains(student))
            this.students.add(student);
    }

    public Vertex getOtherVertex() {
        return otherVertex;
    }

    /**
     * @return The number of students/edges between the two vertices
     */
    public int numStudents() {
        return this.students.size();
    }

}
