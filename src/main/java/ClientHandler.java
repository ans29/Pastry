import java.io.*;
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



        //SAVE SERVER IPPORT ALONG WITH NODEID SO THAT IT CAN BE SHARED WITH OTHERS.
            Pastry.idServerIpPortInfo.put(clientNodeId,ipPort);



        //LET IT SOCIALIZE AND SEND VALUES FROM ROUTING TABLES
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

        if (rcvdMsg.startsWith("put"))          // put key value list like this
        {
            int space = rcvdMsg.indexOf (" ");
            rcvdMsg = rcvdMsg.substring (space+1);       // key val
            space = rcvdMsg.indexOf (" ");
            String keyVal = rcvdMsg.substring (0, space );
            String valVal = rcvdMsg.substring (space+1);
            String keyHash = Helper.getId (keyVal);
            System.out.println("Chandler :: Hash of key : " + keyHash);

            //if (Pastryget(keyHash).compareTo("") == 0 )
            //    return false;

            // if dist(key to nodeId) is smallest as comparedto 1. its Rt, 2. its leaf node, then put in its hash, else send put cmd to that one.

            String closerNodeId = Pastry.routingTable.getNodeId(keyHash);   // possibilities : 1.same as nodeId, 2.table=null, 3.closer
            System.out.println("UI :: closest nodeId in RT of this node is : " + closerNodeId);


            if (Helper.XcloserToA (keyHash, Pastry.NodeId, closerNodeId))
            //if (Math.abs (Helper.strCompare (Pastry.NodeId, keyHash)) < Math.abs (Helper.strCompare (closerNodeId, keyHash)))
            {

                System.out.println ("\t since closest in RT is not closer we'll check neighbours");


                if ((Pastry.leafSet.smallerId != null) && (Helper.XcloserToA (keyHash, Pastry.leafSet.smallerId, Pastry.NodeId)))
                //if (Math.abs (Helper.strCompare (Pastry.NodeId, keyHash)) > Math.abs (Helper.strCompare (Pastry.leafSet.smallerId, keyHash)))
                {
                    //send to smaller
                    System.out.println("\t smaller neighbour is closer... sending cmd to put key val pair there");
                    Helper.sendPutReqToId (Pastry.leafSet.smallerId, rcvdMsg);
                }
                if ((Pastry.leafSet.largerId != null) && (Helper.XcloserToA (keyHash, Pastry.leafSet.largerId, Pastry.NodeId)))
                //if (Math.abs (Helper.strCompare (Pastry.NodeId, keyHash)) > Math.abs (Helper.strCompare (Pastry.leafSet.largerId, keyHash)))
                {
                    //send to larger
                    System.out.println("\t larger neighbour is closer... sending cmd to put key val pair there");
                    Helper.sendPutReqToId (Pastry.leafSet.largerId, rcvdMsg);
                }

                System.out.println("\t both neighbours are not closer... saving pair here");
                Pastry.mainHashTable.put (keyHash, valVal);
            }

            // send to closerNodeId if not null
            if (closerNodeId != null)
            {
                System.out.println ("\t since closest in RT is closer, sending cmd to put key val pair there");
                Helper.sendPutReqToId (closerNodeId, rcvdMsg);
            }

            System.out.println ("\t saving pair here");
            Pastry.mainHashTable.put (keyHash, valVal);
        }

        if

        return true;  // change to false afterwards
    }
}