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

    public static void main(String[] args) throws IOException, java.net.UnknownHostException {
        Cliente cliente = new Cliente();
        Logs log = new Logs();

        String servernamesp = "10.0.16.11";
        String servernamess = "10.0.14.10";

        try {
            //Socket s = new Socket("localhost", 12346);//para o servidor ss no ide
            //Socket s = new Socket("localhost", 12345);//para o servidor sp no ide
            //Socket s = new Socket(servernamesp, 12345);//para o servidor sp no core
            Socket s = new Socket(servernamess, 12346);//para o servidor ss no core


            PrintWriter pr = new PrintWriter((s.getOutputStream()));
            String qu1 = "3874,Q+R,0,0,0,0;robalo.moc.,MX;";
            pr.println("QE " + qu1);
            System.out.println("cliente enviou: " + qu1);
            log.addQE(cliente.getIP(), qu1);
            pr.flush();



            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            String str = bf.readLine();
            System.out.println("cliente recebeu: " + str);
            log.addRR(cliente.getIP(), str);

        }catch (IOException e){
            log.addFL(cliente.getIP(), "!!!!Erro no cliente ao conector ao ServidorP!!!!");
            e.printStackTrace();
        }

    }
}
