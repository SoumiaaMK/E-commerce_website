package view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ViewProductServlet")
public class ViewProductServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        try {
            // Establish database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/website", "root", "");

            // Fetch data from the database
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product");
            ResultSet rs = pst.executeQuery();
            // Create a list to store product information
            List<Product> products = new ArrayList<>();

            // Iterate through the result set and add each product to the list
            while (rs.next()) {
                Product product = new Product();
                product.setPid(rs.getInt("pid")); // Set product ID
                product.setName(rs.getString("name"));
                product.setPrice(rs.getString("price"));
                product.setDetail(rs.getString("detail"));
                product.setCode(rs.getString("code"));
                product.setCategory(rs.getString("category"));
                product.setAvailability(rs.getString("availablity"));
                // Get the full image path from the database
                String fullPath = rs.getString("img_path");
                
                // Extract only the filename from the image path
                String filename = fullPath.substring(fullPath.lastIndexOf("/") + 1);
                
                // Set the filename as the image path in the Product object
                product.setImgPath(filename);
                
                products.add(product);
            }

            // Set the list of products as a request attribute
            request.setAttribute("products", products);

            // Forward the request to the JSP
            RequestDispatcher rd = request.getRequestDispatcher("viewproduct.jsp");
            rd.forward(request, response);

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle any SQL exceptions
            throw new ServletException("Error in database connection or query: " + ex.getMessage(), ex);
        } finally {
            // Connection cleanup
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward the request to AddToCartServlet
        RequestDispatcher rd = request.getRequestDispatcher("/AddToCartServlet");
        rd.forward(request, response);
    }
}
