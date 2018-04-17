<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: Kieran
  Date: 17/04/2018
  Time: 17:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--<LI><B>date:</B>:--%>
        <%--<%= request.getParameter("date1") %>--%>
<LI><B>date</B>:
        <%= request.getParameter("date2") %>
</LI>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.ArrayList" %>
<html>
<style>
    #myProgress {
        width: 100%;
        background-color: #d8dbdd;
    }
    #myBar {
        width: 1%;
        height: 30px;
        background-color: #4CAF50;
    }
</style>
<head>
    <title>Connection with mysql database</title>
    <div id="chartContainer" style="height: 370px; width: 100%;"></div>
    <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
    <div id="myProgress">
        <div id="myBar"></div>
    </div>
</head>
<body>
<button onclick="move()">Start Test</button>
<%
 //String date2 = "2018-04-15 15:30:16%";
    //String date1 = "2018-03-16 15:30:16%";
    //char array[]='hws';
    String date1=request.getParameter("date1");
    String date2=request.getParameter("date2");
    date1.concat("00:00:01");
    date2.concat("23:59:59");
    try {
        String connectionURL = "jdbc:mysql://mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com:3306/TESTDB?autoReconnect=true&useSSL=false";
        Connection connection = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(connectionURL, "kieransDatabase", "thisismypassword");
        if(!connection.isClosed()) {
            //out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            Statement stmt = connection.createStatement();
            String sqlStr;
            PreparedStatement read;
            ResultSet rset;
            ArrayList <Integer> arr = new ArrayList<Integer>();
            ArrayList <String> uno = new ArrayList<String >();
            read = connection.prepareStatement(  "SELECT  ID, unitNum, tripLevel, tripTime, result, myDate FROM testResults WHERE (myDate BETWEEN ? AND ?)");
            //read = connection.prepareStatement(  "SELECT  ID, unitNum, tripLevel, tripTime, result, myDate FROM testResults WHERE myDate LIKE ?");
            read.setString(1,date1);
            read.setString(2,date2);
            System.out.println(date1+"yguvygvyuvuygvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            rset = read.executeQuery();
            //System.out.println(date1);
%>
<table border = "1" width = "100%">
    <tr>
        <th>ID</th>
        <th>Unit Number</th>
        <th>Trip Level</th>
        <th>Trip Time</th>
        <th>Result</th>
        <th>Date Tested</th>
    </tr>
        <%
    while (rset.next()) {
%>
    <tr>
        <td><%= rset.getInt("ID") %></td>
        <td><%= rset.getString("unitNum") %></td>
        <td><%= rset.getString("tripLevel") %></td>
        <td><%= rset.getString("tripTime") %></td>
        <td><%= rset.getString("result") %></td>
        <td><%= rset.getString("myDate") %></td>
    </tr>
        <%arr.add(rset.getInt("tripTime"));%>
        <%uno.add(rset.getString("unitNum"));%>

        <%          }
%>

    <script>
        window.onload = function () {
            var chart = new CanvasJS.Chart("chartContainer", {
                animationEnabled: true,
                title:{
                    text:"Unit Trip Times"
                },
                axisX:{
                    interval: 1
                },
                axisY2:{
                    interlacedColor: "rgba(1,77,101,.2)",
                    gridColor: "rgba(1,77,101,.1)",
                    title: "Time(ms)"
                },
                data: [{
                    type: "bar",
                    name: "trips",
                    axisYType: "secondary",
                    color: "#0c1d65",
                    dataPoints: [
                        <%for(int i=arr.size()-1;i>=0;i--){%>
                        {y: <%=arr.get(i)%>, label: <%=uno.get(i)%>},
                        <%}%>
                    ]
                }]
            });
            chart.render();
        }
    </script>

        <%connection.close();
        }
    }catch(Exception ex){
        out.println("Unable to connect to database"+ex);
    }
%>
    <script>
        function move() {
            var elem = document.getElementById("myBar");
            var width = 1;
            var id = setInterval(frame, 10);
            function frame() {
                if (width >= 100) {
                    clearInterval(id);
                    location.href='/show';
                } else {
                    width++;
                    elem.style.width = width + '%';
                }
            }
        }
        //document.getElementById("myBar").innerHTML = move();
    </script>
</body>
</html>

</body>
</html>
