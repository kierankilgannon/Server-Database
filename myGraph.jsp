<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.ArrayList" %>
<html>
<head>
    <title>Connection with mysql database</title>
    <div id="chartContainer" style="height: 370px; width: 100%;"></div>
    <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
</head>
<body>
<h1>Connection status</h1>
<%
    try {
        String connectionURL = "jdbc:mysql://mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com:3306/TESTDB?autoReconnect=true&useSSL=false";
        Connection connection = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(connectionURL, "kieransDatabase", "thisismypassword");
        if(!connection.isClosed()) {
            //out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            Statement stmt = connection.createStatement();
            String sqlStr;

            ResultSet rset;

            ArrayList <Integer> arr = new ArrayList<Integer>();

            sqlStr = "SELECT  ID, unitNum, tripLevel, tripTime, result, myDate FROM testResults";
            rset = stmt.executeQuery(sqlStr);
            while (rset.next()) {
%>
                <%--<tr>--%>
                    <%--<td><%= rset.getInt("ID") %></td>--%>
                    <%--<td><%= rset.getString("unitNum") %></td>--%>
                    <%--<td><%= rset.getString("tripLevel") %></td>--%>
                    <%--<td><%= rset.getInt("tripTime") %></td>--%>

                    <%arr.add(rset.getInt("tripTime"));%>








              <%--</tr>--%>



<%          }
%>

<script>
    window.onload = function () {
        var t = <%=arr%>;
        alert(t);
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
                title: "Number of Companies"
            },
            data: [{
                type: "bar",
                name: "companies",
                axisYType: "secondary",
                color: "#0c1d65",
                dataPoints: [
                    { y: <%=arr.get(0)%>, label: "Sweden" },
                    { y: <%=arr.get(1)%>, label: "Taiwan" },
                    { y: <%=arr.get(2)%>, label: "Sweden" },
                    { y: <%=arr.get(3)%>, label: "Taiwan" },
                    { y: <%=arr.get(4)%>, label: "Sweden" },
                    { y: <%=arr.get(5)%>, label: "Taiwan" }

                ]
            }]
        });
        chart.render();

    }
</script>

<%
        }
        //connection.close();
    }catch(Exception ex){
        out.println("Unable to connect to database"+ex);
    }
%>

</body>
</html>