import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Query {

    private String ID;
    private String Flags;
    private int nResponse;
    private int  nValues;
    private int nAutho;
    private int nExtravalues;
    private String InfoName;
    private String Type;

    public Query(){
        this.ID = "";
        this.Flags="";
        this.nResponse = 0;
        this.nValues = 0;
        this.nAutho = 0;
        this.nExtravalues = 0;
        this.InfoName = "";
        this.Type ="";

    }

    public void setID(String s){
        System.out.println(s);
        this.ID = s;
    }
    public void setFlags(String s){
        System.out.println(s);
        this.Flags = s;
    }
    public void setnResponse(String s){
        System.out.println(s);
        this.nResponse = Integer.parseInt(s);
    }
    public void setnValues(String s){
        System.out.println(s);
        this.nValues = Integer.parseInt(s);
    }
    public void setnAutho(String s){
        System.out.println(s);
        this.nAutho= Integer.parseInt(s);
    }
    public void setnExtravalues(String s){
        System.out.println(s);
        this.nExtravalues = Integer.parseInt(s);
    }
    public void setInfoName(String s){
        System.out.println(s);
        this.InfoName = s;
    }
    public void setType(String s){
        System.out.println(s);
        this.Type = s;
    }


    public void ParserQuery(){
        try {
            File ficheiro = new File("testquery.txt");
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parte = data.split(";");
                String[] priParte = parte[0].split(",");
                String[] segParte = parte[1].split(",");
                setID(priParte[0]);
                setFlags(priParte[1]);
                setnResponse(priParte[2]);
                setnValues(priParte[3]);
                setnAutho(priParte[4]);
                setnExtravalues(priParte[5]);
                setInfoName(segParte[0]);
                setType(segParte[1]);
            }
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
