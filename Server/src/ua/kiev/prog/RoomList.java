package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

public class RoomList {
    private static final RoomList roomList = new RoomList();
    private UserList userList = UserList.getInstance();

    private final Gson gson;
    private final List<Room> list = new LinkedList<>();

    public static RoomList getInstance() {
        return roomList;
    }

    private RoomList() {
        gson = new GsonBuilder().create();
    }

    public synchronized void create(Room room) {
        list.add(room);
    }

    public Room getRoom(String name) {
        for (Room room : list) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

//    public synchronized String toJSON(int n) {
//        if (n >= list.size()) return null;
//        return gson.toJson(new JsonUsers(list, n));
//    }
}
