import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* NOTE: For this graph data structure, the vertex is the key to unlocking the adjacent vertices, thus
* if you need to get the adjacent vertices for COMP2233 you would loop through the vertices arraylist to find
* the vertex with the name COMP2233 and then use that to get the arraylist for the adjacent vertices. If that arraylist is empty
* that means there are no adjacent vertices and thus it is disconnected from the graph.
*
* OPTION 2: If the above option isn't viable, you may iterate through the hashmap to find a vertex that has the name that you are
* looking for. For example if you need to find vertex COMP2233 then iterate through the hashmap to find a vertex with the name
* COMP2233 and thus when you find the vertex you're looking for you can pull its value (which is the arraylist containing all adjacent
* vertices).
*
* Let me know through discord if you have any questions. The code that I have written handles the constraints and
* connects vertices if need be.
* */

public class Graph{
    private final ArrayList<Course> courses;
    private final ArrayList<Room> rooms;
    private Map<Vertex, List<Vertex>> graph = new HashMap<>();
    private ArrayList<Vertex> vertices = new ArrayList<>();


    public Graph(ArrayList<Course> courses, ArrayList<Room> rooms){
        this.courses = courses;
        this.rooms = rooms;

        this.sortRooms();
        createGraph();
    }

    private Vertex getVertex(String courseName){
        Vertex vertex = null;

        for(int i = 0; i < vertices.size(); i++){
            if(courseName.equals(vertices.get(i).getCourseName())){
                vertex = vertices.get(i);
                break;
            }
        }
        return vertex;
    }

    private void createGraph(){
        ArrayList<Course> courses = getCourses();

        //Add all courses to hashmap (graph) pointing to an arraylist of vertices, and add to list of vertices
        for(Course c : courses){
            Vertex v = new Vertex(c);
            vertices.add(v);

            graph.put(v, new ArrayList<>());
        }

        for(Course c : courses){    //For each course
            Vertex vertex = getVertex(c.getName()); //Get the vertex representing that course

            for(Course deepCourse : courses){   //Loop through all courses
                if(!c.getName().equals(deepCourse.getName())){ //Make sure not the same course
                    for(int x = 0; x < c.getStudents().size(); x++){ //Loop through students in c
                        if(deepCourse.getStudents().contains(c.getStudents().get(x))){ //If deepCourse and c have the same student
                            Vertex vertex1 = getVertex(deepCourse.getName());   //Get the vertex

                            List<Vertex> list = graph.get(vertex);

                            if(!list.contains(vertex1)) {

                                if(!vertex.hasEdge(vertex1))
                                    vertex.addEdge(vertex1);
                                if(!(vertex.getEdge(vertex1) == null)){
                                    vertex.getEdge(vertex1).addStudent(c.getStudents().get(x));
                                }

                                list.add(vertex1);
                                graph.put(vertex, list);
                            }

                            System.out.println("UPDATE: Student " + c.getStudents().get(x) + " is in course " + c.getName() + " and " + deepCourse.getName());

                        }
                    }
                }
            }
        }
    }

    public Vertex getStartNode() {
        return vertices.get(0);
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public boolean removeCourse(Course course) {
        return this.courses.remove(course);
    }

    private void sortRooms() {
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for(int i = 0; i < this.rooms.size()-1; i++)
                if(this.rooms.get(i).getCapacity() > this.rooms.get(i+1).getCapacity()) {
                    Room temporaryRoom = this.rooms.get(i);
                    this.rooms.set(i, this.rooms.get(i+1));
                    this.rooms.set(i+1, temporaryRoom);
                    sorted = false;
                }
        }
    }

    public Vertex getVertex(int index) {
        return this.vertices.get(index);
    }

    public int getNumVertices() {
        return this.vertices.size();
    }
}
