import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Pastry
{
    public static int port;
    public static String ip = null;
    protected static String NodeId;
    private static Server pastryServer;
    private static UserInterface pastryUserInterface;
    public static LeafSet leafSet;
    public static RoutingTable routingTable;

    public static void main(String[] args)
    {
        System.out.println("creating pastry node");
        if (args.length == 0)
        {
            System.out.print("Enter port no : ");
            Scanner sc = new Scanner(System.in);
            port = sc.nextInt();
        }
        else  port = Integer.parseInt(args[0]);


        NodeId = getOwnId (port);
        routingTable = new RoutingTable();
        leafSet = new LeafSet();


        pastryServer = new Server  (port);
        Thread serverThread = new Thread(pastryServer);
        serverThread.start();


        pastryUserInterface = new UserInterface();
        Thread clientThread = new Thread(pastryUserInterface);
        clientThread.start();


        try
        {
            clientThread.join();
            serverThread.join();
        }
        catch (InterruptedException e)
        {   e.printStackTrace();        }

    }


    public static String getOwnId(int port)
    {
        InetAddress IP = null;
        try
        {
            IP = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {   System.err.println("ERROR : in getting ip address");        }

        int slash = IP.toString().indexOf("/");
        ip = IP.toString().substring(slash+1);
        return Helper.getId (ip, port);
    }
}