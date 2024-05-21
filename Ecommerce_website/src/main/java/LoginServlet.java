import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
      
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/website", "root", "");
            String n = request.getParameter("uname");
            String p = request.getParameter("upass");

            // Hash the entered password
            String hashedPassword = hashPassword(p);
            PreparedStatement ps = con.prepareStatement("SELECT username, user_type FROM user WHERE username=? AND password=?");
            ps.setString(1, n);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String userType = rs.getString("user_type");
                if ("vendor".equals(userType)) {
                	request.setAttribute("login_status", true);
                    RequestDispatcher rd = request.getRequestDispatcher("vendor.jsp");
                    request.setAttribute("urname", n);
                    rd.forward(request, response);
                } else if ("customer".equals(userType)) {
                	request.setAttribute("login_status", true);
                    RequestDispatcher rd = request.getRequestDispatcher("customer.jsp");
                    request.setAttribute("urname", n);
                    rd.forward(request, response);
                } else {
                	request.setAttribute("login_status", false);
                    out.println("<font color=red>Unknown user type<br>");
                }
            } else {
                out.println("<font color=red>Login Failed<br>");
                out.println("<a href=login.html>Try again</a>");
            }
        } catch (ClassNotFoundException | SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // Method to hash the password using SHA-256
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : encodedHash) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
