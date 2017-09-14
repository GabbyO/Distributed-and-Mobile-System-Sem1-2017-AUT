/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassPack;

/**
 *
 * @author Gabby
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDao 
{
    public static boolean validate(String username, String pass) 
    {        
        
        Connection connect = null;
        PreparedStatement statementP = null;
        ResultSet resultSet = null;

        String url = "jdbc:derby://localhost:1527/";
        String dbName = "DBDetail";
        String driver = "org.apache.derby.jdbc.ClientDriver";
        String userName = "admins";
        String password = "admins";
        
        boolean check = false;
        
        try 
        {
            Class.forName(driver).newInstance();
            connect = DriverManager.getConnection(url + dbName, userName, password);

            statementP = connect.prepareStatement("select * from members where username=? and password=?");
            statementP.setString(1, username);
            statementP.setString(2, pass);

            resultSet = statementP.executeQuery();
            check = resultSet.next();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (connect != null) 
            {
                try 
                {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            if (statementP != null) 
            {
                try 
                {
                    statementP.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            if (resultSet != null) 
            {
                try 
                {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return check;
    }
}
