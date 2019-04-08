package ua.kiev.prog;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetListServlet extends HttpServlet {

    private MessageList msgList = MessageList.getInstance();
    private UserList userList = UserList.getInstance();
    private RoomList roomList = RoomList.getInstance();
    private Gson gson = new Gson();
    private List<Message> newlist = new LinkedList<>();
    private List<Message> listAll = new LinkedList<>();
    private List<Message> roomMessages = new LinkedList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fromStr = req.getParameter("from");
        int from = 0;
        try {
            from = Integer.parseInt(fromStr);
            if (from < 0) from = 0;
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        resp.setContentType("application/json");
        String json = "";
        String to = req.getParameter("to");
        String ufrom = req.getParameter("ufrom");

        if (to != null) {
            try {
                if (roomList.getRoom(to)!=null) {

                   
                    for (int i = from; i < msgList.getList().size(); i++) {
                        if (msgList.getList().get(i).getTo().equals(roomList.getRoom(to).getName())&roomList.getRoom(to).isInRoom(ufrom)) {
                            roomMessages.add(msgList.getList().get(i));
                        }else{
//                            roomMessages.clear();
                        }
                    }
                    System.out.println(roomList.getRoom(to).getUsers());
                    json = gson.toJson(new JsonMessages(roomMessages, from));
                }else {
                    if (from > msgList.getList().size()) {
                        from = 0;
                    }
                    
                    for (int i = from; i < msgList.getList().size(); i++) {
                     
                        if ((msgList.getList().get(i).getTo().equals(to) &&
                                msgList.getList().get(i).getFrom().equals(ufrom)) || (msgList.getList().get(i).getTo().equals(ufrom) && msgList.getList().get(i).getFrom().equals(to))) {
                            if (!isMessagePrivatePresent(msgList.getList().get(i))) {
                       
                                newlist.add(msgList.getList().get(i));
                            }
                        } 
                    }
                    json = gson.toJson(new JsonMessages(newlist, from));
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            String login = req.getParameter("login");
            if (from > msgList.getList().size()) {
                from = 0;
            }
            for (int i = from; i < msgList.getList().size(); i++) {
                if ((msgList.getList().get(i).getTo().equals("all")) & !isMessagePresent(msgList.getList().get(i))) {
                    listAll.add(msgList.getList().get(i));
                }
            }
            json = gson.toJson(new JsonMessages(listAll, from));
        }

        System.out.println(json);
        if (json != null) {
            OutputStream os = resp.getOutputStream();
            byte[] buf = json.getBytes(StandardCharsets.UTF_8);
            os.write(buf);
        }
    }

    private boolean isMessagePresent(Message m) {
        if (!listAll.isEmpty()) {
            for (Message message : listAll) {
                if (message.getId() == m.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMessagePrivatePresent(Message m) {
        if (!newlist.isEmpty()) {
            for (Message message : newlist) {
                if (message.getId() == m.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

}
