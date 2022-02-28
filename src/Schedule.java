import java.util.ArrayList;

public class Schedule {

    public ArrayList<Course>[][] solution;

    public Schedule(Graph inputGraph, int times) {
        for(int i = 0; i < times; i++) { //Calculates minimum number of time slots needed, up to the number of time slots allowed
            this.solution = this.getSolution(inputGraph, i+1);
            if(!(this.solution == null))
                break;
        }
        this.printSchedule(solution, inputGraph.getRooms());
    }

    /**
     * Base case of get solution. Returns, if possible, a 3d array representing an exam schedule.
     * @param graph The graph representing the input courses and rooms
     * @param times The number of time slots available
     * @return The 3d array representing an exam schedule
     */
    private ArrayList<Course>[][] getSolution(Graph graph, int times) {

        ArrayList<Course>[][] schedule = this.generateBlankSchedule(graph, times);
        ArrayList<ArrayList<Room> > timeSlotRooms = this.getTimeSlotRooms(graph, times); //Generate 2d array of rooms in each time slot, to keep track of remaining room capacity
        Vertex startingVertex = graph.getStartNode(); //Get course with highest number of students

        //Check if there are too many students in this course to fit in the largest room
        if(graph.getRooms().get(graph.getRooms().size()-1).getCapacity() < startingVertex.getCourse().getStudents().size()) {
            return null;
        } else {
            for(int room = 0; room < timeSlotRooms.get(0).size(); room++) {
                if (startingVertex.getCourse().getStudents().size() <= timeSlotRooms.get(0).get(room).getCapacity()) {
                    timeSlotRooms.get(0).get(room).subtractCapacity(startingVertex.getCourse().getStudents().size());
                    schedule[0][timeSlotRooms.get(0).get(room).getIndex()].add(startingVertex.getCourse());
                    this.sortRooms(timeSlotRooms.get(0));
                    break;
                }
            }
        }

        //Get all remaining Vertices, representing courses other than the one that has already been scheduled
        ArrayList<Vertex> verticesLeft = new ArrayList<>();
        for(int i = 0; i < graph.getNumVertices(); i++)
            if(!(graph.getVertex(i).equals(startingVertex)))
                verticesLeft.add(graph.getVertex(i));

        if(startingVertex.getEdges().size()>0) //If there are any vertices connected by edges to the first scheduled course
            return getSolution(verticesLeft,schedule, startingVertex.getEdges().get(0).getOtherVertex(), timeSlotRooms);
        else if(verticesLeft.size() > 0) { //If there are any vertices left to be schedules not connected to the first scheduled course
            return getSolution(verticesLeft, schedule, verticesLeft.get(0), timeSlotRooms);
        } else { //Otherwise, all courses have been scheduled, return the complete schedule.
            return schedule;
        }

    }

    /**
     * Recursive case for getSolution. See other getSolution() for description.
     * @param verticesLeft Array of vertices (each vertex represents a course)
     * @param currentSchedule The partially filled in current schedule
     * @param nextVertex The vertex (or course) to be added to the schedule in this loop
     * @param rooms The 2d array of rooms representing each room in each time slot
     * @return currentSchedule, but with nextVertex added to it
     */
    private ArrayList<Course>[][] getSolution(ArrayList<Vertex> verticesLeft, ArrayList<Course>[][] currentSchedule, Vertex nextVertex, ArrayList<ArrayList<Room> > rooms) {

        boolean scheduled = false; //Boolean to keep track of whether the course represented by "nextVertex" has been scheduled
        for(int time = 0; time < currentSchedule.length; time++) { //Loop through time slots
            if (!this.conflicts(currentSchedule[time], nextVertex.getCourse())) //If there are no student conflicts between "nextVertex" and other remaining scheduled courses
                for(int room = 0; room < currentSchedule[time].length; room++) { // Loop through rooms to see if one has the capacity for the current course
                    if(rooms.get(time).get(room).getCapacity() >= nextVertex.getCourse().getStudents().size()) { // Check to see if current room has the correct capacity
                        currentSchedule[time][rooms.get(time).get(room).getIndex()].add(nextVertex.getCourse()); //Add the course to the schedule
                        rooms.get(time).get(room).subtractCapacity(nextVertex.getCourse().getStudents().size()); //Subtract the students in the course from the total capacity of the room
                        this.sortRooms(rooms.get(time)); //Resort the rooms based on the new capacity
                        System.out.println("Adding Course to Schedule: Course " + nextVertex.getCourseName() + ", Time Slot " + time + ", Room " + rooms.get(time).get(room));
                        scheduled = true; //The course has been scheduled
                        break; //Stop looping through rooms
                    }
                }
            if(scheduled) //If the course has been scheduled, stop looping through time slots
                break;
        }
        if(scheduled) { //If the course has been scheduled
            verticesLeft.remove(nextVertex);

            if(verticesLeft.size() == 0)
                return currentSchedule;

            ArrayList<Edge> possibleNextEdges = nextVertex.getEdges();
            for(int edge = 0; edge < possibleNextEdges.size(); edge++) {
                if(verticesLeft.contains(possibleNextEdges.get(edge).getOtherVertex()))
                    return getSolution(verticesLeft, currentSchedule, possibleNextEdges.get(edge).getOtherVertex(), rooms);
            }

            return getSolution(verticesLeft, currentSchedule, verticesLeft.get(0), rooms);

        } else {
            return null; //If the course has not been scheduled, NO POSSIBLE SOLUTION
        }
    }

