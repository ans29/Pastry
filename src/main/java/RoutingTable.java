public class RoutingTable  // Matrix of digits x base connections
{
    public Client routeTable[][];       // Actual routes, connections
    public String routingTable[][];     // matrix of strings
    String nodeId;


    RoutingTable()
    {
        nodeId = Pastry.NodeId;
        routeTable = new Client [Constants.NodeIdDigitCount] [Constants.NodeIdBase];
        routingTable = new String [Constants.NodeIdDigitCount] [Constants.NodeIdBase];
    }

//HELPER FUNCTIONS
    private int getLcp (String s1, String s2)      //Longest common prefix length, but just for NodeIds
    {
        int ans = 0;
        for (int i=0; i<Constants.NodeIdDigitCount; i++)
            if (s1.charAt(i) == s2.charAt(i))   ans++;
            else break;
        return ans;
    }
    private int strCompare(String str1, String str2)
    {
        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);

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


// MAIN FUNCTIONS
    public boolean insert(String targetId, Client c)    //needs verification... ALL of them
    {
        // case 1: empty Rt,   //case 2: replacement
        // case 1: goes in Rt, //case 2: it is the same as rt, //case 3: LCP = 0
        // case 3 is DOubtTtTt: sloved... it'll be in 0th row, nice.

        if (targetId == nodeId) return false;
        int lcp = getLcp (targetId, nodeId);

        String initVal = routingTable [lcp][targetId.charAt(lcp)];
        if ((initVal == null) || (Math.abs (strCompare (initVal, nodeId)) > Math.abs (strCompare (targetId, nodeId))))
        // if ((there is no val) OR (if there is, then if old dist > new dist)) then write new val
        {
            routingTable [lcp][targetId.charAt(lcp)] = targetId;
            routeTable [lcp][targetId.charAt(lcp)] = c;
            return true;
        }

        return false;
    }

    public Client getNode (String targetId)
    {
        if (targetId == nodeId) return null;
        int lcp = getLcp (targetId, nodeId);
        return routeTable [lcp][targetId.charAt(lcp)];
    }

    public void delEntry (String targetId)
    {
        int lcp = getLcp (targetId, nodeId);
        routeTable [lcp][targetId.charAt(lcp)] = null;
        routingTable [lcp][targetId.charAt(lcp)] = null;
    }
}