public class Room {
    private final String name;
    private int capacity;

    public Room(String name, int capacity){
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean hasCapacity(Course course) {
        return this.capacity >= course.getStudents().size();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public Room getCopy() {
        return new Room(this.name, this.capacity);
    }

    public void subtractCapacity(int amount) {
        this.capacity -= amount;
    }

}
