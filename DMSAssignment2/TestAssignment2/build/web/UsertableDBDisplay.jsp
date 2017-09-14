<%-- 
    Document   : UsertableDBDisplay
    Created on : 21-Apr-2017, 18:05:43
    Author     : Gabby
--%>

<%@page import="java.util.ArrayList"%>
<%@ page import="java.sql.*" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% Class.forName("org.apache.derby.jdbc.ClientDriver"); %>

<html>
    <head>
        <title>Usertable Database Table </title>
    </head>

    <body bgcolor="lightblue" text="black">
        <link rel="stylesheet" type="text/css" href="style.css">
        
        <% 
            Connection connection = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/DBDetail", "admins", "admins");

            Statement statement = connection.createStatement() ;
            ResultSet resultset = 
                statement.executeQuery("select * from usertable") ; 
        %>

        <table border="2" width="700px" align="center">
            <tr bgcolor="#cccccc">
                <td colspan=7 align="center" bgcolor= "#c7ded8">
                    <h1><b>Full Username Database Records</b></h1>
                </td>
            </tr>
        
            <tr bgcolor="#eeeeee">
                <td><h3><b>Username</b></h3></td>
                <td><h3><b>Description</b></h3></td>
            </tr>
            
            <% while(resultset.next()){ %>
                <tr bgcolor="#fefefe">
                    <td> <%= resultset.getString(1) %></td>
                    <td> <%= resultset.getString(2) %></td>
                </tr>
            <% } %>
        </table>
        
        <center>
            <h2><a href="WelcomeUser.jsp">Back to Welcome Page</a></h2>
        </center>
    </body>
</html>