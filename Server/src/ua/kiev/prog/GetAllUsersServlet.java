package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

@WebServlet(name = "GetAllUsersServlet", urlPatterns = "/getAll")
public class GetAllUsersServlet extends HttpServlet {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final UserList userList = UserList.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String users = getFromDB();
        if (users != null) {
            OutputStream os = response.getOutputStream();
            byte[] buf = users.getBytes(StandardCharsets.UTF_8);
            os.write(buf);
        }
    }

    public String getFromDB() {
        try {
            Connection dbh = DriverManager.getConnection("jdbc:mysql://localhost/Chat"
                    + "?user=root&password=1234&characterEncoding=utf8");
            String query = "SELECT login,status FROM Users";
            Statement statement = dbh.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            String users = toJson(resultSet);
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized String toJson(ResultSet result) {
        try {
            String login = "";
            boolean status = true;
            JsonArray jArray = new JsonArray();
            while (result.next()) {
                login = result.getString("login");
                status = result.getBoolean("status");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("login", login);
                jsonObject.addProperty("status", status);

                jArray.add(jsonObject);
            }
            return gson.toJson(jArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