    /**
     * Generates a blank schedule for adding courses to.
     * @param graph The graph with the list of rooms
     * @param times The total times slots available
     * @return A blank 3d array representing a schedule
     */
    private ArrayList<Course>[][] generateBlankSchedule(Graph graph, int times) {
        ArrayList<Course>[][] schedule = new ArrayList[times][graph.getRooms().size()];
        for(int i = 0; i < schedule.length; i++)
            for(int x = 0; x < schedule[i].length; x++)
                schedule[i][x] = new ArrayList<>();
        return schedule;
    }

    /**
     * Generate the starting rooms 2d array for updating room capacities as courses are added to the schedule
     * @param graph The graph with the list of rooms and room capacities
     * @param times The number of time slots available
     * @return A starting 2d rooms array
     */
    private ArrayList<ArrayList<Room> > getTimeSlotRooms(Graph graph, int times) {
        ArrayList<ArrayList<Room> > timeSlotRooms =
                new ArrayList<ArrayList<Room> >(times);

        for(int i = 0; i < times; i++) {
            ArrayList<Room> r = new ArrayList<>();
            for (int j = 0; j < graph.getRooms().size(); j++) {
                r.add(graph.getRooms().get(j).getCopy());
                r.get(j).setIndex(j);
            }
            timeSlotRooms.add(r);
        }
        return timeSlotRooms;
    }

    /**
     * Uses System.out.print() to print out a readable exam schedule.
     * @param schedule The schedule to be printed
     * @param rooms The list of rooms available
     */
    public void printSchedule(ArrayList<Course>[][] schedule, ArrayList<Room> rooms) {

        if(!(schedule == null)) {
            System.out.println("\n\n");
            for (int i = 0; i < schedule.length; i++) {
                System.out.println("Time Slot " + (i + 1));
                for (int j = 0; j < schedule[i].length; j++) {
                    System.out.print("\t" + rooms.get(j).getName() + " (Capacity " + rooms.get(j).getCapacity() + "): ");
                    for(int k = 0; k < schedule[i][j].size(); k++)
                        System.out.print("\t" + schedule[i][j].get(k));
                    System.out.print("\n");
                }
            }
        } else {
            System.out.println("No Valid Solution!");
        }

    }

    /**
     * Determines whether "course" has a time conflict with the other courses in the time slot represented by schedule
     * @param schedule The schedule of a time slot
     * @param course The course to check for conflicts with
     * @return true if there is a conflict, false if there is no conflict
     */
    private boolean conflicts(ArrayList<Course>[] schedule, Course course) {
        for(int i = 0; i < schedule.length; i++)
            for(int j = 0; j < schedule[i].size(); j++){
            if(!(schedule[i] == null) && schedule[i].get(j).conflict(course))
                return true;
        }
        return false;
    }

    /**
     * Basic bubble sort. Sorts the rooms arraylist based on capacity (from smallest capacity to largest capacity)
     * @param rooms The arraylist of rooms
     */
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

    /*private void sortCourses(ArrayList<Course> courses) {
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

    public Course[][] getSolution(ArrayList<Room> totalRooms, ArrayList<Course> totalCourses, int times) {

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
}
