package cc;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ServerResolve {

    private String dominio;
    private String db;
    private String serverP;
    private List<String> listaServerS;
    private String dd;
    private String lg;
    private String lgall;
    private String st;
    private List <String> listaSTs;
    //private final ServerSocket ssocket;
    private final DatagramSocket dsocket;
    private byte[] buffer = new byte[550];


    public ServerResolve(DatagramSocket dsocket){
        this.dominio = "";
        this.db = "";
        this.serverP = "";
        this.listaServerS = new ArrayList<>();
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
        this.listaSTs = new ArrayList<>();
        //this.ssocket = ss;
        this.dsocket = dsocket;
    }

    //seters
    public void setDominio(String s) {
        this.dominio = s;
    }

    public void setdb(String s) {
        this.db = s;
    }

    public void setsp(String s) { this.serverP = s;}

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

    public void setsts(String s) {
        this.listaSTs.add(s);
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

    public String getServerP() {
        return serverP;
    }

    public void ParserSp(String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File(str);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if (Objects.equals(linha[1], "SP")) {
                    setsp(linha[2]);
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
    public String SRSP(String query) throws IOException{
        String resposta = null;
        System.out.println("mensagem enviada ao sp do mesmo dominio: "+query);
        try{
            Socket socket = new Socket("localhost",12345);                   //no ide
            DataInputStream in = new DataInputStream(socket.getInputStream());         //leitores
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(query);
            System.out.println("mandei a query ao ST");

            resposta = in.readUTF();
            System.out.println("recebi a seguinte resposta do st: "+resposta);

        }catch (IOException e){
            e.printStackTrace();
        }
        return resposta;
    }

    public String SRST(String query){
        String resposta = null;
        System.out.println("mensagem enviada ao st: "+query);
        try{
            Socket socket = new Socket("localhost",64321);                   //no ide
            DataInputStream in = new DataInputStream(socket.getInputStream());         //leitores
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());


            out.writeUTF(query);
            System.out.println("mandei a query ao ST");

            resposta = in.readUTF();
            System.out.println("recebi a seguinte resposta do st: "+resposta);

        }catch (IOException e){
            e.printStackTrace();
        }
        return resposta;
    }
    public void clienteServer(Query q) {
        while (true) {
            System.out.println("!!!!!!!!!! espera conexão de um cliente !!!!!!!!!!!!!!");
            try {
                DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
                dsocket.receive(dp);
                //log QR
                InetAddress ClienteIp = dp.getAddress();
                int porta = dp.getPort();
                String query = new String(dp.getData(),0,dp.getLength());
                System.out.println("query recebida: " + query);

                String resposta;
                if(isdomain(query) == 0) {
                    //enviar para o sp do dominio
                    resposta = SRST(query);
                }else {
                    //enviar para o st
                    resposta = SRSP(query);
                }
                //enviar resposta
                //por fim enviar a resposta ao cliente
                //log RP
            }catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    public int isdomain(String query){
        String[] aux = query.split(";");
        String[] aux2 = aux[1].split(",");
        if(Objects.equals(aux2[0],this.dominio)) return 0;
        else return 1;
    }
    public static void  main(String[] args) throws IOException {
        Query q = new Query();
        DatagramSocket ds = new DatagramSocket(54321);
        //ServerSocket ss = new ServerSocket(64321);
        ServerResolve sr = new ServerResolve(ds);
        String configfile = "SR.robalo.txt";
        sr.ParserSp(configfile);
        //sr.ParserSt(sr.st);
        sr.clienteServer(q);
    }
}
