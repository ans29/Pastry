import java.io.*;
import java.net.Socket;

public class Client
{
    String nodeIdServer;
    boolean connectionState;
    DataInputStream in = null;
    DataOutputStream out = null;

    Client(String IP, int port)
    {
        connectionState = false;
        try
        {
            Socket socket = new Socket(IP, port);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            connectionState = true;
        }
        catch (IOException e)
        {
            System.err.println("CLIENT :: caught exception while creating connection with server");
        }
    }



    /*
     input = sc.nextLine();
            dout.writeUTF(input);
            dout.flush();
        }while (!input.equalsIgnoreCase("exit"));

    */
}
