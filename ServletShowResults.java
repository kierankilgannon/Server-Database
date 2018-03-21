// Fig. 9.27: SurveyServlet.java
// A Web-based survey that uses JDBC from a servlet.

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "ServletRecieve", urlPatterns = {"/show"})
public class ServletShowResults extends HttpServlet {
    private Connection connection;
    private PreparedStatement  read;
    int ID;
    String unitNum;
    String tripLevel;
    String tripTime;
    String result;
    String myDate;
    int N = 10;




    public void init( ServletConfig config ) throws ServletException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");  // load the driver
            String url = "jdbc:mysql://mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com:3306/TESTDB?autoReconnect=true&useSSL=false";
            connection = DriverManager.getConnection("jdbc:mysql://mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com:3306/TESTDB?autoReconnect=true&useSSL=false" ,"kieransDatabase","thisismypassword");
            // PreparedStatement to sum the votes
            //booksid = connection.prepareStatement("SELECT MAX(id) FROM book");
            read = connection.prepareStatement(
                    "SELECT  ID, unitNum, tripLevel, tripTime, result, myDate FROM testResults");
        }
        catch ( Exception exception )
        {
            exception.printStackTrace();
            //throw new UnavailableException( exception.getMessage() );
        }
    }

    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        ArrayList<Integer> arrli = new ArrayList<Integer>(N);
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
        out.println( "<?xml version = \"1.0\"?>" );

        out.println( "<!DOCTYPE html PUBLIC \"-//W3C//DTD " +
                "XHTML 1.0 Strict//EN\" \"http://www.w3.org" +
                "/TR/xhtml1/DTD/xhtml1-strict.dtd\">" );

        out.println("<html xmlns = \"http://www.w3.org/1999/xhtml\">" );
        out.println( "<head>" );

        try {
            ResultSet resultSet = read.executeQuery();
            //boolean empty =resultSet.wasNull();
            if(!resultSet.isBeforeFirst())
            {  //returns false when the result set is empty
                out.print("Database records may not be created!! Please try again");
                out.println("</pre></body></html>");
                out.close();
            }


            out.println("<title>Data found!</title>");
            out.println("</head>");

            out.println("<body>");
            out.println("<table border = \"1\" width = \"100%\">\n" +
                    "                  <tr>\n" +
                    "                     <th>ID</th>\n" +
                    "                     <th>Unit no</th>\n" +
                    "                     <th>trip level</th>\n" +
                    "                     <th>Trip time</th>\n" +
                    "                     <th>Result</th>\n" +
                    "                     <th>Date</th>\n" +
                    "                  </tr></table>");

            while (resultSet.next())
            {  //"SELECT  ID, unitNum, tripLevel, tripTime, result, myDate FROM testResults");
                ID = resultSet.getInt(1);
                unitNum = resultSet.getString(2);
                tripLevel = resultSet.getString(3);
                tripTime = resultSet.getString(4);
                result = resultSet.getString(5);
                myDate = resultSet.getString(6);

                String myDate1[] = myDate.split(" ");
                arrli.add(Integer.parseInt((tripTime)));
                out.print("<table border = \"1\" width = \"100%\">\n");
                out.print("                     <tr><td>    " + ID + "  </td> \n");
                out.print("                     <td>       " + unitNum + " </td>  \n");
                out.print("                     <td>       "+ tripLevel + " </td>  \n");
                out.print("                     <td>" + tripTime + " </td>  \n");
                out.print("                     <td>" + result + "  </td> \n");
                out.print("                     <td>" + myDate1[0] + " </td> </tr> \n");
                out.print("</table>\n");

            }

            out.println("<script>\n" +
                    "        window.onload = function () {\n" +
                    "\n" +
                    "            var chart = new CanvasJS.Chart(\"chartContainer\", {\n" +
                    "                animationEnabled: true,\n" +
                    "\n" +
                    "                title:{\n" +
                    "                    text:\"Unit Trip Times\"\n" +
                    "                },\n" +
                    "                axisX:{\n" +
                    "                    interval: 1\n" +
                    "                },\n" +
                    "                axisY2:{\n" +
                    "                    interlacedColor: \"rgba(1,77,101,.2)\",\n" +
                    "                    gridColor: \"rgba(1,77,101,.1)\",\n" +
                    "                    title: \"Trip time in ms\"\n" +
                    "                },\n" +
                    "                data: [{\n" +
                    "                    type: \"bar\",\n" +
                    "                    name: \"companies\",\n" +
                    "                    axisYType: \"secondary\",\n" +
                    "                    color: \"#014D65\",\n" +
                    "                    dataPoints: [\n" +"");
                    for(int i = 0; i < arrli.size(); i++)
                    {
                        out.print("{ y: " + arrli.get(i) + ", label: \"UNIT NO.\" },\n" + "");
                    }
                    out.print(
                    "                    ]\n" +
                    "                }]\n" +
                    "            });\n" +
                    "            chart.render();\n" +
                    "\n" +
                    "        }\n" +
                    "    </script>");
            out.print("<p></p>");
            out.print("<div id=\"chartContainer\" style=\"height: 370px; width: 100%;\"></div>\n" +
                    "<script src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"></script>");
            out.println("</pre></body></html>");
            out.close();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            out.println("<title>Error</title>");
            out.println("</head>");
            out.println("<body><p>Database error occurred. ");
            out.println("Try again later.</p></body></html>");
            out.close();
        }
    }

    public void destroy()
    {
        try
        {
            connection.close();
        }

        // handle database exceptions by returning error to client
        catch( SQLException sqlException )
        {
            sqlException.printStackTrace();
        }
    }
}
