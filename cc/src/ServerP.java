import javax.management.StringValueExp;
import java.io.*;
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

    public ServerP(){
        this.name = "";
        this.db = "";
        this.ss =  new ArrayList<>();
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
    }
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
    public String getname(){return name;}
    public String getSS(){
        return ss.get(0);
    }
    public String getDb() {
        return db;
    }


    public void ParserSp(){
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
            System.out.println("!!!!Erro na funçao Parser so ServidorP!!!!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerP sp = new ServerP();
        sp.ParserSp();
        Logs log = new Logs();
        Cache ca = new Cache();
        ca.ParserCacheSP(sp.getname()+".");
        System.out.println("linas de cache " + ca.getNumeberOfLinesSP());
        try {
            ServerSocket ss = new ServerSocket(4998);
            while(true) {
                System.out.println("espera de conexão!!!!!!!!!!!!!!!!!!");

                Socket s = ss.accept();
                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader bf = new BufferedReader(in);
                String str = bf.readLine();
                String[] aux = str.split(" ");

                //cliente
                if(Objects.equals(aux[0], "QE")){
                    System.out.println("cliente conectado ao server primario");

                    System.out.println("mensagem do cliente: " + aux[1]);
                    log.addToFile("QR " + sp.getSS() + " " + aux[1]);

                    Query q = new Query();
                    String querydone = q.doQuery(str, ca);
                    PrintWriter pr = new PrintWriter(s.getOutputStream());
                    pr.println(querydone);
                    log.addToFile("RR " + sp.getSS() + " " + querydone);
                    pr.flush();
                    s.close();
                    //servidor
                }else if(Objects.equals(aux[0], "domain:")) {
                    System.out.println("servidor ss pediu permissao");

                    //enviar o numero de linhas da cache
                    int i=0,j=0;
                    if (Objects.equals(aux[1], sp.getname())) {
                        i = ca.getNumeberOfLinesSP();
                    }
                    PrintWriter pr2 = new PrintWriter(s.getOutputStream());
                    pr2.println(i);
                    pr2.flush();

                    //receber confirmaçao do numero de linhas
                    InputStreamReader in2 = new InputStreamReader(s.getInputStream());
                    BufferedReader bf2 = new BufferedReader(in2);
                    String str2 = bf2.readLine();
                    System.out.println("o servidor secundario aceita receber "+str2+" linha");

                    //enviar linha por linha a cache do sp
                    while(i>j){
                        Socket socket = new Socket("localHost",4999);
                        String aux2 = ca.getCacheLine(j);
                        PrintWriter pr3 = new PrintWriter(socket.getOutputStream());
                        pr3.println(aux2);
                        System.out.println(j+": "+aux2);
                        pr3.flush();
                        j++;
                    }
                    s.close();
                }
                s.close();
            }
        }catch (Exception e){
            System.out.println("!!!!Erro no ServidorP ss!!!!");
            e.printStackTrace();
        }
    }
}
