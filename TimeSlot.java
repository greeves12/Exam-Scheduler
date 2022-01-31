import java.sql.Time;
import java.util.HashMap;

public class TimeSlot {
    private final int slot;
    private HashMap<Vertex, Room> course = new HashMap<>();

    public TimeSlot(int slot){
        this.slot = slot;
    }

    public HashMap<Vertex, Room> getCourse() {
        return course;
    }

    public void setCourse(HashMap<Vertex, Room> course) {
        this.course = course;
    }

    public int getSlot() {
        return slot;
    }
}
