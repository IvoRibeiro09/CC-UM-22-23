package cc.src;

import javax.management.StringValueExp;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SeekableByteChannel;
import java.util.*;

import static java.lang.Thread.sleep;

public class ServerP {
    private String name;
    private String db;
    private List<String> ss;
    private String dd;
    private String lg;
    private String lgall;
    private String st;

    //construtor vazio de um servidor principal
    public ServerP(){
        this.name = "";
        this.db = "";
        this.ss =  new ArrayList<>();
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
    }

    //seters
    public void setname(String s){this.name=s;}
    public void setdb(String s){
        this.db=s;
    }
    public void setss(String s){
        this.ss.add(s);
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
    public String getname(){return name;}
    public String getSS(){
        return ss.get(0);
    }

    //funçao que interpreta o fichiero de configuraçao do servidor Principal e guarda essa informaçao
    public void ParserSp( String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File("test.txt");
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if(Objects.equals(linha[1], "DB")) {
                    setdb(linha[2]);
                    setname(linha[0]);
                }
                else if(Objects.equals(linha[1], "SS")) {setss(linha[2]);}
                else if(Objects.equals(linha[1], "DD")) {setdd(linha[2]);}
                else if(Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {setlg(linha[2]);}
                else if(!Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {setlgall(linha[2]);}
                else if(Objects.equals(linha[1], "ST")) {setst(linha[2]);}
            }
        }catch (FileNotFoundException e) {
            String[] aux = str.split("\\.");
            log.addFL(aux[0],"!!!!Erro na leitura do ficheiro de configuraçao do Servidor Primario!!!!");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        ServerP sp = new ServerP();
        Logs log = new Logs();
        Cache ca = new Cache();
        String configfile = "test.txt";
        sp.ParserSp(configfile);
        log.addEV("config",configfile);//leu o ficheiro de configuração
        ca.ParserCacheServer(sp.getname());
        log.addEV("bd",sp.getname()+".db");//leu o ficheiro de base de dados

        try {
            ServerSocket ss = new ServerSocket(123456); //conexão

            while(true) {
                System.out.println("espera de conexão!!!!!!!!!!!!!!!!!!");

                Socket s = ss.accept();
                //recebe uma conexao, que vem com um header
                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader bf = new BufferedReader(in);
                String str = bf.readLine();
                String[] aux = str.split(" ");

                //caso o header for um destes tipos executa
                //cliente
                if(Objects.equals(aux[0], "QE")){
                    try {
                        System.out.println("cliente conectado ao server primario");

                        System.out.println("mensagem do cliente: " + aux[1]);
                        log.addQR(sp.getSS(), aux[1]);

                        Query q = new Query();
                        String querydone = q.doQuery(aux[1], ca);
                        PrintWriter pr = new PrintWriter(s.getOutputStream());
                        pr.println(querydone);
                        log.addRP(sp.getSS(), querydone);
                        pr.flush();
                        log.addRP(sp.getSS(), querydone);
                        s.close();
                    }catch (Exception e){
                        log.addFL(sp.getSS(),"comunicação entre cliente e servidor primario");
                    }
                //servidor
                }else if(Objects.equals(aux[0], "domain:")) {
                    System.out.println("servidor ss pediu permissao");
                    try {
                        //enviar o numero de linhas da cache
                        int i = 0, j = 0;
                        if (Objects.equals(aux[1], sp.getname())) {
                            i = ca.getNumeberOfLinesSP();
                        }
                        PrintWriter pr2 = new PrintWriter(s.getOutputStream());
                        pr2.println(i);
                        pr2.flush();


                        //receber confirmaçao do numero de linhas
                        //ss.accept();
                        InputStreamReader in2 = new InputStreamReader(s.getInputStream());
                        BufferedReader bf2 = new BufferedReader(in2);
                        String str2 = bf2.readLine();
                        System.out.println("o servidor secundario aceita receber " + str2 + " linha");

                        //enviar linha por linha a cache do sp
                        //Socket ns = new Socket("localhost",4999);
                        while (i > j) {
                            Socket ns = new Socket("Localhost", 4999);
                            String aux2 = ca.getCacheLine(j);
                            PrintWriter pr3 = new PrintWriter(ns.getOutputStream());
                            pr3.println(aux2);
                            System.out.println(j + ": " + aux2);
                            pr3.flush();
                            j++;

                        }
                        s.close();
                    }catch (Exception e ){
                        log.addEZ(InetAddress.getLocalHost().getHostAddress(),"comunicação entre servidor secundario e servidor primario");
                    }
                }
                s.close();
            }
        }catch (Exception e){
            System.out.println("!!!!Erro no ServidorP !!!!");
            e.printStackTrace();
        }
    }
}
