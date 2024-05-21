import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import view.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ViewUploadedServlet")
public class ViewUploadedServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        try {
            // Establish database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/website", "root", "");
            String username = request.getParameter("username");

            // Fetch data from the database
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product WHERE uploaded_by=?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            // Create a list to store product information
            List<Product> products = new ArrayList<>();

            // Iterate through the result set and add each product to the list
            while (rs.next()) {
                Product product = new Product();
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
            RequestDispatcher rd = request.getRequestDispatcher("viewupload.jsp");
            rd.forward(request, response);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}