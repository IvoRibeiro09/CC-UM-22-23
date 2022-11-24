import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class ServerS {
    private String dominio;
    private String db;
    private String sp;
    private String dd;
    private String lg;
    private String lgall;
    private String st;
    //construtor inicial
    public ServerS(){
        this.dominio="";
        this.db = "";
        this.sp = "";
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
    public void setsp(String s){
        this.sp=s;
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
    public String getDb(){
        return this.db;
    }
    public String getSP(){
        return this.sp;
    }
    public String getDominio(){return this.dominio;}
    //funçao que le o ficheiro de config do servidor secundario
    public void ParserSs(String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File(str);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if(Objects.equals(linha[1], "DB")) {setdb(linha[2]);setDominio(linha[0]);}
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
        String configfile = "files/SS.robalo.txt";
        servidor.ParserSs(configfile);
        log.addEV("config",configfile);//leu o ficheiro de configuração
        Cache cachess = new Cache();
        cachess.ParserCacheServer(servidor.getDb());
        log.addEV("bd",servidor.getDb());//leu o ficheiro de base de dados
        //conexoes
        ServerSocket servercliente = new ServerSocket(12346);
        ServerSocket servers = new ServerSocket(12347);

        try{
            System.out.println("comecei uma transferencia de zona");
            //Socket socket = new Socket("localhost",12345); // no ide
            Socket socket = new Socket(servidor.getSP(),12345); //no core
            PrintWriter pr = new PrintWriter((socket.getOutputStream()));
            String qu1 = "domain: "+servidor.getDominio();
            pr.println(qu1);
            pr.flush();
            System.out.println("perguntei se o sp é do mesmo dominio");
            //envio mensagem a pedir transferencia de zona ao servidor primario
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            int count = Integer.parseInt(bf.readLine());
            System.out.println("recebi o numero de linhas a ser transferido : "+count);

            //confirmaçao do numero de linhas a receber
            PrintWriter pr2 = new PrintWriter(socket.getOutputStream());
            pr2.println("ok: " + count);
            pr2.flush();
            socket.close();
            System.out.println("confirmei que estou apto a receber as "+count+" linhas");

            //receber todas as linhas
            int i = 0;
            while (i < count) {
                Socket sockets = servers.accept();
                InputStreamReader in2 = new InputStreamReader(sockets.getInputStream());
                BufferedReader bf2 = new BufferedReader(in2);
                String str = bf2.readLine();
                cachess.ParserPorLinha(str, servidor.getDominio());
                i++;
                sockets.close();
            }
            System.out.println("servidor secundario recebeu as "+i+" linhas");
            log.addZT(servidor.getSP(), "SS");
            System.out.println("espera de mova transferencia de zona!!!!!!!!!!!!!!!!!!");
        }catch (IOException e){
            System.out.println("!!!!Erro no servidor secundario!!!!");
            log.addEZ(servidor.getSP(),"SS");
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
                    log.addQR(servidor.getSP() ,aux[1]);

                    Query q = new Query();
                    String querydone = q.doQuery(aux[1], cachess);
                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                    pr.println(querydone);
                    log.addRP(servidor.getSP() , querydone);
                    pr.flush();
                    System.out.println("mensagem enviada para o cliente: " + querydone);
                    s.close();
                }
            }
        }catch (Exception e){
            System.out.println("!!!!Erro no ServidorP!!!!");
            log.addFL(servidor.getSP(),"comunicação entre cliente e servidor primario");
            e.printStackTrace();
        }

    }


}
