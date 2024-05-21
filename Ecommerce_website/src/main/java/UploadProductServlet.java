import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/UploadProductServlet")
@MultipartConfig
public class UploadProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        // Database operation
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/website", "root", "");

            String name = request.getParameter("name");
            String price = request.getParameter("price");
            String detail = request.getParameter("detail");
            String code = request.getParameter("code");
            String category = request.getParameter("category");
            String availablity = request.getParameter("availablity");

            Part filePart = request.getPart("pdt_img");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String filePath = "C:/Users/91978/eclipse-workspace/Ecommerce_website/src/main/webapp/" + fileName;

            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                out.println("File uploaded successfully.");
            } catch (IOException e) {
                out.println("Error uploading file: " + e.getMessage());
            }

            // Get username from parameter
            String username = request.getParameter("username");

            // Check if username is not null
            if (username != null) {
                // The rest of your code
                PreparedStatement ps = con.prepareStatement("INSERT INTO product (name, price, detail, code, category, availablity, img_path, uploaded_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, name);
                ps.setString(2, price);
                ps.setString(3, detail);
                ps.setString(4, code);
                ps.setString(5, category);
                ps.setString(6, availablity);
                ps.setString(7, filePath);
                ps.setString(8, username);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    // Redirect to vendor.jsp after successful upload
                    response.sendRedirect("vendor.jsp");
                } else {
                    out.println("<font color=red>Upload Failed<br>");
                    out.println("<a href=upload_product.html>Try again</a>");
                }
                con.close();
            } else {
                // Handle the case where 'username' is null
                out.println("<font color=red>Username is null<br>");
                out.println("<a href=upload_product.html>Try again</a>");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle database errors
            out.println("<font color=red>SQL error: " + e.getMessage() + "<br>");
            out.println("<a href=upload_product.html>Try again</a>");
        } catch (ServletException ea) {
            ea.printStackTrace();
            // Handle file upload errors
            out.println("<font color=red>Servlet error: " + ea.getMessage() + "<br>");
            out.println("<a href=upload_product.html>Try again</a>");
        }
    }
}
