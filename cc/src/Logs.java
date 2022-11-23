import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Logs {


    public Logs(){
    }
    public void addEZ(String ip, String dados) throws IOException {
        String add = " Type:EZ IP:"+ip+" Data:"+dados;
        addToFile(add);
    }
    public void addZT(String ip,String dados) throws IOException {
        String add = " Type:ZT IP:"+ip+" Data:"+dados;
        addToFile(add);
    }
    public void addQE(String ip,String dados) throws IOException {
        String add = " Type:QE IP:"+ip+" Data:"+dados;
        addToFile(add);
    }
    public void addQR(String ip,String dados) throws IOException {
        String add = " Type:QR IP:"+ip+" Data:"+dados;
        addToFile(add);
    }
    public void addRP(String ip,String dados) throws IOException {
        String add = " Type:RP IP:"+ip+" Data:"+dados;
        addToFile(add);
    }
    public void addRR(String ip,String dados) throws IOException {
        String add = " Type:RR IP:"+ip+" Data:"+dados;
        addToFile(add);
    }
    public void addFL(String ip,String dados) throws IOException {
        String add = " Type:FL IP:"+ip+" erro:"+dados;
        addToFile(add);
    }
    public void addEV(String Type,String file) throws IOException {
        if (Objects.equals(Type,"config")){
            String add = " Type:EV @ conf-file-read on:" + file;
            addToFile(add);
        }else if(Objects.equals(Type,"bd")){
            String add = " Type:EV @ db-file-read on:" + file;
            addToFile(add);
        }else if(Objects.equals(Type,"log")){
            String add = " Type:EV @ log-file-create on:" + file;
            addToFile(add);
        }
    }
    public void addToFile(String conteudo) throws IOException,FileNotFoundException {

        try {
            FileWriter fW = new FileWriter("logs-CC.txt",true);
            PrintWriter pW = new PrintWriter(fW);
            String s ="Data: "+getDataHora() + conteudo;
            pW.println(s);
            pW.flush();
            pW.close();
        }catch (FileNotFoundException ignore){
            System.out.println("!!!!Erro em escrever no fichiero de logs!!!!");
        }
    }

    public String getDataHora(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy.HH:mm:ss:SSS");//dd/MM/yyyy
        Date now = new Date();
        String time = sdf.format(now);
        return time;
    }
}
