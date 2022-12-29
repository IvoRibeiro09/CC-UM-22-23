package cc;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerResolve {
    private String dominio;
    private String db;
    private HashMap<String,String> ServerIps;
    private String dd;
    private String lg;
    private String lgall;
    private String ST;
    private final DatagramSocket dsocket;
    private byte[] buffer = new byte[550];


    public ServerResolve(DatagramSocket dsocket){
        this.db = "";
        this.ServerIps = new HashMap<>();
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.dsocket = dsocket;
    }

    //seters
    public void setdb(String s) {
        this.db = s;
    }
    public void setDominio(String s){this.dominio = s;}

    public void setServerIps(String key,String value){
        this.ServerIps.put(key,value);
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
    public void setSt(String s){this.ST = s;}

    //geters
    public String getDb() {
        return this.db;
    }
    public String getDominio(){return this.dominio;}

    public void ParserSp(String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File(str);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if (Objects.equals(linha[1], "SP")) {
                    setServerIps(linha[1],linha[2]);
                } else if (Objects.equals(linha[1], "SS")) {
                    setServerIps(linha[1],linha[2]);
                } else if (Objects.equals(linha[1], "DD")) {
                    setdd(linha[2]);
                } else if (Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {
                    setlg(linha[2]);
                } else if (!Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {
                    setlgall(linha[2]);
                } else if (Objects.equals(linha[1], "ST")) {
                    setSt(linha[2]);
                    setDominio(linha[0]);
                }
            }
        } catch (FileNotFoundException e) {
            String[] aux = str.split("\\.");
            log.addFL(aux[0], "!!!!Erro na leitura do ficheiro de configuraçao do Servidor Primario!!!!",getDominio());
            e.printStackTrace();
        }
    }
    public String SRSP(String query,String IP) throws IOException{
        String resposta = null;
        System.out.println("mensagem enviada ao sp do mesmo dominio: "+query);
        try{
            Socket socket = new Socket(this.ServerIps.get("SP"),12345);                   //no ide
            DataInputStream in = new DataInputStream(socket.getInputStream());         //leitores
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(query);
            System.out.println("mandei a query ao SP");

            resposta = in.readUTF();
            System.out.println("recebi a seguinte resposta do sp: "+resposta);

        }catch (IOException e){
            e.printStackTrace();
        }
        return resposta;
    }

    public String SRST(String query){
        String resposta = null;
        System.out.println("mensagem enviada ao st: "+query);
        try{
            Socket socket = new Socket(this.ServerIps.get("ST"),64321);                   //no ide
            DataInputStream in = new DataInputStream(socket.getInputStream());         //leitores
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());


            out.writeUTF(query);
            System.out.println("mandei a query ao ST");

            resposta = in.readUTF();
            String[] aux = resposta.split("!");
            if(Objects.equals(aux[0],"smaller")){
                resposta = SRSP(query,aux[1]);
            }
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
                    resposta = SRSP(query,"");
                }else {
                    //enviar para o st
                    resposta = SRST(query);
                }
                //enviar resposta
                //por fim enviar a resposta ao cliente
                buffer = new byte[550];
                buffer = resposta.getBytes();
                dp = new DatagramPacket(buffer,buffer.length,ClienteIp,porta);
                System.out.println("resposta enviada: "+ resposta);
                dsocket.send(dp);
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
