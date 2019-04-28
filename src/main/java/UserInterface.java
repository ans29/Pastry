import java.util.Scanner;

public class UserInterface implements Runnable
{
// private variables
    private boolean connectionState = false;         //true = connected to Pastry network
    Scanner keyboard = new Scanner(System.in);
    String userInput = "";
    Client clientOfServer = null;


// function declarations

    private Client connect(String ipPort)
    {
        int space = ipPort.indexOf(' ');
        String serverIp = ipPort.substring(0,space);
        int serverPort = Integer.parseInt(ipPort.substring(space+1));

        Client serverToConnect = new Client(serverIp, serverPort);
        return serverToConnect;
    }

    public boolean SendJoinReq(String userInput)
    {
        clientOfServer = connect(userInput.substring(5));
        if (clientOfServer != null)
            connectionState = true;
        return (clientOfServer.connectionState);
    }

    public boolean closeClient()
    {
        System.out.println("Exiting client part, ctrl+c to stop server");
        return false;
    }

    public boolean put(String p)
    {
        return false;
    }
    public int get(String s)
    {
        return -1;
    }


// Main run
    @Override
    public void run()
    {
        if (connectionState == false)
        {
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
            else
            {
                System.out.println("\nUI :: Pastry Node created with NodeId : "+ Pastry.NodeId +", ready to accept Key-val pair or hello from another node.");
            }
        }


        do
        {
            System.out.print("\nUI :: Enter cmd : ");
            userInput = keyboard.nextLine();
            if(userInput.startsWith("join"))        System.out.println(SendJoinReq(userInput));
            if(userInput.startsWith("node"))        System.out.println(getNodeId());
            if(userInput.startsWith("rout"))        System.out.println("Routetable");
            if(userInput.startsWith("hash"))        System.out.println("Hashtable");
            if(userInput.startsWith("put"))         put(userInput);
            if(userInput.startsWith("get"))         System.out.println (get(userInput));

        }while (!userInput.startsWith("exit"));

        closeClient();
    }

    private String getNodeId()
    {
        return Pastry.NodeId;
    }
}