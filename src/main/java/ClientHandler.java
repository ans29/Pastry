import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread
{
    private Socket socket = null;
    private DataInputStream in;
    private DataOutputStream out;


    ClientHandler(Socket sock, DataInputStream dis, DataOutputStream dos)
    {
        socket = sock;
        in = dis;
        out = dos;
    }

    @Override
    public void run()
    {
        String rcvdMsg = "";
        boolean flagToCloseConnection = false;
        while((!rcvdMsg.equalsIgnoreCase("exit")) && (flagToCloseConnection==false))
        {
            try
            {
                rcvdMsg = in.readUTF();
                System.out.println(rcvdMsg);
            } catch (IOException e)
            {
                System.err.println("CLIENT HANDLER :: closing connection");
                flagToCloseConnection = true;
            }
        }

        try
        {   in.close();     }
        catch (IOException e)
        {   e.printStackTrace();    }
    }
}