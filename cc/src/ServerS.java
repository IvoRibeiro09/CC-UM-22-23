import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ServerS {
    private String db;
    private String sp;
    private String dd;
    private String lg;
    private String lgall;
    private String st;

    public ServerS(){
        this.db = "";
        this.sp = "";
        this.dd = "";
        this.lg = "";
        this.lgall = "";
        this.st = "";
    }

    public void setdb(String s){
        System.out.println(s);
        this.db=s;
    }

    public void setsp(String s){
        System.out.println(s);
        this.ss=s;
    }

    public void setdd(String s){
        System.out.println(s);
        this.dd=s;
    }

    public void setlg(String s){
        System.out.println(s);
        this.lg=s;
    }

    public void setlgall(String s){
        System.out.println(s);
        this.lgall=s;
    }

    public void setst(String s){
        System.out.println(s);
        this.st=s;
    }


    public void ParserSs(){
        try {
            File ficheiro = new File("testSS.txt");
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if(Objects.equals(linha[1], "DB")) {setdb(linha[2]);}
                else if(Objects.equals(linha[1], "SP")) {setsp(linha[2]);}
                else if(Objects.equals(linha[1], "DD")) {setdd(linha[2]);}
                else if(Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {setlg(linha[2]);}
                else if(!Objects.equals(linha[0], "all") && Objects.equals(linha[1], "LG")) {setlgall(linha[2]);}
                else if(Objects.equals(linha[1], "ST")) {setst(linha[2]);}
            }
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
