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

        System.out.println ("C.HANDLER :: Connection terminated with node " + clientNodeId);
    }

    private boolean queryHandler(String rcvdMsg)
    {
        if(rcvdMsg.startsWith("Hello"))
        {
        //PING SERVER AND CONNECT TO A NEW CHANDLER OF CLIENT
            int marker = rcvdMsg.indexOf(":");
            clientNodeId = rcvdMsg.substring(11,15);    //Hello, I'm 4432, my server's ip and port are :10.42.0.1 4000
            String ipPort = rcvdMsg.substring (marker+1);
            Client chandler = Helper.connect (ipPort);

        //INITIALIZE TABLES
            Pastry.leafSet.addNode (clientNodeId, chandler);
            Pastry.routingTable.insert (clientNodeId, chandler);

        //SAVE SERVER IPPORT ALONG WITH NODEID SO THAT IT CAN BE SHARED WITH OTHERS. #toDo 1
        //SOCIALIZE AND GET VALUES FROM ITS TABLES  #toDo 2

            System.out.println("C.HANDLER :: Chandler connection added of Node " + clientNodeId);
        }
        return true;  // change to false afterwards
    }
}