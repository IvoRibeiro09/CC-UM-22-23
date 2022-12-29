package cc;

import com.sun.jdi.request.ThreadStartRequest;

import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerTopo {
        private HashMap<String,String> Aips;
        private final ServerSocket ssocket;
        //private final DatagramSocket dsocket;
        private byte[] buffer = new byte[550];

        //construtor vazio de um servidor principal
        public ServerTopo(ServerSocket ss) {
            this.Aips = new HashMap<>();
            this.ssocket = ss;
        }

        //seters
        public void setAips(String key,String value) {
            this.Aips.put(key,value);
        }

    public void ParserST(String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File(str);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if (Objects.equals(linha[1], "A")) {
                    setAips(linha[0],linha[2]);
                }
            }
        } catch (FileNotFoundException e) {
            String[] aux = str.split("\\.");
           // log.addFL(aux[0], "!!!!Erro na leitura do ficheiro de configuraçao do Servidor de Topo!!!!");
            e.printStackTrace();
        }
    }
    public void STservidor(){
        while(true){
            System.out.println("St recebeu alguma query");
            try {
                Socket socket = ssocket.accept();
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                String query = in.readUTF();
                System.out.println(query);
                //String resposta;
                if(iterative(query) == 0){
                    String ip = Aips.get("sp."+getdomain(query));
                    out.writeUTF("smaller");
                    out.writeUTF(ip);
                }else {
                    String ip = "";
                    if (isSub(query) == 2) {
                        Socket s2 = new Socket(Aips.get("sp."+getdomain(query)), 12345); //ter um getip invez do localhost e uma porta no ficheiro
                        DataInputStream in2 = new DataInputStream(s2.getInputStream());
                        DataOutputStream out2 = new DataOutputStream(s2.getOutputStream());

                        out2.writeUTF("smaller");
                        ip = in2.readUTF(); //ip do sp do subdominio
                    } else {
                        ip = Aips.get("sp."+getdomain(query));
                    }
                    //enviar a query ao sp do dominio certo
                    Socket s3 = new Socket(ip, 12345);
                    DataInputStream in3 = new DataInputStream(s3.getInputStream());
                    DataOutputStream out3 = new DataOutputStream(s3.getOutputStream());
                    out3.writeUTF(query);
                    String resposta = in3.readUTF();
                    out.writeUTF(resposta);//enviar a resposta
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public int isSub(String query){
            String[] aux = query.split(";");
            String[] aux2 = aux[1].split(",");
            int c = 0;
            for(int i =0;i < aux2[0].length();i++){
                if(aux2[0].charAt(i)=='.'){
                    c++;
                }
            }
            return c;
    }
    public String getdomain(String query){
        String[] aux = query.split(";");
        String[] aux2 = aux[1].split(",");
        String[] aux3 = aux2[0].split("\\.");
        return aux3[0]+".";
    }
    public int iterative(String query){
        String[] aux = query.split(";");
        String[] aux2 = aux[0].split(",");
        for(int i=0;i < aux2[0].length();i++){
            if(aux2[0].charAt(i)=='R'){
                return 1;
            }
        }
        return 0;
    }
    public static int getPorta(String IP){
            String[] aux = IP.split("\\.");
            return Integer.parseInt(aux[2])*100 + Integer.parseInt(aux[3]);
    }
    public static void main (String[] args) throws IOException{
            String Ip = InetAddress.getLocalHost().getHostAddress();
            ServerSocket serverSocket = new ServerSocket(64321);
            ServerTopo St = new ServerTopo(serverSocket);
            String config = "STFile.txt";
            St.ParserST(config);

            St.STservidor();
    }
}
