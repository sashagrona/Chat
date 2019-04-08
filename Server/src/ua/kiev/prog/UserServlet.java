package ua.kiev.prog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "UserServlet", urlPatterns = "/login")
public class UserServlet extends HttpServlet {
private final UserList userList = UserList.getInstance();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        User user = new User(login, password, true);
        if (checkInDB(user)) {
            setInDB(login);
            userList.add(user);
            response.setStatus(200);
        } else {
            response.setStatus(400);
        }
    }

    public boolean checkInDB(User user) {
        try {
            Connection dbh = DriverManager.getConnection("jdbc:mysql://localhost/Chat"
                    + "?user=root&password=1234&characterEncoding=utf8");
            String query = "SELECT login,password FROM Users";
            Statement statement = dbh.createStatement();
            ResultSet result = statement.executeQuery(query);
            String login = "";
            String password = "";
            while (result.next()) {
                login = result.getString("login");
                password = result.getString("password");
                if (login.equals(user.getLogin()) & password.equals(user.getPassword())) {

                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void setInDB(String login) {
        try {
            Connection dbh = DriverManager.getConnection("jdbc:mysql://localhost/Chat"
                    + "?user=root&password=1234&characterEncoding=utf8");
            String query = "UPDATE Users SET status=true WHERE login='" + login + "'";
            Statement statement = dbh.createStatement();
            statement.executeUpdate(query);
            dbh.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
