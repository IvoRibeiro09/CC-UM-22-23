package cc.src;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerError;
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
    //construtor inicial
    public ServerS(){
        this.name = "10.1.1.0";
        this.db = "";
        this.ss = "";
        this.sp = "";
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
    }
    //seters
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

    //geters
    public String getname(){
        return name;
    }
    public String getSS(){return ss;}

    //funçao que le o ficheiro de config do servidor secundario
    public void ParserSs(String str) throws IOException {
        Logs log = new Logs();
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
            String[] aux = str.split("\\.");
            log.addFL(aux[0],"!!!!Erro na leitura do ficheiro de configuraçáo do Servidor Secundario!!!!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Logs log = new Logs();
        ServerS servidor = new ServerS();
        String configfile = "testSS.txt";
        servidor.ParserSs(configfile);
        log.addEV("config",configfile);//leu o ficheiro de configuração
        Cache cachess = new Cache();
        cachess.ParserCacheServer(servidor.getname());
        log.addEV("bd",servidor.getname()+".db");//leu o ficheiro de base de dados
        //conexoes
        ServerSocket servercliente = new ServerSocket(4997);
        ServerSocket servers = new ServerSocket(4999);

        try{
            System.out.println("fiz transferencia de zona");
            Socket socket = new Socket("localhost",4998);
            PrintWriter pr = new PrintWriter((socket.getOutputStream()));
            String domain = "example.com";
            String qu1 = "domain: "+domain;
            pr.println(qu1);
            pr.flush();

            //envio mensagem a pedir transferencia de zona ao servidor primario
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            int count = Integer.parseInt(bf.readLine());


            //confirmaçao do numero de linhas a receber
            PrintWriter pr2 = new PrintWriter(socket.getOutputStream());
            pr2.println("ok: " + count);
            pr2.flush();

            //receber todas as linhas
            int i = 0;
            while (i < count) {
                Socket sockets = servers.accept();
                InputStreamReader in2 = new InputStreamReader(sockets.getInputStream());
                BufferedReader bf2 = new BufferedReader(in2);
                String str = bf2.readLine();
                cachess.ParserPorLinha(str,domain+".");
                i++;
                socket.close();
            }
            log.addZT(InetAddress.getLocalHost().getHostAddress(), "SS");
            System.out.println("espera de mova transferencia de zona!!!!!!!!!!!!!!!!!!");
        }catch (IOException e){
            System.out.println("!!!!Erro no servidor secundario!!!!");
            log.addEZ(InetAddress.getLocalHost().getHostAddress(),"SS");
            e.printStackTrace();
        }
        try {
            while(true) {
                System.out.println("espera de conexão de um cliente!!!!!!!!!!!!!!!!!!");

                Socket s = servercliente.accept();
                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader bf = new BufferedReader(in);
                String str = bf.readLine();
                String[] aux = str.split(" ");

                //cliente
                if (Objects.equals(aux[0], "QE")) {
                    System.out.println("cliente conectado ao server primario");

                    System.out.println("mensagem do cliente: " + aux[1]);
                    log.addQR(servidor.getSS() ,aux[1]);

                    Query q = new Query();
                    String querydone = q.doQuery(aux[1], cachess);
                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                    pr.println(querydone);
                    pr.flush();
                    log.addRP(servidor.getSS() , querydone);
                    s.close();
                }
            }
        }catch (Exception e){
            System.out.println("!!!!Erro no ServidorP!!!!");
            log.addFL(servidor.getSS(),"comunicação entre cliente e servidor primario");
            e.printStackTrace();
        }

    }
}
