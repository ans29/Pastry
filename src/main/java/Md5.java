import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5
{
    public static String getHash (String input)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(Helper.NodeIdBase);

            while (hashtext.length() < 32)
                hashtext = "0" + hashtext;


            return hashtext.substring(32- Helper.NodeIdDigitCount,32);
        }

        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }

    }
}
