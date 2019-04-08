package ua.kiev.prog;

import java.util.LinkedList;
import java.util.List;

public class Room {
    private String name;
    private List<Message> list;
    private UserList userList = UserList.getInstance();
    private List<User> users = new LinkedList<>();

    public Room(String name) {
        this.name = name;
        this.list = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getList() {
        return list;
    }

    public void setList(List<Message> list) {
        this.list = list;
    }

    public void addUser(String user) {
        users.add(userList.getUser(user));
    }

    public void removeUser(String user) {
        users.remove(userList.getUser(user));
    }

    public boolean isInRoom(String user) {
        for (User u : users) {
            if (u.getLogin().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUsers() {
        return users;
    }
}
