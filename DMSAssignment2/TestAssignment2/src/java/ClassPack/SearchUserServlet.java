package ClassPack;


import java.sql.*;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gabby
 */
@WebServlet(name = "SearchUserServlet", urlPatterns = {"/SearchUserServlet"})
 
public class SearchUserServlet extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html");
        
        Connection connect = null;
        String url = "jdbc:derby://localhost:1527/";
        String dbName = "DBDetail";
        String driver = "org.apache.derby.jdbc.ClientDriver";
        String username = "admins";
        String password = "admins";
 
        Statement state;
        
        try 
        {
            Class.forName(driver).newInstance();
            connect = DriverManager.getConnection(url + dbName, username, password);
            System.out.println("Connected!");
            String userid = request.getParameter("pid");
 
            ArrayList uArray = null;
            ArrayList userList = new ArrayList();
            String message = "select * from members where fname='" + userid + "' ";
 
            System.out.println("query " + message);
            state = connect.createStatement();
            ResultSet resultSet = state.executeQuery(message);
 
            while (resultSet.next()) 
            {
                uArray = new ArrayList();

                uArray.add(resultSet.getString(1));
                uArray.add(resultSet.getString(2));
                uArray.add(resultSet.getString(3));
                uArray.add(resultSet.getString(4));
                uArray.add(resultSet.getString(5));
                uArray.add(resultSet.getString(6));
                uArray.add(resultSet.getString(7));
                
                System.out.println("al :: " + uArray);
                userList.add(uArray);
            }
 
            request.setAttribute("piList", userList);
            RequestDispatcher view = request.getRequestDispatcher("/SearchDB.jsp");
            view.forward(request, response);
            connect.close();
            System.out.println("Disconnected!");
            
        } catch (Exception e) {
            e.printStackTrace();
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
