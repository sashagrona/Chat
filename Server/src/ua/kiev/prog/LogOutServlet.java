package ua.kiev.prog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "LogOutServlet", urlPatterns = "/logout")
public class LogOutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        setInDB(login);
        response.setStatus(400);
    }

    public void setInDB(String login) {
        try {
            Connection dbh = DriverManager.getConnection("jdbc:mysql://localhost/Chat"
                    + "?user=root&password=1234&characterEncoding=utf8");
            String query = "UPDATE Users SET status=false WHERE login='"+ login+"'";
            Statement statement = dbh.createStatement();
            statement.executeUpdate(query);
            dbh.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
