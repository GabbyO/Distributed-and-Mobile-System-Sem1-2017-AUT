<%-- 
    Document   : SearchDB
    Created on : 20-Apr-2017, 15:46:48
    Author     : Gabby
--%>
<%@ page import="java.util.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    </head>
    <body bgcolor="lightblue" text="black">
        <link rel="stylesheet" type="text/css" href="style.css">
        <table border="2" width="700px" align="center">
            <tr>
                <td colspan=7 align="center" bgcolor= "#c7ded8">
                    <h1><b>User Member Records</b></h1>
                </td>
            </tr>
            
            <tr bgcolor="#eeeeee">
                <td><h4><b>First Name</b></h4></td>
                <td><h4><b>Last Name</b></h4></td>
                <td><h4><b>Email</b></h4></td>
                <td><h4><b>User Name</b></h4></td>
                <td><h4><b>Password</b></h4></td>
                <td><h4><b>Gender</b></h4></td>
                <td><h4><b>Age</b></h4></td>
            </tr>
            <%
                int count = 0;
                String color = "#fefefe";
                if (request.getAttribute("piList") != null) {
                    ArrayList al = (ArrayList) request.getAttribute("piList");
                    System.out.println(al);
                    Iterator itr = al.iterator();
                    while (itr.hasNext()) {
 
                        if ((count % 2) == 0) {
                            color = "#fefefe";
                        }
                        count++;
                        ArrayList pList = (ArrayList) itr.next();
            %>
            
            <tr bgcolor="#fbfbf8">
                <td><%=pList.get(0)%></td>
                <td><%=pList.get(1)%></td>
                <td><%=pList.get(2)%></td>
                <td><%=pList.get(3)%></td>
                <td><%=pList.get(4)%></td>
                <td><%=pList.get(5)%></td>
                <td><%=pList.get(6)%></td>
            </tr>
            <%
                    }
                }
                if (count == 0) 
                {
                    %>
                    <tr>
                        <td colspan=7 align="center" bgcolor="#fefefe">
                            <b>No Record Found...</b>
                        </td>
                    </tr>
                    <%            
                }
            %>
        </table>
        <center>
            <h3><a href="Search.jsp">Search Again</a></h2>
            <h2><a href="WelcomeUser.jsp">Back to Welcome Page</a></h2>
        </center>
    </body>
</html>
