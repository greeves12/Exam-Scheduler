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

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

}
