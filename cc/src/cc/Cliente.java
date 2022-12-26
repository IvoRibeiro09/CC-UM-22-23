package cc;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    private final DatagramSocket dsocket;
    private final InetAddress ServerIP;
    private int ServerPorta;
    private byte[] buffer = new byte[550];

    public Cliente(DatagramSocket dsocket,InetAddress ServerIP,int porta){
        this.dsocket = dsocket;
        this.ServerIP = ServerIP;
        this.ServerPorta = porta;
    }

    public void clienteservidor(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("digite uma mensagem a enviar para o servidor com IP: " + this.ServerIP);
            try{
                String msg = scanner.nextLine();
                buffer = msg.getBytes();
                DatagramPacket dp = new DatagramPacket(buffer,buffer.length,this.ServerIP, this.ServerPorta);
                dsocket.send(dp);
                //log QE
                System.out.println("query enviada: "+ msg);


                buffer = new byte[550];
                dp = new DatagramPacket(buffer,buffer.length,this.ServerIP, this.ServerPorta);
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


    /*
    String qu1 = "3874,Q+R,0,0,0,0;robalo.moc.,MX;";
     */
    public static void main(String[] args) throws UnknownHostException, SocketException {
        DatagramSocket dsocket = new DatagramSocket();
        //InetAddress ServerIP = InetAddress.getByName(args[0]);            //no core recebe o Ip do servidor
        InetAddress ServerIP = InetAddress.getByName("localhost");     //no ide
        int porta = 54321;
        Cliente cliente = new Cliente(dsocket,ServerIP,porta);
        cliente.clienteservidor();
    }

}
