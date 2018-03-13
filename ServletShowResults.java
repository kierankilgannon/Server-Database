// Fig. 9.27: SurveyServlet.java
// A Web-based survey that uses JDBC from a servlet.

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "ServletRecieve", urlPatterns = {"/show"})
public class ServletShowResults extends HttpServlet {
    private Connection connection;
    private PreparedStatement  read;
    String FIRST_NAME;
    String LAST_NAME;
    int AGE;
    String SEX;
    int INCOME;
    //String comments;
    int idNum;

    public void init( ServletConfig config )
            throws ServletException
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");  // load the driver
            String url = "jdbc:mysql://mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com:3306/TESTDB?autoReconnect=true&useSSL=false";
            connection = DriverManager.getConnection("jdbc:mysql://mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com:3306/TESTDB?autoReconnect=true&useSSL=false" ,"kieransDatabase","thisismypassword");
            // PreparedStatement to sum the votes
            //booksid = connection.prepareStatement("SELECT MAX(id) FROM book");
            read = connection.prepareStatement(
                    "SELECT  FIRST_NAME, LAST_NAME, AGE,  SEX, INCOME FROM EMPLOYEE");
        }
        catch ( Exception exception ) {
            exception.printStackTrace();
            //throw new UnavailableException( exception.getMessage() );
        }
    }

    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
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
            if(!resultSet.isBeforeFirst()){  //returns false when the result set is empty
                out.print("Database records may not be created!! Please try again");
                out.println("</pre></body></html>");
                out.close();
            }


            out.println("<title>Data found!</title>");
            out.println("</head>");

            out.println("<body>");

            while (resultSet.next()) {
                FIRST_NAME = resultSet.getString(1);
                LAST_NAME = resultSet.getString(2);
                AGE = resultSet.getInt(3);
                SEX = resultSet.getString(4);
                INCOME = resultSet.getInt(5);
                //copyright = resultSet.getString(6);
                //comments = resultSet.getString(7);
                //out.print("<br>"+ "---Result table:"+idNum +"<br/>");
               //<strong>Strong text</strong>
                //out.print("  <strong>"+" ID:"+"</strong>"+id);
                out.print("<br>"+" <strong>First Name:</strong>" + FIRST_NAME + "   ");
                out.print("  <strong>Last Name:</strong>" + LAST_NAME + "   ");
                out.print("   <strong>ISBN:</strong>" + AGE + "   ");
                out.print("   <strong>Title:</strong>" + SEX + "   ");
                out.print("   <strong>Copyright Number:</strong>" + INCOME + "   ");
                //out.print("   <strong>Comments:</strong>" + comments + "   ");
                out.print("<p></p>");

            }
            out.println("</pre></body></html>");
            out.close();
        }
        catch (SQLException sqlException) {
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
        try {
            connection.close();
        }

        // handle database exceptions by returning error to client
        catch( SQLException sqlException ) {
            sqlException.printStackTrace();
        }
    }
}
