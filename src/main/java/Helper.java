import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper
{
    public static final int NodeIdBase = 4;        //base of NodeIds, 16 for hex, 8 for octal
    public static final int NodeIdDigitCount = 4;   //No of digits in NodeIds



    public static String getId(String ip, int port)
    {
        return getId(ip + " " +port);
    }


    public static String getId(String ipPort)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest (ipPort.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString (NodeIdBase);

            while (hashtext.length() < 32)
                hashtext = "0" + hashtext;

            return hashtext.substring(32- NodeIdDigitCount,32);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }



    public static void sendPutReqToId(String NodeId, String userInput)
    {
        Client chandler = Helper.connect (Pastry.idServerIpPortInfo.get (NodeId));
        try
        {
            chandler.out.writeUTF (userInput);
            chandler.out.flush();
          //  return chandler.in.readBoolean();
        }
        catch (IOException e)
        {   e.printStackTrace();    }
        //return false;
    }



    public static Client connect(String ipPort)           //creates a connection object with data-streams : done
    {
        int space = ipPort.indexOf(" ");
        String serverIp = ipPort.substring(0,space);
        int serverPort = Integer.parseInt(ipPort.substring(space+1));

        Client clientOfServer = new Client(serverIp, serverPort);
        return clientOfServer;
    }



    public static int getLcpLength(String s1, String s2)      //Longest common prefix length, but just for NodeIds
    {
        int ans = 0;
        for (int i = 0; i< Helper.NodeIdDigitCount; i++)
            if (s1.charAt(i) == s2.charAt(i))   ans++;
            else break;
        return ans;
    }




    public static int strCompare(String str1, String str2)
    {
        int l1, l2, lmin;
        if (str1==null)     l1=0;
        else    l1 = str1.length();
        if (str2 == null)   l2=0;
        else    l2 = str2.length();
        lmin = Math.min(l1, l2);

        for (int i = 0; i < lmin; i++)
        {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);

            if (str1_ch != str2_ch)
                return str1_ch - str2_ch;
        }

        if (l1 != l2)
            return l1 - l2;
        else
            return 0;
    }

    public static boolean XcloserToA (String x, String a, String b)
    {

        if (a==null)
            return false;
        if (b==null)
            return true;

        int n = Integer.parseInt(x);
        int n1 = Integer.parseInt(a);
        int n2 = Integer.parseInt(b);

        if (Math.abs(n1-n) <= Math.abs(n2-n))
            return true;

        return false;
    }
}
