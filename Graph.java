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

public class Graph implements GraphInterface{
    private final ArrayList<Course> courses;
    private final ArrayList<Room> rooms;
    private Map<Vertex, List<Vertex>> graph = new HashMap<>();
    private ArrayList<Vertex> vertices = new ArrayList<>();


    public Graph(ArrayList<Course> courses, ArrayList<Room> rooms){
        this.courses = courses;
        this.rooms = rooms;

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

        for(Course c : courses){
            Vertex v = new Vertex(c.getName());
            vertices.add(v);

            graph.put(v, new ArrayList<>());
        }

        for(Course c : courses){
            Vertex vertex = getVertex(c.getName());

            for(Course deepCourse : courses){
                if(!c.getName().equals(deepCourse.getName())){
                    for(int x = 0; x < c.getStudents().size(); x++){
                        if(deepCourse.getStudents().contains(c.getStudents().get(x))){
                            Vertex vertex1 = getVertex(deepCourse.getName());

                            List<Vertex> list = graph.get(vertex);

                            list.add(vertex1);

                            graph.put(vertex, list);

                            System.out.println("UPDATE: Student " + c.getStudents().get(x) + " is in course " + c.getName() + " and " + deepCourse.getName());

                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public Course getStartNode() {
        return null;
    }


    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}
