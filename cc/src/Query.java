import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.util.*;

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

    private void ParserQuery(String data) throws IOException {
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
    public String getId(){
        return ID;
    }
    public int getnResponse() {
        return nResponse;
    }

    public int getnValues() {
        return nValues;
    }

    public int getnAutho() {
        return nAutho;
    }

    public int getnExtravalues() {
        return nExtravalues;
    }

    public String getInfoName() {
        return InfoName;
    }

    public String getType() {
        return Type;
    }
    public List<String> doquery(String str) throws IOException {
        ParserQuery(str);
        Cache ca = new Cache();
        ca.ParserCache();

        List<String> ls = new ArrayList<>();
        String tipo = getType();
        int valor = 0,nValor=0;
        if (Objects.equals(tipo, "MX")){
            valor = (ca.getMx()).size();
            nValor = (ca.getNS()).size();
        }else if(Objects.equals(tipo, "NS")){
            valor = (ca.getNS()).size();
            nValor = (ca.getMx()).size();
        }
        setnValues(Integer.toString(valor));
        setnAutho(Integer.toString(nValor));
        setnExtravalues(Integer.toString((ca.getNames()).size()));

        String fsrt = getId()+",R+A,"+getnResponse()+","+getnValues()+","+getnAutho()+","+getnExtravalues()+";"+getInfoName()+","+getType()+";";
        ls.add(fsrt);
        for(Map.Entry<String,String> entry : (ca.getMx()).entrySet()) {
            ls.add(getInfoName() + " " + entry.getKey() +" "+ca.getTtl()+" "+entry.getValue());
        }
        for(int j=0;j < getnAutho();j++) {
            ls.add(getInfoName() + " " + (ca.getNS()).get(j)+" "+ca.getTtl());
        }
        for(Map.Entry<String,String> entry : (ca.getNames()).entrySet()){
            ls.add(entry.getValue()+"."+getInfoName()+" A " + (ca.getIps()).get(entry.getValue())+" "+ ca.getTtl() );
        }
    /*
        for(int i=0;i<(getnAutho()+getnValues()+getnExtravalues()+1);i++){
            System.out.println(ls.get(i));
        }
    */
        return ls;
    }
}
