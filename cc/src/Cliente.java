import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws IOException {
        try {
            Socket s = new Socket("localhost", 4998);

            PrintWriter pr = new PrintWriter((s.getOutputStream()));
            String qu1 = "3874,Q+R,0,0,0,0;example.com.,MX;";
            pr.println(qu1);
            pr.flush();

            int i =0;
            while(i<12) {
                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader bf = new BufferedReader(in);

                String str = bf.readLine();
                System.out.println("serverP: " + str);
                i++;
            }
        }catch (IOException exception){
            //controlo de erros
        }
        try {
            Socket s1 = new Socket("localhost", 4999);

            PrintWriter pr1 = new PrintWriter((s1.getOutputStream()));
            pr1.println("is it working");
            pr1.flush();

            InputStreamReader in1 = new InputStreamReader(s1.getInputStream());
            BufferedReader bf1 = new BufferedReader(in1);

            String str1 = bf1.readLine();
            System.out.println("serverS: " + str1);
        }catch (IOException exception){
            //controlo de erros
        }

    }
}
