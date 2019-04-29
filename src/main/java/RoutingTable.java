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
    public boolean insert(String targetId, Client c)    //needs verification... ALL of them
    {
        // case 1: empty Rt,   //case 2: replacement
        // case 1: goes in Rt, //case 2: it is the same as rt, //case 3: LCP = 0
        // case 3 is DOubtTtTt: sloved... it'll be in 0th row, nice.

        if (targetId == nodeId) return false;
        int lcp = Helper.getLcp (targetId, nodeId);
        String initVal = routingTable [lcp][targetId.charAt(lcp)];


        if ((initVal == null) || (Math.abs (Helper.strCompare (initVal, nodeId)) > Math.abs (Helper.strCompare (targetId, nodeId))))
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
        int lcp = Helper.getLcp (targetId, nodeId);
        return routeTable [lcp][targetId.charAt(lcp)];
    }

    public void delEntry (String targetId)
    {
        int lcp = Helper.getLcp (targetId, nodeId);
        routeTable [lcp][targetId.charAt(lcp)] = null;
        routingTable [lcp][targetId.charAt(lcp)] = null;
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
}