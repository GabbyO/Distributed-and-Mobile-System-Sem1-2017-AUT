<%-- 
    Document   : Search
    Created on : 20-Apr-2017, 15:48:32
    Author     : Gabby
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
    </head>

    <body bgcolor="lightblue" text="black">
        <link rel="stylesheet" type="text/css" href="style.css">
            <form method="post" name="frm" action="SearchUserServlet">
                <center>
                    <table border="2">
                        <thead>
                            <tr bgcolor="#e7f3fc">
                                <td colspan=2 align="center">
                                    <h1><b>Search User</b></h1> 
                                </td>
                            </tr>
                        </thead>
                    <tbody>
                        <tr bgcolor="#fafdfa">
                    <td><b> First Name</b></td>
                            <td align="center"> 
                                <input type="text" name="pid" id="pid" required="required" size="40" />
                            </td>
                        </tr>   
                             
                        <tr bgcolor="#fafdfa">
                            <td colspan=2 align="center">
                                <input type="submit" name="submit" value="Search">
                            </td>
                        </tr>
                    </tbody>
                </table>
                </center>
            </form>
        <center>
            <h2><a href="WelcomeUser.jsp">Back to Welcome Page</a></h2>
        </center>
    
    </body>
</html>
