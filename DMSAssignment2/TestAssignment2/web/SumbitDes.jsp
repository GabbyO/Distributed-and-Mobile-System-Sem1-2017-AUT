<%-- 
    Document   : SumbitDes
    Created on : 21-Apr-2017, 18:03:37
    Author     : Gabby
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sumbit Page</title>
    </head>
    <body bgcolor="lightblue" text="black">
        <link rel="stylesheet" type="text/css" href="style.css">
        <center>
            <form  method="post" action="SubmitServlet">

                <table border="2" width="31%" cellpadding="2" bgcolor="#fefefa">
                    <thead>
                        <tr bgcolor="#efe8e0">
                            <td colspan=2 align="center" bgcolor= "#c7ded8">
                                 <h1> <b> SUBMIT FORM <b> </h1>
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr bgcolor="#fefefa">
                            <td><b> User Name</b> </td>
                            <td><input type="text" name="uname" required="required" size="40" /></td>
                        </tr>
                        <tr bgcolor="#fefefa">
                            <td><b> Please enter description </b> </td>
                        
                            <td><BR>
                                <TEXTAREA NAME="textarea1" ROWS="10" cols="40" required="required"></TEXTAREA>
                            <BR></td>
                        </tr>
                        <tr bgcolor="#fefefa">
                            <td></td>
                            <td> 
                                <INPUT TYPE="SUBMIT" VALUE="Submit">
                            </td>  
                        </tr>
                    </tbody>
                </table>
            </form>
            <h2><a href="WelcomeUser.jsp">Back to Welcome Page</a></h2>
        </center>
    </body>
</html>
