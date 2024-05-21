package view;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/AddToCartServlet")
@MultipartConfig
public class AddToCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/website", "root", "")) {
            // Get parameters from the request
            String username = request.getParameter("username");
            String name = request.getParameter("name");
            String price = request.getParameter("price");
            Part filePart = request.getPart("pdt_img");

            // Upload the file
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            // Dynamically create file path
            String filePath = getServletContext().getRealPath("/WEB-INF/uploads/") + "/" + fileName;

            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // Handle file upload errors
                throw new ServletException("Error uploading file: " + e.getMessage(), e);
            }

            // Insert data into the cart table
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO cart (username, name, price, img_path) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, username);
                ps.setString(2, name);
                ps.setString(3, price);
                ps.setString(4, filePath);
                int rows = ps.executeUpdate();

                // Check if insertion was successful
                if (rows > 0) {
                    // Retrieve data for the specific username from the cart table
                    try (PreparedStatement selectPs = con.prepareStatement("SELECT * FROM cart WHERE username = ?")) {
                        selectPs.setString(1, username);
                        try (ResultSet rs = selectPs.executeQuery()) {
                            // Forward the result set to the JSP for display
                            request.setAttribute("cartItems", rs);
                            RequestDispatcher rd = request.getRequestDispatcher("viewcart.jsp");
                            rd.forward(request, response);
                        }
                    }
                } else {
                    // Handle the case where insertion failed
                    response.sendRedirect("error.jsp");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
            response.sendRedirect("error.jsp");
        }
    }
}
