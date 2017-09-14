/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassPack;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabby
 */
@WebServlet(name = "DatabaseServlet", urlPatterns = {"/DatabaseServlet"})
public class DatabaseServlet extends HttpServlet 
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String dbURL = "jdbc:derby://localhost:1527/DBDetail;";
    private String dbUser = "admins";
    private String dbPass = "admins";
    private String dbDriverURL;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");

        String firstName = request.getParameter("fname");
	String lastName = request.getParameter("lname");
	String email = request.getParameter("email");
        String username = request.getParameter("uname");
        String password = request.getParameter("pass");
        String gender = request.getParameter("gender");
        String age = request.getParameter("age");
				
	Connection conn = null;
	String message = null;	
        int row;
                
	try 
        {
            dbDriverURL = "org.apache.derby.jdbc.ClientDriver";
                    
            try 
            {
                Class.forName(dbDriverURL);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
		conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

		String sql = "INSERT INTO members (fname, lname, email, username, password, gender, age) "
                    + "values (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, firstName);
		statement.setString(2, lastName);
		statement.setString(3, email);
                statement.setString(4, username);
                statement.setString(5, password);
                statement.setString(6, gender);  
                statement.setString(7, age);  
                
		row = statement.executeUpdate();
                
		if (row > 0) 
                {
                    message = "File uploaded and saved into database";
		}
	} catch (SQLException ex) {
            message = "ERROR: " + ex.getMessage();
            ex.printStackTrace();
	} finally {
            if (conn != null) 
            {
                // closes the database connection
		try 
                {
                    conn.close();
		} catch (SQLException ex) {
                    ex.printStackTrace();
		}
            }
            
            request.setAttribute("RegSuccessful", message);
            getServletContext().getRequestDispatcher("/RegSuccessful.jsp").forward(request, response);
	}
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
