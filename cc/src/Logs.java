import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {


    public Logs(){
    }

    //add to file
    public void addToFile(String conteudo) throws IOException,FileNotFoundException {

        try {
            FileWriter fW = new FileWriter("logs-CC.txt",true);
            PrintWriter pW = new PrintWriter(fW);
            String s = getDataHora() +" "+ conteudo;
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
