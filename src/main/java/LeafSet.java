public class LeafSet
{
    String smallerId=null, largerId=null;
    Client smallerNode=null, largerNode=null;

    public void addNode(String id, Client c)
    {
        int dist = Helper.strCompare (id, Pastry.NodeId);
        if (dist < 0)
        {
            if ((smallerId == null) || (dist*(-1) < Math.abs(Helper.strCompare(smallerId, Pastry.NodeId))))
            // if ((smallerId is null) OR if not then (new dist < older one))
            {
                smallerId = id;
                smallerNode = c;
            }

            // ELSE : call addNode of Node closest to this one according to Routetable
        }
        else if (dist > 0)
        {
            if ((largerId== null) || (dist*(-1) < Math.abs(Helper.strCompare(largerId, Pastry.NodeId))))
            {
                largerId = id;
                largerNode = c;
            }
        }
    }
}
