import java.util.*;

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

/*
* TODO:
*  - Coloring algorithm (COMPLETE)
*  - Need to filter duplicates still (What I mean is that my algorithm always creates the least colors so we need to
*    filter the duplicates and make them their own color, so if a time slot of 5 is requested and we use 2 colors, then
*    one color has 3 vertices and the other has 2, such that we need to take at least 3 vertices and make them their own
*    colors to fill 5 time slots.) PM me on discord if this doesn't make sense.
*
* */

public class Graph implements GraphInterface{
    private final ArrayList<Course> courses;
    private final ArrayList<Room> rooms;
    private Map<Vertex, List<Vertex>> graph = new HashMap<>();
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private final int maxColors;

    public Map<Vertex, List<Vertex>> getGraph() {
        return graph;
    }

    public Graph(ArrayList<Course> courses, ArrayList<Room> rooms, int maxColors){
        this.courses = courses;
        this.rooms = rooms;
        this.maxColors = maxColors;

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
        int colors = 0;
        ArrayList<Course> courses = getCourses();

        for(Course c : courses){
            Vertex v = new Vertex(c.getName(), c.getStudents().size());
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

                            if(!list.contains(vertex1)) {
                                list.add(vertex1);
                                graph.put(vertex, list);
                                //System.out.println("UPDATE: Student " + c.getStudents().get(x) + " is in course " + c.getName() + " and " + deepCourse.getName());
                            }

                            break;
                        }
                    }
                }
            }
        }

        if(colors <= getMaxColors()) {
            ArrayList<Vertex> vert = colorVertices();
            ArrayList<TimeSlot> timeSlots = new ArrayList<>();

            while ((highestDegree(vert)) < getMaxColors() && timeSlots.isEmpty()) {
                timeSlots = generateRooms(vert);

                if(timeSlots.isEmpty()){
                   // vert = generateNewColors(vert);
                    System.out.println("ERRORS: Impossible to generate time table with the given courses");
                    break;
                }
            }

            for(TimeSlot timeSlot : timeSlots){
                System.out.println("Time Slot #" + timeSlot.getSlot());
                System.out.println();

                for(Map.Entry<Vertex, Room> entry : timeSlot.getCourse().entrySet()){
                    System.out.println("   " + entry.getKey().getCourseName() + "        " + entry.getValue().getName());
                }

                System.out.println();
            }
        }else{
            System.out.println("ERROR: Cannot generate schedule, not enough time slots.");
        }

