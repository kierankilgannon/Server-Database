import java.io.*;
import java.net.*;

class WebServer     //class webserver
{
    int threadId = 0;
    public static void main(String[] args) {  //main method
        try
        {
            new WebServer(); // creates new instance of the class webserver
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public WebServer() throws IOException
    {
        ServerSocket ss = new ServerSocket(8080); //sets up socket at port 8080
        while(true)
        {
            Socket sc = ss.accept();  //accpets the socket
            new multiTreadServer(sc,threadId);  //makes a new instance of the class
            threadId++;
        }
    }
}

class multiTreadServer implements Runnable
{
    Socket clientConnection = null;
    int threadNo = 0;
    public multiTreadServer(Socket sc, int threadId)
    {
        clientConnection = sc; //makes the cc = sc passed from webserver
        threadNo = threadId;
        System.out.println("Thread No: " + threadNo);
        Thread thr = new Thread(this);
        thr.start();   //makes a new thread and starts it
    }

    public void run()  //runs the thread
    {
        processRequest(clientConnection);
    }


    void processRequest(Socket sc)
    {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(sc.getInputStream())); //input on the socket is read into an inputstream
            PrintWriter pw = new PrintWriter(sc.getOutputStream());
            OutputStream os = sc.getOutputStream();
            String firstRequestLine = br.readLine(); //takes first url line
            //br.readLine();
            String[] words = firstRequestLine.split(" ");
            String theUrl = words[1];     //element 0 is "get" element 1 is the html file
            int x = theUrl.indexOf('/');  //gets posistion of /
            theUrl = theUrl.substring(x+1);  //creates substring at the end of /
            String filename = "";
            if(theUrl.equals(null))
            {
                filename = "U:\\ComNetsWebServ\\myHTML.html";
            }
            else
            {
                filename = "U:\\ComNetsWebServ\\" + theUrl;
            }
//System.out.println(filename);
            FileInputStream fis = new FileInputStream(filename);
System.out.println(theUrl);
            if(theUrl.matches("(.*).jpg")==true){
                pw.println("HTTP/1.1 200 OK");  //writes to the browser
                pw.println("Content-Type: text/jpg");
                pw.println("Content-Length: "+fileLength(filename));
                pw.println("Connection: keep-alive");
                pw.println("");
                pw.flush();
                while(fis.available() > 0){
                    os.write(fis.read()); //read from file and write the contents to the server socket
                }
                os.flush();
            }

            else if(theUrl.matches("(.*).gif")==true){
                pw.println("HTTP/1.1 200 OK");  //writes to the browser
                pw.println("Content-Type: text/gif");
                pw.println("Content-Length: "+fileLength(filename));
                pw.println("Connection: keep-alive");
                pw.println("");
                pw.flush();
                while(fis.available() > 0){
                    os.write(fis.read()); //read from file and write the contents to the server socket
                }
                os.flush();
            }
        
            else
            {
                pw.println("HTTP/1.1 200 OK");
                pw.println("Content-Type: text/html");
                pw.println("Content-Length: "+fileLength(filename));
                pw.println("Connection: keep-alive");
                pw.println("");

                while(fis.available() > 0){  //while data is available from fis
                    char theByte = (char) fis.read(); //reads the file and casts to a char
                    pw.print(theByte);   //prints out the file
                }
                pw.flush();
            }

            sc.close();
            fis.close();
        }
        catch(Exception ee) {//ee.printStackTrace();
        }
    }

    static double fileLength(String fileDir) throws FileNotFoundException
    {
        File file = new File(fileDir);
        if(file.exists())
        {
            double bytes = file.length();
            return bytes;
        }
        else
            return 0;
    }
}