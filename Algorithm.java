import java.lang.reflect.Array;
import java.util.ArrayList;

public class Algorithm {

    public Course[][] solution;

    public Algorithm(Graph inputGraph, int times) {

        this.solution = this.getSolution(inputGraph, times);

        /*this.solution = this.getSolution(inputGraph.getRooms(), inputGraph.getCourses(), times);

        ArrayList<Room> rooms = new ArrayList<>();
        for(int i = 0; i < inputGraph.getRooms().size(); i++)
            rooms.add(inputGraph.getRooms().get(i));
        this.sortRooms(rooms);
*/
        this.printSolution(solution, inputGraph.getRooms());
    }

    private Course[][] getSolution(Graph graph, int times) {

        Course[][] schedule = new Course[times][graph.getRooms().size()];
        Vertex startingVertex = graph.getStartNode();
        ArrayList<Room> rooms = graph.getRooms();

        for(int i = 0; i < schedule.length; i++)
            for(int x = 0; x < schedule[i].length; x++)
                schedule[i][x] = null;

        if(graph.getRooms().get(graph.getRooms().size()-1).getCapacity() < startingVertex.getCourse().getStudents().size()) {
            return null;
        } else {
            for(int room = 0; room < rooms.size(); room++) {
                if (startingVertex.getCourse().getStudents().size() <= rooms.get(room).getCapacity()) {
                    schedule[0][room] = startingVertex.getCourse();
                    break;
                }
            }
        }

        ArrayList<Vertex> verticesLeft = new ArrayList<>();
        for(int i = 0; i < graph.getNumVertices(); i++)
            if(!(graph.getVertex(i).equals(startingVertex)))
                verticesLeft.add(graph.getVertex(i));

        if(startingVertex.getEdges().size()>0)
            return addCourses(verticesLeft,schedule, startingVertex.getEdges().get(0).getOtherVertex(), rooms);
        else if(verticesLeft.size() > 0) {
            return addCourses(verticesLeft, schedule, verticesLeft.get(0), rooms);
        } else {
            return schedule;
        }



    }

    private Course[][] addCourses(ArrayList<Vertex> verticesLeft, Course[][] currentSchedule, Vertex nextVertex, ArrayList<Room> rooms) {

        boolean scheduled = false;
        for(int time = 0; time < currentSchedule.length; time++) {
            if (!this.conflicts(currentSchedule[time], nextVertex.getCourse()))
                for(int room = 0; room < currentSchedule[time].length; room++) {
                    if(currentSchedule[time][room] == null && rooms.get(room).getCapacity() >= nextVertex.getCourse().getStudents().size()) {
                        currentSchedule[time][room] = nextVertex.getCourse();
                        System.out.println("Adding Course to Schedule: Course " + nextVertex.getCourseName() + ", Time Slot " + time + ", Room " + rooms.get(room));
                        scheduled = true;
                        break;
                    }
                }
            if(scheduled)
                break;
        }

        if(scheduled) {
            verticesLeft.remove(nextVertex);

            if(verticesLeft.size() == 0)
                return currentSchedule;

            ArrayList<Edge> possibleNextEdges = nextVertex.getEdges();
            for(int edge = 0; edge < possibleNextEdges.size(); edge++) {
                if(verticesLeft.contains(possibleNextEdges.get(edge).getOtherVertex()))
                    return addCourses(verticesLeft, currentSchedule, possibleNextEdges.get(edge).getOtherVertex(), rooms);
            }

            return addCourses(verticesLeft, currentSchedule, verticesLeft.get(0), rooms);

        } else {
            return null;
        }
    }

    /*public Course[][] getSolution(ArrayList<Room> totalRooms, ArrayList<Course> totalCourses, int times) {

            //Generates Schedules with leftover courses arraylist
            Course[][] schedule = new Course[times][totalRooms.size()];

            ArrayList<Course> courses = new ArrayList<>();
            for (int i = 0; i < totalCourses.size(); i++) {
                courses.add(totalCourses.get(i));
            }
            this.sortCourses(courses);

            for(int time = 0; time < times; time++) {
                for (int i = 0; i < schedule[time].length; i++)
                    schedule[time][i] = null;

                ArrayList<Room> rooms = new ArrayList<>();
                for (int i = 0; i < totalRooms.size(); i++) {
                    rooms.add(totalRooms.get(i));
                }
                this.sortRooms(rooms);

                for (int i = 0; i < courses.size(); i++) {
                    for (int x = 0; x < rooms.size(); x++) {
                        if (rooms.get(x).hasCapacity(courses.get(i)) && !conflicts(schedule[time], courses.get(i))) {
                            schedule[time][x] = courses.get(i);
                            courses.remove(i);
                            rooms.remove(x);
                            i = i - 1;
                            break;
                        }
                    }
                    if(courses.size() == 0)
                        break;
                }
                if(courses.size() == 0)
                    break;
            }

            if(courses.size() != 0)
                return null;
            return schedule;
    }
*/
    public void printSolution(Course[][] schedule, ArrayList<Room> rooms) {

        if(!(schedule == null)) {
            System.out.println("\n\n");
            for (int i = 0; i < schedule.length; i++) {
                System.out.println("Time Slot " + (i + 1));
                for (int j = 0; j < schedule[i].length; j++) {
                    System.out.println("\t" + rooms.get(j).getName() + " (Capacity " + rooms.get(j).getCapacity() + "): " + schedule[i][j]);
                }
            }
        } else {
            System.out.println("No Valid Solution!");
        }
    }

    private boolean conflicts(Course[] schedule, Course course) {
        for(int i = 0; i < schedule.length; i++) {
            if(!(schedule[i] == null) && schedule[i].conflict(course))
                return true;
        }
        return false;
    }

    private void sortRooms(ArrayList<Room> rooms) {
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < rooms.size() - 1; i++)
                if (rooms.get(i).getCapacity() > rooms.get(i + 1).getCapacity()) {
                    Room tempRoom = rooms.get(i);
                    rooms.set(i, rooms.get(i+1));
                    rooms.set(i+1, tempRoom);
                    sorted = false;
                }
        }
    }

    private void sortCourses(ArrayList<Course> courses) {
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            for(int i = 0; i < courses.size()-1; i++)
                if(courses.get(i).getStudents().size() < courses.get(i+1).getStudents().size()) {
                    Course tempCourse = courses.get(i);
                    courses.set(i, courses.get(i+1));
                    courses.set(i+1, tempCourse);
                    sorted = false;
                }
        }
    }
}
