import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ServerS {
    private String name;
    private String db;
    private String ss;
    private String sp;
    private String dd;
    private String lg;
    private String lgall;
    private String st;
    private int refresh;

    public ServerS(){
        this.name = "";
        this.db = "";
        this.ss = "";
        this.sp = "";
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
        this.refresh = 0;
    }
    public void setname(String s){this.name= s;}
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

    public String getname(){
        return name;
    }

    public void ParserSs(String str){
        try {
            File ficheiro = new File(str);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if(Objects.equals(linha[1], "DB")) {setdb(linha[2]);setname(linha[0]);}
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
        ServerS servidor = new ServerS();
        servidor.ParserSs("testSS.txt");
        Logs log = new Logs();
        Cache cachess = new Cache();
        try {
            while(true) {

                if (servidor.refresh == 0) {
                    ServerSocket servers = new ServerSocket(4999);
                    Socket socket = new Socket("localHost", 4998);
                    PrintWriter pr = new PrintWriter((socket.getOutputStream()));
                    String qu1 = "domain: example.com";
                    pr.println(qu1);
                    log.addToFile("ZT " + qu1);
                    pr.flush();


                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader bf = new BufferedReader(in);
                    int count = Integer.parseInt(bf.readLine());

                    //confirmaçao
                    PrintWriter pr2 = new PrintWriter(socket.getOutputStream());
                    pr2.println("ok: " + count);
                    pr2.flush();

                    //receber todas as linhas
                    int i = 0;
                    while (i < count) {
                        Socket ns = servers.accept();
                        InputStreamReader in2 = new InputStreamReader(ns.getInputStream());
                        BufferedReader bf2 = new BufferedReader(in2);
                        String str = bf2.readLine();
                        System.out.println(i + ": " + str);
                        //cachess.ParserPorLinha(str);
                        i++;
                        ns.close();
                    }
                    servidor.refresh++;
                    socket.close();
                    System.out.println("espera de mova transferencia de zona!!!!!!!!!!!!!!!!!!");
                }
            }

        }catch (IOException e){
            System.out.println("!!!!Erro no servidor secundario!!!!");
            log.addToFile("EZ "+servidor.getname());
            e.printStackTrace();
        }
    }
}
