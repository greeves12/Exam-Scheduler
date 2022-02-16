/*
* File: Room.java
* Purpose: To create an object of a room that holds the room name and the capacity.
*
* Important variables: name - the name of the room
*                      capacity - the capacity of the room
* */
public class Room {
    private final String name;
    private int capacity;
    private int index = -1;

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

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }
}
