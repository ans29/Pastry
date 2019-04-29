import java.io.IOException;
import java.util.Scanner;

public class UserInterface implements Runnable
{
// private variables
    private boolean connectionState = false;         //true = connected to Pastry network
    Scanner keyboard = new Scanner(System.in);
    String userInput = "";



// function declarations


    public boolean SendJoinReq(String userInput)    //userInput = join 10.42.0.1 4000
    {
    // 1. get NodeId and Client for that Chandler
        String ipPort = userInput.substring(5);
        Client chandler = Helper.connect(ipPort);
        if (chandler != null)
            connectionState = true;

        String nodeIdToAdd = Helper.getId(ipPort);
        System.out.println("UI :: 1/3. Connected to Chandler of " + nodeIdToAdd);

    // 2. UPDATE TABLES
        Pastry.leafSet.addNode (nodeIdToAdd,chandler);
        Pastry.routingTable.insert (nodeIdToAdd,chandler);
        System.out.println("UI :: 2/3. Chandler connection added to Tables");

    // 3. send own NodeId, Chandler's ip and port
        try
        {
            chandler.out.writeUTF("Hello, I'm " + Pastry.NodeId + ", my server's ip and port are :" + Pastry.ip.toString() + " " + Pastry.port);           //send nodeId to ClientHandler
            chandler.out.flush();
            System.out.println("UI :: 3/3. sent Hello message to " + nodeIdToAdd);
        }
        catch (IOException e)
        {
            System.err.println("UI :: Exception in sending nodeId");
            e.printStackTrace();
        }

        return (chandler.connectionState);
    }




    public boolean closeClient()                    //may need extra work, working for now.
    {
        System.out.println("Exiting client part, ctrl+c to stop server");
        return false;
    }

    public boolean put(String p)
    {
        return false;
    }  //REMAINING
    public int get(String s)
    {
        return -1;
    }         //REMAINING




// Main run
    @Override
    public void run()
    {
        if (connectionState == false)
        {

            System.out.println("\nUI :: Pastry Node created with NodeId : "+ Pastry.NodeId);
            System.out.print("UI :: Create new pastry network? (0/1) : ");
            if (keyboard.nextInt() == 0)
            {

                keyboard = new Scanner(System.in);
                System.out.print("UI :: Enter IP and port to connect to : ");
                String ipPortInput = keyboard.nextLine();
                userInput = "join ";
                userInput = userInput.concat(ipPortInput);
                System.out.println(SendJoinReq(userInput));
            }
        }


        do
        {
            System.out.print("\nUI :: Enter cmd : ");
            userInput = keyboard.nextLine();
            if(userInput.startsWith("join"))        System.out.println(SendJoinReq(userInput));
            if(userInput.startsWith("node"))        System.out.println (Pastry.NodeId);
            if(userInput.startsWith("rout"))        Pastry.routingTable.showTable();
            if(userInput.startsWith("leaf"))        Pastry.leafSet.showLeafset();
            if(userInput.startsWith("hash"))        System.out.println("Hashtable");
            if(userInput.startsWith("put"))         put(userInput);
            if(userInput.startsWith("get"))         System.out.println (get(userInput));

        }while (!userInput.startsWith("exit"));

        closeClient();
    }
}