import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;

public class serverp{

    private String db;
    private List<String> ss;
    private String dd; 
    private String lg;
    private String lgall;
    private String st;

    public void setdb(String s){
        this.db=s;
    }

    public void setss(String s){
        this.default.add(s);
    }

    public void setdd(String s){
        this.dd=s;
    }

    public void setlg(String s){
        this.lg=s;
    }

    public void setlgall(String s){
        this.lgall=s;
    }

    public void setst(String s){
        this.st=s;
    }


    private void parser(String file){
        try {
            File ficheiro = new File(file);
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if(linha[2]=="DB") {setdb(linha[3]);}
                else if(linha[2]=="SS") {setss(linha[3]);}
                else if(linha[2]=="DD") {setdd(linha[3]);}
                else if(linha[1]!="all"&&linha[2]=="LG") {setlg(linha[3]);}
                else if(linha[1]!="all"&&linha[2]=="LG") {setlgall(linha[3]);}
                else if(linha[2]=="ST") {setst(linha[3]);}
        }
    }
}
}