/*
        if(colors > getMaxColors()){
            System.out.println("ERROR: Time conflict results with " + getMaxColors() + " time slots.");
        }else if(colors < getMaxColors()){
            ArrayList<Vertex> newColors = generateNewColors(vert);

            for(Vertex v : newColors){
                System.out.println("Course: " + v.getCourseName() + " Color: " + v.getColor());
            }
        }*/
    }

    private ArrayList<TimeSlot> generateRooms(ArrayList<Vertex> courses){
        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
        ArrayList<Room> roomsAvailable = new ArrayList<>(rooms);
        ArrayList<Room> tempRooms = new ArrayList<>(rooms);
        HashMap<Integer, ArrayList<Vertex>> course = new HashMap<>();
        int slot = 1;

        for(Vertex v : courses){
            if(!course.containsKey(v.getColor())){
                ArrayList<Vertex> color = new ArrayList<>();
                color.add(v);
                course.put(v.getColor(), color);
            }else{
                course.get(v.getColor()).add(v);
            }
        }

        for(Map.Entry<Integer, ArrayList<Vertex>> entry : course.entrySet()){
            ArrayList<Vertex> colorCourses = entry.getValue();
            boolean flag = true;
            TimeSlot timeSlot = new TimeSlot(slot);

            while (!colorCourses.isEmpty()){
                if(roomsAvailable.isEmpty()){
                    flag = false;
                    break;
                }

                Vertex biggestCourse = findLargestCourse(colorCourses);
                Room biggestRoom = findLargetRoom(roomsAvailable);

                if(biggestCourse.getCapacity() > biggestRoom.getCapacity()){
                    flag = false;
                    break;
                }

                biggestRoom.setCapacity(biggestRoom.getCapacity() - biggestCourse.getCapacity());
                HashMap<Vertex, Room> map = timeSlot.getCourse();
                map.put(biggestCourse, biggestRoom);
                timeSlot.setCourse(map);
                colorCourses.remove(biggestCourse);

                if(biggestRoom.getCapacity() == 0){
                    roomsAvailable.remove(biggestRoom);
                }
            }

            timeSlots.add(timeSlot);

            if(!flag){
                timeSlots.clear();
                break;
            }

            roomsAvailable.clear();
            roomsAvailable.addAll(tempRooms);

            slot++;
        }

        return timeSlots;
    }

    private Room findLargetRoom(ArrayList<Room> rooms){
        int size = 0;
        Room v = null;

        for(Room vertex : rooms){
            if(vertex.getCapacity() > size){
                v = vertex;
                size = vertex.getCapacity();
            }
        }

        return v;
    }

    private Vertex findLargestCourse(ArrayList<Vertex> vertice){
        int size = 0;
        Vertex v = null;

        for(Vertex vertex : vertice){
            if(vertex.getCapacity() > size){
                v = vertex;
            }
        }

        return v;
    }

    private int highestDegree(ArrayList<Vertex> vertices){
        int degree = 0;

        for(Vertex v : vertices){
            if(v.getColor() > degree){
                degree = v.getColor();
            }
        }
        return degree;
    }

    private ArrayList<Vertex> generateNewColors(ArrayList<Vertex> vertices){
        ArrayList<Vertex> newColors = new ArrayList<>();
        HashMap<Integer, ArrayList<Vertex>> duplicates = new HashMap<>();
        HashMap<Integer, ArrayList<Vertex>> temp = new HashMap<>();
        int degree;
        int colors;


        for(Vertex v : vertices){
            ArrayList<Vertex> v3;

            if(!duplicates.containsKey(v.getColor())){
                v3 = new ArrayList<>();
            }else{
                v3 = duplicates.get(v.getColor());
            }
            v3.add(v);
            duplicates.put(v.getColor(), v3);
        }

        degree = highestDegree(duplicates) + 1;
        colors = degree;

        colors = (getMaxColors()) - colors;

        for (Map.Entry<Integer, ArrayList<Vertex>> entry : duplicates.entrySet()) {
            if (colors >= 0) {
                while (entry.getValue().size() > 1) {
                    ArrayList<Vertex> newVertex = new ArrayList<>();
                    entry.getValue().get(0).setColor(degree);
                    newVertex.add(entry.getValue().get(0));
                    entry.getValue().remove(0);


                    temp.put(degree, newVertex);
                    colors--;
                    degree++;
                }
            } else {
                break;
            }
        }

        for(Map.Entry<Integer, ArrayList<Vertex>> entry : temp.entrySet()){
            duplicates.put(entry.getKey(), entry.getValue());
        }

        if(colors > 0){
            System.out.println("Impossible to make time table");
        }else{
            for(Map.Entry<Integer, ArrayList<Vertex>> entry : duplicates.entrySet()){
                newColors.addAll(entry.getValue());
            }
        }

        return newColors;
    }

    public int getMaxColors() {
        return maxColors;
    }

    private int highestDegree(HashMap<Integer, ArrayList<Vertex>> v){
        int degree = 0;

        for(Map.Entry<Integer, ArrayList<Vertex>> entry : v.entrySet()){
            if(entry.getKey() > degree){
                degree = entry.getKey();
            }
        }

        return degree;
    }

    private int minDegree(HashMap<Integer, ArrayList<Vertex>> v){
        int degree = 10000;

        for(Map.Entry<Integer, ArrayList<Vertex>> entry : v.entrySet()){
            if(entry.getKey() < degree){
                degree = entry.getKey();
            }
        }

        return degree;
    }

    private ArrayList<Vertex> colorVertices(){
      ArrayList<Vertex> coloredVertices = new ArrayList<>();
      ArrayList<Vertex> nonColored = new ArrayList<>(vertices);
      int color = 2;
      int highestDegree;
      int minDegree;
      HashMap<Integer, ArrayList<Vertex>> verticesByDegree = new HashMap<>();

      for(Vertex v : nonColored){
          if(!verticesByDegree.containsKey(getGraph().get(v).size())){
              ArrayList<Vertex> v23 = new ArrayList<>();
              v23.add(v);
              verticesByDegree.put(getGraph().get(v).size(), v23);

          }else{
              ArrayList<Vertex> retrieved = verticesByDegree.get(getGraph().get(v).size());
              retrieved.add(v);
              verticesByDegree.put(getGraph().get(v).size(), retrieved);

          }
      }


      highestDegree = highestDegree(verticesByDegree);
      minDegree = minDegree(verticesByDegree);

      if(highestDegree > 0) {
          ArrayList<Vertex> v1 = verticesByDegree.get(highestDegree);

          Vertex v2 = v1.get(0);
          v2.setColor(color);
          coloredVertices.add(v2);

          v1.remove(v2);

          verticesByDegree.put(highestDegree, v1);

          while (highestDegree > 0 && highestDegree >= minDegree) {
                ArrayList<Vertex> v = verticesByDegree.get(highestDegree);
                color--;
                while (!v.isEmpty()){

                    for(int x = 0; x < v.size(); x++){
                        List<Vertex> adjacentToV = getGraph().get(v.get(x));
                        boolean addColor = true;


                        for (int i = 0; i < adjacentToV.size(); i++) {
                            if(coloredVertices.contains(adjacentToV.get(i))) {

                                Vertex vert = null;
                                for(int t = 0; t < coloredVertices.size(); t++){
                                    if(coloredVertices.get(t).getCourseName().equals(adjacentToV.get(i).getCourseName())){
                                        vert = coloredVertices.get(t);
                                        break;
                                    }
                                }

                                boolean flag = false;
                                if(v.get(x).getColor() == 0) {
                                    v.get(x).setColor(color);
                                    flag = true;
                                }

                                if (vert.getColor() == color) {
                                    addColor = false;

                                }

                                if(flag){
                                    v.get(x).setColor(0);
                                }

                            }
                        }


                        if(addColor){
                            v.get(x).setColor(color);
                            coloredVertices.add(v.get(x));
                            v.remove(x);

                            verticesByDegree.put(highestDegree, v);
                        }
                    }
                    color++;

                }

                highestDegree--;
          }
      }

      if(minDegree == 0){
          ArrayList<Vertex> disconnected = verticesByDegree.get(minDegree);

          for(Vertex v : disconnected){
              v.setColor(color);
              coloredVertices.add(v);
          }
      }

      return coloredVertices;
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
