import java.net.*;
import java.io.*;

public class WebServ {

    public static void main(String[] args)
    {
        new WebServ();
    }

    public WebServ()
    {
        try
        {
            ServerSocket ss = new ServerSocket(8080);
            while(true)
            {
                Socket cs = ss.accept();
                InputStreamReader isr = new InputStreamReader(cs.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                PrintWriter pw = new PrintWriter(cs.getOutputStream());
                String request = br.readLine();
                String firstReqLine = request;
                while(request.length() > 0)
                {
                    request = br.readLine();
                }
                String[] toks = firstReqLine.split(" ");
                String reqUrl = toks[1];
                System.out.println(reqUrl);
                System.out.println("");
                if((reqUrl.compareTo("/") == 0)||(reqUrl.compareTo("127.0.0.1:8080/") == 0))
                {
                    String html = "<HTML><HEAD><TITLE>home page</TITLE></HEAD><BODY><BR><b>Test button page</b><BR>";
                    html += "<form><input type=\"button\" value=\"Start Test\" onclick=\"";
                    html += "window.location.href='127.0.0.1:8080/button1.html'\" /></form>";
                    html += "<BR></BODY></HTML>";
                    String response = "HTTP/1.1 200 OK\r\n";
                    response += "host: localhost\r\n";
                    response += "Connection: close\r\n";
                    response += "Content-Length: " + html.length() + "\r\n\r\n";
                    response += html;
                    System.out.println(response);
                    pw.println(response);
                    pw.flush();
                }
                if(reqUrl.compareTo("/127.0.0.1:8080/button1.html") == 0)
                {
                    String testResult = "50";

                    String html = "<HTML><HEAD><TITLE>home page</TITLE></HEAD><BODY><BR><b>Test button page</b><BR>";
                    html += "<form><input type=\"button\" value=\"Go Back To Home Page\" onclick=\"";
                    html += "window.location.href='/'\" /></form>";
                    html += "<BR>Test result was: " + testResult + " ms";
                    html += "</BODY></HTML>";
                    String response = "HTTP/1.1 200 OK\r\n";
                    response += "host: localhost\r\n";
                    response += "Connection: close\r\n";
                    response += "Content-Length: " + html.length() + "\r\n\r\n";
                    response += html;
                    System.out.println(response);
                    pw.println(response);
                    pw.flush();
                }

            }
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
        }
    }
}