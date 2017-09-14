<%-- 
    Document   : WelcomeUser
    Created on : 20-Apr-2017, 01:17:02
    Author     : Gabby
--%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome <%=session.getAttribute("userid")%></title>
    </head>
    <body bgcolor="lightblue" text="black">
        <link rel="stylesheet" type="text/css" href="style.css">
        <center>
               
            <%
                out.print("<center><b><h2><p style=\"color:red\">User Login is successful!!"
                            + "</p></h2></b></center>"); 
            %>
            
            <table border="2" width="700px" align="center">
                <tr bgcolor="#eeeeee">
                        <td colspan=2 align="center" bgcolor= "#eeeeee">
                            <h1><b> Welcome, <%=session.getAttribute("userid")%>!</b></h1>

                            <h4>
                                Logging in
                                Date and Time
                            </h4>

                            <h4><%= new Date() %></h4>
                        </td>
                </tr>
            </table>
            <table border="1" width="700px" align="center">
                <tr bgcolor="#eeeeee">
                    <td colspan=2 align="center" bgcolor= "#eeeeee">
                    <h3>To see all member in Member Database!</h3> 
                    <h3><a href="DatabaseDisplay.jsp">Member Here!</a> <h3>

                    <h3>To search user - first name in the Member Database:-</h3>
                    <h3><a href="Search.jsp">Search here!</a> </h3>

                    <h3>To submit the textarea into Usertable Database! -</h3>
                    <h3><a href="SumbitDes.jsp">Submit here!</a> </h3>

                    <h3>To display the full Usertable Database -</h3>
                    <h3><a href="UsertableDBDisplay.jsp">Usertable here!</a> </h3>
                    
                    <h3>To display the full Usertable and Member Tables -</h3>
                    <h3><a href="Jointables.jsp">Tables here!</a> </h3>
                    </td>
                </tr>
            </table>
                <h2><a href="LogOut.jsp"><b>Log Out</b></a></h2>
        </center>
    </body>
</html>

