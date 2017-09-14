<%-- 
    Document   : Login
    Created on : 20-Apr-2017, 00:58:57
    Author     : Gabby
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>LOGIN Page</title>
    </head>
    
    <body bgcolor="lightblue" text="black">
        <link rel="stylesheet" type="text/css" href="style.css">
        <form method="post" action="LoginServlet">
            <center>
                <table border="2">
                    <thead>
                        <tr bgcolor="#efe8e0">
                            <td colspan=2 align="center" bgcolor= "#c7ded8">
                                <h1> <b> LOGIN FORM <b> </h1>
                            </td>
                        </tr>
                    </thead>
                <tbody>
                    <tr bgcolor="#fefefa">
                        <td><b> User Name</b> </td>
                        <td><input type="text" name="uname" required="required" size="40" /></td>
                    </tr>
                    
                    <tr bgcolor="#fefefa">
                        <td><b> Password</b> </td>
                        <td><input type="password" name="pass" required="required" size="40" /></td>
                    </tr>
                    
                    <tr bgcolor="#fefefa">
                        <td></td>
                        <td>     
                             <input type="submit" value="Login"/>
                             <input type="reset" value="Reset" /> 
                        </td>  
                    </tr>

                    <tr bgcolor="#fefefa">
                        <td colspan="2">
                            <b> Need to Sign up? </b> 
                            <a href="Register.jsp"><b> Register Here!</b> </a>
                        </td>
                    </tr>
                    
                </tbody>
            </table>
                <h2><a href="index.html">Back to Home Page</a></h2>
                
            </center>
        </form>
    </body>
</html>
