public class Helper
{
    public static final int NodeIdBase = 4;        //base of NodeIds, 16 for hex, 8 for octal
    public static final int NodeIdDigitCount = 4;   //No of digits in NodeIds

    public static int getLcp(String s1, String s2)      //Longest common prefix length, but just for NodeIds
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
}
