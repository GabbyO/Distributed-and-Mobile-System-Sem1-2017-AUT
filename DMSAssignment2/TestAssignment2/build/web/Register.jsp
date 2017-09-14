<%-- 
    Document   : Register
    Created on : 20-Apr-2017, 01:01:48
    Author     : Gabby
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>REGISTER Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body bgcolor="lightblue" text="black">
        <link rel="stylesheet" type="text/css" href="style.css">
        <form  method="post" action="DatabaseServlet">
            <center>
            <table border="2" width="31%" cellpadding="6" bgcolor="#fefefa">
                <thead>
                    <tr bgcolor="#efe8e0">
                        <td colspan=7 align="center" bgcolor= "#c7ded8">
                            <h1> <b> REGISTER FORM </b> </h1>
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <tr bgcolor="#fefefa">
                        <td><b> First Name</b> </td>
                        <td><input type="text" name="fname" required="required" size="40" /></td>
                    </tr>
                    
                    <tr bgcolor="#fefefa">
                        <td><b> Last Name</b> </td>
                        <td><input type="text" name="lname" required="required" size="40" /></td>
                    </tr>
                    
                    <tr bgcolor="#fefefa">
                        <td><b> Email</b> </td>
                        <td><input type="text" name="email" required="required" size="40" /></td>
                    </tr>
                    
                    <tr bgcolor="#fefefa">
                        <td><b> User Name</b> </td>
                        <td><input type="text" name="uname" required="required" size="40" /></td>
                    </tr>
                    
                    <tr bgcolor="#fefefa">
                        <td><b> Password</b> </td>
                        <td><input type="password" name="pass" required="required" size="40" /></td>
                    </tr>
                    
                    <tr bgcolor="#fefefa">
                        <td><b> Gender</b> </td>
                        <td> 
                            <INPUT TYPE="Radio" Name="gender" Value="Male" required="required"/> Male
                            <INPUT TYPE="Radio" Name="gender" Value="Female" required="required"/>Female
                        </td>
                    </tr>
                    
                    <tr bgcolor="#fefefa">
                        <td><b> Age</b> </td>
                        <td>
                            <input type="radio" name="age" value="15-19" required="required"/>15-19
                            <input type="radio" name="age" value="20-25" required="required"/>20-25
                            <input type="radio" name="age" value="26-30" required="required"/>26-30
                            <input type="radio" name="age" value="31-40" required="required"/>31-40
                            <input type="radio" name="age" value="41-50" required="required"/>41-50
                            <input type="radio" name="age" value="50-60" required="required"/>50-60
                            <input type="radio" name="age" value="over60" required="required"/>over 60
                        </td>
                    </tr>
                    <tr bgcolor="#fefefa">
                        <td></td>
                        <td>     
                            <input type="submit" value="Submit" />
                            <input type="reset" value="Reset" />
                        </td>  
                    </tr>
 
                    <tr bgcolor="#fefefa">
                        <td colspan="2">
                           <b> Already sign up?</b> 
                            <a href="Login.jsp"><b> Login Here!</b> </a>
                        </td>
                    </tr>
                </tbody>
            </table>
                <h2><a href="index.html">Back to Home Page</a></h2>
            </center>
        </form>
    </body>
</html>