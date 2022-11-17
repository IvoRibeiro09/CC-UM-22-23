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
        //header Info
        this.ID = "";
        this.Flags="";
        this.nResponse = 0;
        this.nValues = 0;
        this.nAutho = 0;
        this.nExtravalues = 0;
        //Data Info
        this.InfoName = "";
        this.Type ="";

    }
    public void setID(String s){
        this.ID = s;
    }
    public void setFlags(String s){
        this.Flags = s;
    }
    public void setnResponse(String s){
        this.nResponse = Integer.parseInt(s);
    }
    public void setnValues(String s){
        this.nValues = Integer.parseInt(s);
    }
    public void setnAutho(String s){
        this.nAutho= Integer.parseInt(s);
    }
    public void setnExtravalues(String s){
        this.nExtravalues = Integer.parseInt(s);
    }
    public void setInfoName(String s){
        this.InfoName = s;
    }
    public void setType(String s){
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
    public String doquery(String str) throws IOException {
        ParserQuery(str);
        Cache ca = new Cache();
        ca.ParserCache();

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


        StringBuilder ls = new StringBuilder();
        ls.append(getId()).append(",R+A,").append(getnResponse()).append(",").append(getnValues()).append(",").append(getnAutho()).append(",").append(getnExtravalues()).append(";").append(getInfoName()).append(",").append(getType()).append(";");

        for(Map.Entry<String,String> entry : (ca.getMx()).entrySet()) {
            ls.append(getInfoName()).append(" ").append(entry.getKey()).append(" ").append(ca.getTtl()).append(" ").append(entry.getValue()).append(",");
        }
        ls.deleteCharAt(ls.length() - 1);
        ls.append(";");
        for(int j=0;j < getnAutho();j++) {
            ls.append(getInfoName()).append(" ").append((ca.getNS()).get(j)).append(" ").append(ca.getTtl()).append(",");
        }
        ls.deleteCharAt(ls.length() - 1);
        ls.append(";");
        for(Map.Entry<String,String> entry : (ca.getNames()).entrySet()){
            ls.append(entry.getValue()).append(".").append(getInfoName()).append(" A ").append((ca.getIps()).get(entry.getValue())).append(" ").append(ca.getTtl()).append(",");
        }
        ls.deleteCharAt(ls.length() - 1);
        ls.append(";");

        return ls.toString();
    }
}
