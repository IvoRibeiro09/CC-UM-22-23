import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static java.lang.Thread.sleep;

public class ServerP {
    private String dominio;
    private String db;
    private List<String> ss;
    private String dd;
    private String lg;
    private String lgall;
    private String st;

    //construtor vazio de um servidor principal
    public ServerP(){
        this.dominio = "";
        this.db = "";
        this.ss =  new ArrayList<>();
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
    }

    //seters
    public void setDominio(String s){this.dominio=s;}
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
    public String getDominio(){return this.dominio;}
    public String getDb(){return this.db;}
    public String get1SS(){
        return ss.get(0);
    }

    //funçao que interpreta o fichiero de configuraçao do servidor Principal e guarda essa informaçao
    public void ParserSp( String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File("cc/SP.robalo.txt");
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if(Objects.equals(linha[1], "DB")) {setdb(linha[2]);setDominio(linha[0]);}
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
        String configfile = "cc/SP.robalo.txt";
        sp.ParserSp(configfile);
        log.addEV("config",configfile);//leu o ficheiro de configuração
        ca.ParserCacheServer(sp.getDb());
        log.addEV("bd",sp.getDb());//leu o ficheiro de base de dados

        try {
            ServerSocket ss = new ServerSocket(12345); //conexão

            while(true) {
                System.out.println("espera de conexão!!!!!!!!!!!!!!!!!!");

                Socket s = ss.accept();
                //recebe uma conexao, que vem com um header
                System.out.println("alguma coisa foi conectada");
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
                        log.addQR(sp.get1SS(), aux[1]);

                        Query q = new Query();
                        String querydone = q.doQuery(aux[1], ca);
                        PrintWriter pr = new PrintWriter(s.getOutputStream());
                        pr.println(querydone);
                        log.addRP(sp.get1SS(), querydone);
                        pr.flush();
                        System.out.println("mensagem enviada para o cliente: " + querydone);
                        s.close();
                    }catch (Exception e){
                        log.addFL(sp.get1SS(),"comunicação entre cliente e servidor primario");
                    }
                //servidor
                }else if(Objects.equals(aux[0], "domain:")) {
                    System.out.println("servidor ss pediu permissao");
                    try {
                        //enviar o numero de linhas da cache
                        int i = 0, j = 0;
                        if (Objects.equals(aux[1], sp.getDominio())) {
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
                        //Socket ns = new Socket("Localhost", 12347);
                        while (i > j) {
                           // Socket ns = new Socket("Localhost", 12347); // no ide
                            //Socket ns = new Socket(sp.get1SS(), 12347); //no core
                            String aux2 = ca.getCacheLine(j);
                            PrintWriter pr3 = new PrintWriter(s.getOutputStream());
                            pr3.println(aux2);
                            pr3.flush();
                            j++;
                            sleep(0,3);
                        }
                        System.out.println("o servidor primario enviou as "+j+" linhas");
                        s.close();
                    }catch (Exception e ){
                        log.addEZ(sp.get1SS(),"comunicação entre servidor secundario e servidor primario");
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
