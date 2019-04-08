package ua.kiev.prog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "AddUserServlet", urlPatterns = "/signin")
public class AddUserServlet extends HttpServlet {
    private final UserList userList = UserList.getInstance();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        User user = new User(login, password, true);
        if(sendToDB(user, response)){
            userList.add(user);
            response.setStatus(200);
        }else{
            response.setStatus(400);
        }

    }

    private boolean sendToDB(User user, HttpServletResponse response) {
        try {
            Connection dbh = DriverManager.getConnection("jdbc:mysql://localhost/Chat"
                    + "?user=root&password=1234&characterEncoding=utf8");
            String insert = "INSERT INTO Users(login,password,status) VALUES(?,?,?)";
            String query = "SELECT login FROM Users WHERE login='" + user.getLogin() + "'";
            Statement statement = dbh.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return false;
            } else {
                PreparedStatement ps = dbh.prepareStatement(insert);
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getPassword());
                ps.setBoolean(3, user.isStatus());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
