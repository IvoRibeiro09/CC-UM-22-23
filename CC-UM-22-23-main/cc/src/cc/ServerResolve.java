package cc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
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

    private final ServerSocket ss;
    private final DatagramSocket dsocket;
    private byte[] buffer = new byte[550];


    public ServerResolve(DatagramSocket dsocket,ServerSocket ss) {
        this.dominio = "";
        this.db = "";
        this.serverP = "";
        this.listaServerS = new ArrayList<>();
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
        this.listaSTs = new ArrayList<>();
        this.ss = ss;
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

    public void ParserSt(String str) throws IOException {
        Logs log = new Logs();
        try {
            File ficheiro = new File(str);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if (Objects.equals(linha[1], "ST")) {
                    setsts(linha[2]);
                }
            }
        } catch (FileNotFoundException e) {
            String[] aux = str.split("\\.");
            log.addFL(aux[0], "!!!!Erro na leitura do ficheiro de Servidores de topo!!!!",getDominio());
            e.printStackTrace();
        }
    }

    public void SRSP(String query){
        //public void clienteservidor(String portaString){                  //no core
        //int porta = Integer.parseInt(portaString);                        //no core
        while(true){
            System.out.println("digite uma mensagem a enviar para o servidor com IP: " + this.serverP);
            try{
                String msg = query;
                buffer = msg.getBytes();
                DatagramPacket dp = new DatagramPacket(buffer,buffer.length,this.serverP, 12345);
                dsocket.send(dp);
                //log QE
                System.out.println("query enviada: "+ msg);

                buffer = new byte[550];
                dp = new DatagramPacket(buffer,buffer.length,this.serverP, 12345);
                dsocket.receive(dp);
                //Log RR
                String resposta = new String(dp.getData(),0, dp.getLength());
                System.out.println("resposta recebida: " + resposta);

            }catch (IOException e){
                e.printStackTrace();
                break;
            }
        }
    }

    public String SRST(String query,String dominio){
        //public void clienteservidor(String portaString){                  //no core
        //int porta = Integer.parseInt(portaString);                        //no core
        while(true){
            System.out.println("digite uma mensagem a enviar para o servidor com IP: " + this.listaSTs.get(0));
            try{
                StringBuilder msg = new StringBuilder();
                msg.append(dominio).append("/").append(query);
                msg.toString();
                buffer = msg.getBytes();
                DatagramPacket dp = new DatagramPacket(buffer,buffer.length,this.listaSTs.get(0), 12345);
                dsocket.send(dp);
                //log QE
                System.out.println("query enviada: "+ msg);

                buffer = new byte[550];
                dp = new DatagramPacket(buffer,buffer.length,this.listaSTs.get(0), 12345);
                dsocket.receive(dp);
                //Log RR
                String resposta = new String(dp.getData(),0, dp.getLength());
                System.out.println("resposta recebida: " + resposta);

            }catch (IOException e){
                e.printStackTrace();
                break;
            }
        }
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

                buffer = new byte[64000];
                if(Objects.equals(q.dominioQuery(query,getDominio()),1)){
                    SRSP(query);
                }else if(Objects.equals(q.dominioQuery(query,getDominio()),0)){
                    SRST(query,getDominio());
                }else{
                    StringBuilder ls = new StringBuilder();
                    ls.append(q.getId()).append(",R+A,3,0,0,0;,;");
                    ls.toString();
                    buffer = ls.getBytes();
                    dp = new DatagramPacket(buffer,buffer.length,ClienteIp,porta);
                    System.out.println("resposta enviada: "+ ls);
                    dsocket.send(dp);
                }
            }catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void  main(String[] args) throws IOException {
        Query q = new Query();
        DatagramSocket ds = new DatagramSocket(12345);
        ServerSocket ss = new ServerSocket(12346);
        ServerResolve sr = new ServerResolve(ds,ss);
        String configfile = "SP.robalo.txt";
        sr.ParserSp(configfile);
        sr.ParserSt(sr.st);
        sr.clienteServer(q);
    }
}
