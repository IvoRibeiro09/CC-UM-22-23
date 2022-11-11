import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
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
       ArrayList<String> gfg = new ArrayList<String>();
       this.ss = gfg;
       this.dd = "";
       this.lg = "";
       this.lgall = "";
       this.st = "";
    }

    public void setdb(String s){
        System.out.println(s);
        this.db=s;
    }

    public void setss(String s){
        System.out.println(s);
        this.ss.add(s);
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
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
