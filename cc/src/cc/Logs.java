package cc;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Logs {


    public Logs(){
    }
    public void addEZ(String ip, String dados,String domain) throws IOException {
        String add = " Type:EZ IP:"+ip+" Data:"+dados;
        addToFicheiro(add,domain);
    }
    public void addZT(String ip,String dados,String domain) throws IOException {
        String add = " Type:ZT IP:"+ip+" Data:"+dados;
        addToFicheiro(add,domain);
    }
    public void addQE(String ip,String dados,String domain) throws IOException {
        String add = " Type:QE IP:"+ip+" Data:"+dados;
        addToFicheiro(add,domain);
    }
    public void addQR(String ip,String dados,String domain) throws IOException {
        String add = " Type:QR IP:"+ip+" Data:"+dados;
        addToFicheiro(add,domain);
    }
    public void addRP(String ip,String dados,String domain) throws IOException {
        String add = " Type:RP IP:"+ip+" Data:"+dados;
        addToFicheiro(add,domain);
    }
    public void addRR(String ip,String dados,String domain) throws IOException {
        String add = " Type:RR IP:"+ip+" Data:"+dados;
        addToFicheiro(add,domain);
    }
    public void addFL(String ip,String dados,String domain) throws IOException {
        String add = " Type:FL IP:"+ip+" erro:"+dados;
        addToFicheiro(add,domain);
    }
    public void addEV(String Type,String file,String domain) throws IOException {
        if (Objects.equals(Type,"config")){
            String add = " Type:EV @ conf-file-read on:" + file;
            addToFicheiro(add,domain);
        }else if(Objects.equals(Type,"bd")){
            String add = " Type:EV @ db-file-read on:" + file;
            addToFicheiro(add,domain);
        }else if(Objects.equals(Type,"log")){
            String add = " Type:EV @ log-file-create on:" + file;
            addToFicheiro(add,domain);
        }
    }
    public void addToFicheiro(String conteudo,String domain) throws IOException,FileNotFoundException {
        try {
            addToFicheiroGlobal(conteudo);
            File dmfile = new File( "alllogs."+ domain +".txt");
            dmfile.createNewFile();
            FileWriter fW = new FileWriter("alllogs."+ domain +".txt",true);
            PrintWriter pW = new PrintWriter(fW);
            String s ="Data: "+getDataHora() + conteudo;
            pW.println(s);
            pW.flush();
            pW.close();
        }catch (FileNotFoundException ignore){
            System.out.println("!!!!Erro em escrever no fichiero de logs!!!!(ficheiro cc.Logs)");
        }
    }
    public void addToFicheiroGlobal(String conteudo) throws IOException,FileNotFoundException {

        try {
            FileWriter fW = new FileWriter("logs-CC.txt",true);
            PrintWriter pW = new PrintWriter(fW);
            String s ="Data: "+getDataHora() + conteudo;
            pW.println(s);
            pW.flush();
            pW.close();
        }catch (FileNotFoundException ignore){
            System.out.println("!!!!Erro em escrever no fichiero de logs!!!!(ficheiro cc.Logs)");
        }
    }
    public String getDataHora(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy.HH:mm:ss:SSS");//dd/MM/yyyy
        Date now = new Date();
        String time = sdf.format(now);
        return time;
    }
}
