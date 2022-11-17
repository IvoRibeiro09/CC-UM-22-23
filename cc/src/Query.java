import java.io.IOException;
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
    private void ParserQuery(String data)  {
        try{
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
        }catch(Exception e){
            String[] id = data.split(",",1);
            setID(id[0]);
            setFlags("");
            setnResponse("3");
            setnValues("0");
            setnAutho("0");
            setnExtravalues("0");
            setInfoName("");
            setType("");
        }
    }
    public String getId(){
        return ID;
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
        String nomedominio = getInfoName();
        String tipo = getType();
        int nva=0,nres=0,nextra=0;
        StringBuilder ls = new StringBuilder();

        StringBuilder rv = new StringBuilder();
        StringBuilder av = new StringBuilder();
        StringBuilder ev = new StringBuilder();
        List<String> listas = new ArrayList<>();
        Set<String > chaves = ca.getAllva().keySet();


        for(String chave : chaves){
            if(Objects.equals(tipo, ca.getAllva().get(chave))){
                nva++;
                if(Objects.equals(tipo, "MX")){
                    String[] spl = chave.split(" ",2);
                    listas.add(spl[0]);
                    rv.append(nomedominio).append(" ").append(tipo).append(" ")
                            .append(spl[0]).append(" ").append(ca.getTtl()).append(" ")
                            .append(spl[1]).append(",");
                }else {
                    listas.add(chave);
                    rv.append(nomedominio).append(" ").append(tipo).append(" ")
                            .append(chave).append(" ").append(ca.getTtl()).append(",");
                }
            }
            if(Objects.equals(ca.getAllva().get(chave),"NS")){
                nres++;
                listas.add(chave);
                av.append(nomedominio).append(" NS ").append(chave)
                        .append(" ").append(ca.getTtl()).append(",");
            }
        }
        for (String lista : listas) {
            nextra++;
            String[] splt = lista.split("\\.");
            String ip = ca.getAIps().get(splt[0]);
            ev.append(splt[0]).append(".").append(nomedominio).append(" A ")
                    .append(ip).append(" ").append(ca.getTtl()).append(",");
        }

        ls.append(getId()).append(",R+A,").append(nres).append(",").append(nva)
                .append(",").append(nres).append(",").append(nextra).append(";")
                .append(nomedominio).append(tipo).append(";").append(rv)
                .deleteCharAt(ls.length() - 1).append(";").append(av)
                .deleteCharAt(ls.length() - 1).append(";").append(ev)
                .deleteCharAt(ls.length() - 1).append(";");

        return ls.toString();
    }
}
