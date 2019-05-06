public class RoutingTable  // Matrix of digits x base connections
{
    String nodeId;
    int rowCount, colCount;
    public Client routeTable[][];       // Actual routes, connections
    public String routingTable[][];     // matrix of strings


    RoutingTable()
    {
        rowCount = Helper.NodeIdDigitCount;
        colCount = Helper.NodeIdBase;
        nodeId = Pastry.NodeId;
        routeTable = new Client [rowCount] [colCount];
        routingTable = new String [rowCount] [colCount];
    }


// MAIN FUNCTIONS
    public boolean insert(String targetId, Client c)    //needs verification... ALL of them, UPDATE : checked but still not 100% sure
    {
        // case 1: empty Rt,   //case 2: replacement
        // case 1: goes in Rt, //case 2: it is the same as rt, //case 3: LCP = 0
        // case 3 is DOubtTtTt: solved... it'll be in 0th row, nice.

        if (targetId == nodeId) return false;
        int lcp = Helper.getLcpLength(targetId, nodeId);
        int colVal = 0;
        try
        {   colVal = targetId.charAt(lcp) - '0';    }
        catch (StringIndexOutOfBoundsException e)
        {
            System.out.println("C.HANDLER :: ERROR : trying to self connect. Aborting.");
        }

        String initVal = routingTable [lcp][colVal];


        if ((initVal == null) || (Math.abs (Helper.strCompare (initVal, nodeId)) < Math.abs (Helper.strCompare (targetId, nodeId))))
        // if ((there is no val) OR (if there is, then if old dist < new dist)) then write new val,
        // #IMPORTANT : its "<", because we want to save info of farthest node, faster for jumping.
        {
            routingTable [lcp][colVal] = targetId;
            routeTable [lcp][colVal] = c;
            return true;
        }

        return false;
    }

    public Client getNode (String targetId)
    {
        if (targetId == nodeId) return null;
        int lcp = Helper.getLcpLength(targetId, nodeId);
        return routeTable [lcp][targetId.charAt(lcp)];
    }


    public String getNodeId (String targetId)
    {
        if (targetId == nodeId) return null;
        int lcp = Helper.getLcpLength(targetId, nodeId);
        return routingTable [lcp][targetId.charAt(lcp)-'0'];
    }


    public void delEntry (String targetId)
    {
        int lcp = Helper.getLcpLength(targetId, nodeId);
        routeTable [lcp][targetId.charAt(lcp)] = null;
        routingTable [lcp][targetId.charAt(lcp)-'0'] = null;
    }

    public void showTable ()
    {
        System.out.println("\n\t\t Routing Table ");
        for (int i = 0; i < rowCount; i++)
        {
            System.out.print("\t\t");
            for (int j = 0; j < colCount; j++)
                System.out.print(routingTable[i][j] + " ");
            System.out.println();
        }
    }

    public boolean contains (String targetId)
    {
        if (targetId == nodeId)  return true;

        int lcp = Helper.getLcpLength(targetId, nodeId);
        int colVal = 0;
        colVal = targetId.charAt(lcp) - '0';

        String initVal = routingTable [lcp][colVal];
        if (targetId.compareTo(initVal) == 0)
            return true;

        return false;
    }
}