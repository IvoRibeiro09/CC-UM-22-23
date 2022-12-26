package cc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.*;

public class ServerTopo {
        private String db;

        private String st;
        private String lg;
        private String lgall;
        private final ServerSocket ssocket;
        //private final DatagramSocket dsocket;
        private byte[] buffer = new byte[550];

        //construtor vazio de um servidor principal
        public ServerTopo(ServerSocket ss) {
            this.st = "";
            this.db = "";
            this.lg = "";
            this.lgall = "";
            this.ssocket = ss;
        }

        //seters
        public void setdb(String s) {
            this.db = s;
        }
        public void setst(String s) {
        this.db = s;
    }
        public void setlg(String s) {
            this.lg = s;
        }

        public void setlgall(String s) {
            this.lgall = s;
        }


    public void ParserST(String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File(str);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if (Objects.equals(linha[1], "DB")) {
                    setdb(linha[2]);
                } else if (Objects.equals(linha[1], "ST")) {
                    setst(linha[2]);
                } else if (Objects.equals(linha[1], "LG")) {
                    setlg(linha[2]);
                }
            }
        } catch (FileNotFoundException e) {
            String[] aux = str.split("\\.");
           // log.addFL(aux[0], "!!!!Erro na leitura do ficheiro de configuraçao do Servidor de Topo!!!!");
            e.printStackTrace();
        }
    }
    public void STservidor(){

    }

    public static void main (String[] args) throws IOException{
            ServerSocket serverSocket = new ServerSocket(64321);

            ServerTopo St = new ServerTopo(serverSocket);
            String config = "STFile.txt";
            St.ParserST(config);

            St.STservidor();
    }
}
