import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerP {

    private String db;
    private List<String> ss;
    private String dd;
    private String lg;
    private String lgall;
    private String st;

    public ServerP(){
       this.db = "";
       this.ss =  new ArrayList<>();
       this.dd = "";
       this.lg = "";
       this.lgall = "";
       this.st = "";
    }
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
    public String getSS(){
        return ss.get(0);
    }
    public void ParserSp(){
        try {
            File ficheiro = new File("test.txt");
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if(Objects.equals(linha[1], "DB")) {setdb(linha[2]);}
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
        try {
            ServerSocket ss = new ServerSocket(4998);
            Logs log = new Logs();
            while(true) {
                System.out.println("espera de conexão!!!!!!!!!!!!!!!!!!");

                Socket s = ss.accept();
                System.out.println("cliente conectado ao server primario");

                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader bf = new BufferedReader(in);

                String str = bf.readLine();
                System.out.println("mensagem do cliente: " + str);
                log.addToFile("QR "+ sp.getSS() + " "+str);

                Query q = new Query();
                String querydone = q.doQuery(str);
                PrintWriter pr = new PrintWriter(s.getOutputStream());
                pr.println(querydone);
                log.addToFile("RR "+sp.getSS()+" "+querydone);
                pr.flush();
                s.close();
            }



        }catch (IOException e ){
            System.out.println("!!!!Erro no ServidorP!!!!");
            e.printStackTrace();
        }
    }
}
