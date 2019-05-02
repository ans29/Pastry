import java.io.*;
import java.net.Socket;
import java.util.HashMap;

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
        //Pastry.idServerIpPortInfo =  new HashMap<>();
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



        //SAVE SERVER IPPORT ALONG WITH NODEID SO THAT IT CAN BE SHARED WITH OTHERS.
            Pastry.idServerIpPortInfo.put(clientNodeId,ipPort);



        //LET IT SOCIALIZE AND SEND VALUES FROM ROUTING TABLES  #toDo 2
        // terminating condition at client side for socializing, dist from chandler is minimum, as compared to any neighbour, id in routingTable
            //send the hashmap of server details, and routingTable, not routeTable

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput o = null;
            try
            {
                o = new ObjectOutputStream(bos);
                o.writeObject(Pastry.idServerIpPortInfo);
                o.flush();
                byte[] byteArr = bos.toByteArray();

                out.writeInt(byteArr.length);      // HASHMAP size sent to client
                out.flush();

                out.write(byteArr);             // HASHMAP sent to client
                out.flush();
            }
            catch (IOException e) {   e.printStackTrace();    }
            finally
            {
                try {   bos.close();    }
                catch (IOException ex) {}
            }



            System.out.println("C.HANDLER :: Chandler connection added of Node " + clientNodeId);
        }
        return true;  // change to false afterwards
    }
}