<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <style>
        #results {
            font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }
        #results tr:nth-child(even){background-color: #f2f2f2;}
        #results th {
            background-color: #4CAF50;
            color: white;
        }



    </style>
<%
    int theID=0;
    try {
        String connectionURL = "jdbc:mysql://mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com:3306/TESTDB?autoReconnect=true&useSSL=false";
        Connection connection = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(connectionURL, "kieransDatabase", "thisismypassword");
        if(!connection.isClosed()) {
            //out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            PreparedStatement read;
            ResultSet rset;
            read = connection.prepareStatement("SELECT * FROM testResults WHERE ID =(SELECT MAX(ID)FROM testResults)");
            rset = read.executeQuery();
            %>
    <h1>Test Result:</h1>
    <table id = "results" border = "1" width = "100%">
        <tr>
            <th>ID</th>
            <th>Unit Number</th>
            <th>Trip Level(mA)</th>
            <th>Trip Time(ms)</th>
            <th>Result</th>
            <th>Date Tested</th>
        </tr>
            <%
    if (rset.next()) {
%>
            <tr>
        <td><%= rset.getInt("ID") %></td>
    <td><%= rset.getString("unitNum") %></td>
    <td><%= rset.getString("tripLevel") %></td>
    <td><%= rset.getString("tripTime") %></td>
    <td><%= rset.getString("result") %></td>
    <td><%= rset.getString("myDate") %></td>
    </tr>
<%connection.close();
}
}
else{
    out.println("No new test completed check RCD is latched!!");
}
}catch(Exception ex){
    out.println("Unable to connect to database"+ex);
}
%>