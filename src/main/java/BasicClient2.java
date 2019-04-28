import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BasicClient2
{
    public static void main(String[] args)
    {
        try
        {
            Socket s=new Socket("localhost",1234);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            Scanner sc = new Scanner(System.in);
            String input = " ";

            do
            {
                System.out.println("Enter cmd : ");
                input = sc.nextLine();
                dout.writeUTF(input);
                dout.flush();
            }while (!input.equalsIgnoreCase("exit"));

            dout.close();
            s.close();
        }
        catch(Exception e)
        {System.out.println(e);}
    }
}
