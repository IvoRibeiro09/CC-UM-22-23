import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ServerS {
    private String db;
    private String ss;
    private String sp;
    private String dd;
    private String lg;
    private String lgall;
    private String st;

    public ServerS(){
        this.db = "";
        this.ss = "";
        this.sp = "";
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
    }
    public void setdb(String s){
        this.db=s;
    }
    public void setss(String s){
        this.ss=s;
    }
    public void setsp(String s){
        this.ss=s;
    }
    public void setdd(String s){
        this.dd=s;
    }
    public void setlg(String s){
        this.lg=s;
    }
    public void setlgall(String s){
        this.lgall=s;
    }
    public void setst(String s){
        this.st=s;
    }
    public void ParserSs(){
        try {
            File ficheiro = new File("testSS.txt");
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if(Objects.equals(linha[1], "DB")) {setdb(linha[2]);}
                else if(Objects.equals(linha[1], "SS")) {setss(linha[2]);}
                else if(Objects.equals(linha[1], "SP")) {setsp(linha[2]);}
                else if(Objects.equals(linha[1], "DD")) {setdd(linha[2]);}
                else if(Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {setlg(linha[2]);}
                else if(!Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {setlgall(linha[2]);}
                else if(Objects.equals(linha[1], "ST")) {setst(linha[2]);}
            }
        }catch (FileNotFoundException e) {
            System.out.println("!!!!Erro na funçao Parser do ServidorS!!!!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {

        try {
            ServerSocket ss = new ServerSocket(4999);
            while (true) {
                System.out.println("espera de conexão!!!!!!!!!!!!!!!!!!");
                Socket s = ss.accept();
                System.out.println("cliente conectado ao servidor secundario");

                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader bf = new BufferedReader(in);

                String str = bf.readLine();
                System.out.println("mensagem do cliente: " + str);

                PrintWriter pr = new PrintWriter(s.getOutputStream());
                pr.println("yes!!");
                pr.flush();
                s.close();
            }
        }catch (IOException e){
            System.out.println("!!!!Erro no servidor secundario!!!!");
            e.printStackTrace();
        }
    }
}
