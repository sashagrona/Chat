package ua.kiev.prog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RoomServlet", urlPatterns = "/room")
public class RoomServlet extends HttpServlet {
    private RoomList roomList = RoomList.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String user = request.getParameter("user");
        if(user==null) {
            Room room = new Room(name);
            roomList.create(room);
        }
        if (user != null) {
            String exit = request.getParameter("exit");
            if(exit!=null){
                roomList.getRoom(name).removeUser(user);
            }else {
                roomList.getRoom(name).addUser(user);

            }
        }

    }
}
