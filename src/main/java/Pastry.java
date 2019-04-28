import java.net.InetAddress;
import java.net.UnknownHostException;

public class Pastry
{
    public static String NodeId;
    private static Server pastryServer;
    private static UserInterface pastryUserInterface;

    public static void main(String[] args)
    {
        System.out.println("creating pastry node");
        if (args.length == 0)
        {
            System.out.println("ERROR : need port number as argument.. EXITING");
            return;
        }


        int port = Integer.parseInt(args[0]);


        NodeId = getId (port);


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

    private static String getId(int port)
    {

        InetAddress ip = null;
        try
        {
            ip = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {   System.err.println("ERROR : in getting ip address");        }


        String op = Md5.getHash(ip.toString()+port);
        return op;
    }
}