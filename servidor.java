import java.net.*;
import java.io.*;


public class servidor {
    
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket(4999);
        Socket s = ss.accept();

        System.out.println("cliente conectado");
    
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
    
        String str = bf.readLine();
        System.out.println("cliente: "+ str);

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("yes!!");
        pr.flush();
        
    }
}