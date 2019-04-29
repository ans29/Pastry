import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable
{
    private Socket socket = null;
    private ServerSocket server = null;

    public Server(int port)
    {
        try
        {   server = new ServerSocket(port);    }
        catch (IOException e)
        {   System.err.println("SERVER :: EXCEPTION : cannot create server on this port, maybe already taken");    }
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                System.out.println("SERVER :: LISTENING FOR REQUEST(S)...");
                socket = server.accept();           // Now Listening for client
                System.out.println("SERVER :: ACCEPTED A REQUEST");

                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                Thread t = new ClientHandler(socket, in, out);  // create a new thread object of this type for each request
                t.start();

                //            socket.close();

            } catch (Exception e)
            {
                System.err.println("SERVER :: caught exception while accepting a request");
            }
        }
    }
}