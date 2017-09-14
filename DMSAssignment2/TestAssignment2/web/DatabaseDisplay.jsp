<%-- 
    Document   : DatabaseDisplay
    Created on : 20-Apr-2017, 01:48:16
    Author     : Gabby
--%>

<%@page import="java.util.ArrayList"%>
<%@ page import="java.sql.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% Class.forName("org.apache.derby.jdbc.ClientDriver"); %>

<html>
    <head>
        <title>Member Database Table </title>
    </head>

    <body bgcolor="lightblue" text="black">
    <link rel="stylesheet" type="text/css" href="style.css">
        <% 
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/DBDetail", "admins", "admins");

            Statement statement = connection.createStatement() ;
            ResultSet resultset = 
                statement.executeQuery("select * from members") ; 
        %>

        <table border="2" width="700px" align="center">
            <tr bgcolor="#cccccc">
                <td colspan=7 align="center" bgcolor= "#c7ded8">
                    <h1><b>Full Member Database Records</b></h1>
                </td>
            </tr>
        
            <tr bgcolor="#eeeeee">
                <td><h3><b>First Name</b></h3></td>
                <td><h3><b>Last Name</b></h3></td>
                <td><h3><b>Email</b></h3></td>
                <td><h3><b>User Name</b></h3></td>
                <td><h3><b>Password</b></h3></td>
                <td><h3><b>Gender</b></h3></td>
                <td><h3><b>Age</b></h3></td>
            </tr>
            
            <% while(resultset.next()){ %>
                <tr bgcolor="#fefefe">
                    <td> <%= resultset.getString(1) %></td>
                    <td> <%= resultset.getString(2) %></td>
                    <td> <%= resultset.getString(3) %></td>
                    <td> <%= resultset.getString(4) %></td>
                    <td> <%= resultset.getString(5) %></td>
                    <td> <%= resultset.getString(6) %></td>
                    <td> <%= resultset.getString(7) %></td>
                </tr>
            <% } %>
        </table>
        
        <center>
            <h2><a href="WelcomeUser.jsp">Back to Welcome Page</a></h2>
        </center>
    </body>
</html>
