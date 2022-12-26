package cc;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerP {
    private String dominio;
    private String db;
    private List<String> listaServerS;
    private String dd;
    private String lg;
    private String lgall;
    private String st;
    private final ServerSocket ss;
    private byte[] buffer = new byte[550];

    //construtor vazio de um servidor principal
    public ServerP(ServerSocket ss) {
        this.dominio = "";
        this.db = "";
        this.listaServerS = new ArrayList<>();
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
        this.ss = ss;
    }

    //seters
    public void setDominio(String s) {
        this.dominio = s;
    }

    public void setdb(String s) {
        this.db = s;
    }

    public void setss(String s) {
        this.listaServerS.add(s);
    }

    public void setdd(String s) {
        this.dd = s;
    }

    public void setlg(String s) {
        this.lg = s;
    }

    public void setlgall(String s) {
        this.lgall = s;
    }

    public void setst(String s) {
        this.st = s;
    }

    //geters
    public String getDominio() {
        return this.dominio;
    }
    public String getDb() {
        return this.db;
    }
    public String get1SS() {
        return listaServerS.get(0);
    }

    //funçao que interpreta o fichiero de configuraçao do servidor Principal e guarda essa informaçao
    public void ParserSp(String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File(str);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if (Objects.equals(linha[1], "DB")) {
                    setdb(linha[2]);
                    setDominio(linha[0]);
                } else if (Objects.equals(linha[1], "SS")) {
                    setss(linha[2]);
                } else if (Objects.equals(linha[1], "DD")) {
                    setdd(linha[2]);
                } else if (Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {
                    setlg(linha[2]);
                } else if (!Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {
                    setlgall(linha[2]);
                } else if (Objects.equals(linha[1], "ST")) {
                    setst(linha[2]);
                }
            }
        } catch (FileNotFoundException e) {
            String[] aux = str.split("\\.");
            log.addFL(aux[0], "!!!!Erro na leitura do ficheiro de configuraçao do Servidor Primario!!!!",getDominio());
            e.printStackTrace();
        }
    }
    public void servidorSp(Cache ca,Query q){
        while(true){
            System.out.println("!!!!!!!!!! SP à espera de conexão!!!!!!!!!!!!!!");
            try{
                Socket socket = ss.accept();
                System.out.println("um servidor foi conectado ao servidor principal");
                DataInputStream in = new DataInputStream(socket.getInputStream()); //leitores
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());//escritor

                String str = in.readUTF();
                System.out.println(str);
                String[] aux = str.split(" ");
                if (Objects.equals(aux[0], "domain:")) {
                    System.out.println("servidorS é do mesmo dominio que o ServerP");
                    //conta toda a informaçao que tem guardada na sua cache
                    int i = 0, j = 0;
                    if (Objects.equals(aux[1], getDominio())) {
                        i = ca.getNumeberOfLinesSP();
                    }
                    //envia mensagem ao ServerS com o numero de linhas que ira enviar
                    out.writeInt(i);
                    System.out.println("o servidorP enviou a msg: " + i);

                    //recebe a confirmaçao que o ServerS aceita receber o numero de linhas indicado
                    String str2 = in.readUTF();
                    System.out.println("o servidor secundario aceita receber " + str2 + " linha");

                    //envia a informaçao presente na base de dados linha a linha
                    while (i > j) {

                        String linha = ca.getCacheLine(j);
                        out.writeUTF(linha);
                        System.out.println("linha "+j+": "+linha);
                        j++;
                    }
                    System.out.println("o servidor primario enviou as " + (j+1) + " linhas");

                }else{
                    System.out.println("recebi do ST ou do Sr a query: "+str);

                    //responder á query
                    String resposta = q.doQuery(str,ca);
                    System.out.println("reposta devolvida pelo Sp : "+resposta);
                    out.writeUTF(resposta);

                }

                socket.close(); //fecha a conexao
                in.close();//fecha leitores e escritores
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void  main(String[] args) throws IOException {
        Query q = new Query();
        Cache ca = new Cache();

        ServerSocket ss = new ServerSocket(12345);
        ServerP sp = new ServerP(ss);
        String configfile = "SP.robalo.txt";
        sp.ParserSp(configfile);
        ca.ParserCacheServer(sp.getDb());

        Thread t1 = new Thread(new Mover(sp, 0, ca, q)); //thread responsavel pela conexao com os clientes
        Thread t2 = new Thread(new Mover(sp, 1, ca, q)); //thread responsavel pela conexao com os servidores

        t1.start();t2.start(); //iniciar as threads

    }
    static class Mover implements Runnable {
        ServerP sp;
        int s;
        Cache ca;
        Query q;
        public Mover(ServerP sp, int s,Cache ca ,Query q) { this.sp=sp; this.s=s;this.ca=ca;this.q=q;}

        public void run() {
            if (s == 0) sp.servidorSp(ca,q);
            //if (s == 1) sp.SPSS(ca);
        }
    }
}


