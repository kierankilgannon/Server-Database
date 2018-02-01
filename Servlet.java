
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "Servlet", urlPatterns = {"/html_form_send.php"})
public class Servlet extends HttpServlet {
    int count = 0;
    private Connection connection;
    private PreparedStatement insert, booksid;


    public void init( ServletConfig config )
            throws ServletException
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");  // load the driver
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/books" ,"root","root");
            booksid = connection.prepareStatement("SELECT MAX(id) FROM book");
        }
        catch ( Exception exception ) {
            exception.printStackTrace();
            throw new UnavailableException( exception.getMessage() );
        }
    }

    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response )
            throws ServletException, IOException
    {
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
        out.println( "<?xml version = \"1.0\"?>" );
        out.println( "<!DOCTYPE html PUBLIC \"-//W3C//DTD " +
                "XHTML 1.0 Strict//EN\" \"http://www.w3.org" +
                "/TR/xhtml1/DTD/xhtml1-strict.dtd\">" );
        out.println(
                "<html xmlns = \"http://www.w3.org/1999/xhtml\">" );
        out.println( "<head>" );
        String first_name =
                request.getParameter( "first_name" );
        String last_name =
                request.getParameter( "last_name" );
        String ISBN =
                request.getParameter( "ISBN" );
        String Title =
                request.getParameter( "Title" );
        String Copyright =
                request.getParameter( "Copyright" );
        String comments =
                request.getParameter( "comments" );
        int ID = 0;

        if (Copyright == ""){
            Copyright = "null";
        }
        if (first_name.isEmpty() || last_name.isEmpty()|| ISBN.isEmpty() || Title.isEmpty() || comments.isEmpty()){  //if any field with a * is empty

            try {

                out.print("<title>Failed entry!</title>");
                out.print("</head>");
                out.print("<body>");
                out.print("Please fill in the neccessary fields.");
                out.print("</pre></body></html>");
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                out.println("<title>Error</title>");
                out.println("</head>");
                out.println("<body><p>Database error occurred. ");
                out.println("Try again later.</p></body></html>");
                out.close();
            }
        }
        else{

            try {

                insert = connection.prepareStatement("Insert Into book Values (?,?,?,?,?,?,?)");
                ResultSet resultPersonID = booksid.executeQuery();
                if ( resultPersonID.next() ) {
                    ID = resultPersonID.getInt(1) + 1;
                    insert.setInt(1, ID);
                    insert.setString(2, first_name);
                    insert.setString(3, last_name);
                    insert.setString(4, ISBN);
                    insert.setString(5, Title);
                    insert.setString(6, Copyright);
                    insert.setString(7, comments);
                    insert.executeUpdate();
                }
                out.println("<title>Your data was entered correctly</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("Entry completed. ID num = " + ID);
                out.println("</pre></body></html>");
                out.close();
            }
            catch (SQLException sqlException) {
                sqlException.printStackTrace();
                out.println("<title>Error</title>");
                out.println("</head>");
                out.println("<body><p>Error data could not be saved in database. ");
                //out.println("Entry failed. ID num = " + ID);
                out.println("Try again later.</p></body></html>");
                out.close();
            }
        }

    }

    public void destroy()
    {
        try {
            insert.close();
            connection.close();
        }
        catch( SQLException sqlException ) {
            sqlException.printStackTrace();
        }
    }
}

