public class Room {
    private final String name;
    private int capacity;
    private final int prevCapacity;

    public Room(String name, int capacity){
        this.name = name;
        this.capacity = capacity;
        this.prevCapacity = capacity;
    }

    public void resetCapacity(){
        this.capacity = prevCapacity;
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
