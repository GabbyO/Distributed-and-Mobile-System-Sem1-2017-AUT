<%-- 
    Document   : LogOut
    Created on : 20-Apr-2017, 01:52:54
    Author     : Gabby
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    
    <body bgcolor="lightblue" text="black">
        <link rel="stylesheet" type="text/css" href="style.css">
            <center>
                <table border="1" width="500px" align="center">
                    <tr bgcolor="#eeeeee">
                        <td colspan="1" align="center" bgcolor= "#eeeeee">
                            <%
                                session.setAttribute("userid", null);
                                session.invalidate();
                                out.print("<center><h3><p style=\"color:red\">Log out is successful!!"
                                        + "</p></h3></center>");  
                            %>
                        </td>
                    </tr>
                </table>
                
                <table border="1" width="500px" align="center">
                    <tr bgcolor="#eeeeee">
                        <td colspan="1" align="center" bgcolor= "#eeeeee">
                            <h1><b>WELCOME GUESTS</b></h1>
                        </td>
                    </tr>
                </table>
                        
                <table border="1" width="500px" align="center">
                    <tr bgcolor="#eeeeee">
                        <td colspan="1" align="center" bgcolor= "#eeeeee">
                            <h3>Need to Sign up? <a href="Register.jsp">Register Here</a></h3>
                            <h3>Want login again? <a href="Login.jsp">Log In Here</a></h3>
                        </td>
                    </tr>
                </table>
                        
                    <h3><a href="index.html">Back to Home Page</a></h3>
                <table border="1" width="500px" align="center"></table>
            </center>
    </body>
</html>
