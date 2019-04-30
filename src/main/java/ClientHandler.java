import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread
{
    String clientNodeId;
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

                System.out.println("C.HANDLER :: Received message : " + rcvdMsg);
                if (queryHandler (rcvdMsg) == false)
                    throw new IOException("C.HANDLER :: Undefined command");
            }
            catch (IOException e)
            {
                System.err.println("C.HANDLER :: closing connection");
                flagToCloseConnection = true;
            }
        }

        try
        {   in.close();     }
        catch (IOException e)
        {   e.printStackTrace();    }

        System.out.println ("C.HANDLER :: Connection terminated");
    }

    private boolean queryHandler(String rcvdMsg)
    {
        if(rcvdMsg.startsWith("Hello"))
        {
            String id = rcvdMsg.substring(11,15);
            int marker = rcvdMsg.indexOf(":");
            String ipPort = rcvdMsg.substring (marker+1);
            Client chandler = Helper.connect ("127.0.01 4321");


            Pastry.leafSet.addNode (id, chandler);
            Pastry.routingTable.insert (id, chandler);
            System.out.println("C.HANDLER :: Chandler connection added of Node " + id);
        }
        return true;  // change to false afterwards
    }
}