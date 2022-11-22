import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    private String ip;

    public Cliente(){
        this.ip = "10.0.0.1";
    }
    public String getIP(){
        return ip;
    }

    public static void main(String[] args) throws IOException {
        Cliente cliente = new Cliente();
        Logs log = new Logs();
        try {
            Socket s = new Socket("localhost", 4998);

            PrintWriter pr = new PrintWriter((s.getOutputStream()));
            String qu1 = "3874,Q+R,0,0,0,0;example.com.,MX;";
            pr.println("QE " + qu1);
            log.addToFile("QE "+cliente.getIP()+" "+qu1);
            pr.flush();


            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            String str = bf.readLine();
            System.out.println(str);
            log.addToFile("RP "+cliente.getIP()+" "+str);

        }catch (IOException e){
            System.out.println("!!!!Erro no cliente ao conector ao ServidorP!!!!");
            e.printStackTrace();
        }
        /*
        try {
            Socket s1 = new Socket("localhost", 4999);

            PrintWriter pr1 = new PrintWriter((s1.getOutputStream()));
            pr1.println("is it working");
            pr1.flush();

            InputStreamReader in1 = new InputStreamReader(s1.getInputStream());
            BufferedReader bf1 = new BufferedReader(in1);

            String str1 = bf1.readLine();
            System.out.println("serverS: " + str1);
        }catch (IOException e){
            System.out.println("!!!!Erro no cliente ao conectar ao ServidorS!!!!");
            e.printStackTrace();
        }
*/
    }
}
