import java.io.File;
import java.util.*;

public class Cache {

    private String dfault;
    private String ttl;
    private String soasp;
    private String soaadmin;
    private String soaserial;
    private String soarefresh;
    private String soaretry;
    private String soaexpire;
    private HashMap<String,String> allva;
     private HashMap<String,String> ns;
     private HashMap<String,String > smalers;
     private HashMap<String,String> mx;
     private HashMap<String,String> Aips;
    private HashMap<String,String> cnames;
    private int lines ;
    private ArrayList<String> AllLines;


    public Cache(){
        this.dfault = "";
        this.ttl = "";
        this.soasp = "";
        this.soaadmin = "";
        this.soaserial = "";
        this.soarefresh = "";
        this.soaexpire = "";
        this.allva = new HashMap<>();
        this.ns = new HashMap<>();
        this.smalers = new HashMap<>();
        this.mx = new HashMap<>();
        this.Aips = new HashMap<>();
        this.cnames = new HashMap<>();
        this.lines = 0;
        this.AllLines = new ArrayList<String>();
    }

    public void setdfault(String s){
        this.dfault = s;
    }
    public void setttl(String s){
        this.ttl = s;
    }
    public void setsoasp(String s){
        this.soasp = s;
    }
    public void setsoaadmin(String s){
        this.soaadmin = s;
    }
    public void setsoaserial(String s){
        this.soaserial = s;
    }
    public void setsoarefresh(String s){
        this.soarefresh = s;
    }
    public void setsoaretry(String s){
        this.soaretry = s;
    }
    public void setsoaexpire(String s){
        this.soasp = s;
    }
    public void setAllva(String key,String value){
        this.allva.put(value,key);
    }
    public void setSmalers(String key,String value){
        this.smalers.put(key,value);
    }
    public void setNs(String key,String value){
        this.ns.put(key,value);
    }
    public void setMx(String mx,String value){
        this.mx.put(mx,value);
    }
    public void setAips(String tipo,String ip){
        this.Aips.put(tipo,ip);
    }
    public void setCnames(String tipo,String nome){
        this.cnames.put(tipo,nome);
    }
    public void setAllLines(String s){
        this.AllLines.add(s);
    }
    public void incLines(){
        this.lines++;
    }


    public String getDfault(){
        return this.dfault;
    }
    public HashMap<String,String> getAllva(){
        return allva;
    }
    public String getTtl(){
        return ttl;
    }
    public HashMap<String,String> getAIps(){
        return Aips;
    }
    private HashMap<String, String> getCnames() {
        return this.cnames;
    }
    private HashMap<String, String> getMx() {
        return this.mx;
    }
    private HashMap<String, String> getNs() {
        return this.ns;
    }
    public int getNumeberOfLinesSP() {
        return this.lines;
    }
    private String getSoaexpire() {
        return this.soaexpire;
    }
    private String getSoaretry() {
        return this.soaretry;
    }
    private String getSoarefresh() {
        return this.soarefresh;
    }
    private String getSoaserial() {
        return this.soaserial;
    }
    private String getSoaadmin() {
        return this.soaadmin;
    }
    private String getSoasp() {
        return this.soasp;
    }


    public boolean ParserCacheSP(String str){
        try {
            String strfile = str +"db";
            File ficheiro = new File(strfile);
            Scanner myReader = new Scanner(ficheiro);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                ParserPorLinha(data);
            }
            return true;
        } catch (Exception e) {
            System.out.println("!!!!Erro no parse do ficheiro de Base de Dados!!!!");
            return false;
        }
    }

    public void ParserPorLinha(String str) {

        String[] linha = str.split(" ");
        if (!Objects.equals(linha[0], "#")) {
            if (Objects.equals(linha[0], "@")) {
                if (Objects.equals(linha[1], "DEFAULT")) {
                    setdfault(linha[2]);
                } else if (Objects.equals(linha[1], "SOASP")) {
                    setsoasp(linha[2]);
                    incLines();
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl());
                } else if (Objects.equals(linha[1], "SOAADMIN")) {
                    setsoaadmin(linha[2]);
                    incLines();
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl());
                } else if (Objects.equals(linha[1], "SOASERIAL")) {
                    setsoaserial(linha[2]);
                    incLines();
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl());
                } else if (Objects.equals(linha[1], "SOAREFRESH")) {
                    setsoarefresh(linha[2]);
                    incLines();
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl());
                } else if (Objects.equals(linha[1], "SOARETRY")) {
                    setsoaretry(linha[2]);
                    incLines();
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl());
                } else if (Objects.equals(linha[1], "SOAEXPIRE")) {
                    setsoaexpire(linha[2]);
                    incLines();
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl());
                } else if (Objects.equals(linha[1], "NS")) {
                    String[] splt = linha[2].split("\\.");
                    setNs(splt[0], linha[2]);
                    incLines();
                    setAllva(linha[1], linha[2]);
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl());
                } else if (Objects.equals(linha[1], "MX")) {
                    String[] splt = linha[2].split("\\.");
                    String aux = linha[2] + " " + linha[4];
                    setMx(splt[0], aux);
                    setAllva(linha[1], aux);
                    incLines();
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl()+" "+linha[4]);
                } else {
                    setAllva(linha[1], linha[2]);
                    incLines();
                    setAllLines(getDfault()+" "+linha[1]+" "+linha[2]);
                }
            } else if (Objects.equals(linha[0], "TTL")) {
                setttl(linha[2]);
            }else if (Objects.equals(linha[0],"Smaller.@")){
                setSmalers(linha[1], linha[2]);
                incLines();
                setAllLines(getDfault()+" "+linha[1]+" "+linha[2]);
            } else if (Objects.equals(linha[1], "A") && Objects.equals(linha[0], "www")) {
                String aux = linha[2] + " " + linha[4];
                setAips(linha[0], aux);
                incLines();
                setAllLines(linha[0]+"."+getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl()+" "+linha[4]);
            } else if (Objects.equals(linha[1], "A") && !Objects.equals(linha[0], "www")) {
                setAips(linha[0], linha[2]);
                incLines();
                setAllLines(linha[0]+"."+getDfault()+" "+linha[1]+" "+linha[2]+" "+getTtl());
            } else if (Objects.equals(linha[1], "CNAME")) {
                setCnames(linha[2], linha[0]);
                incLines();
                setAllLines(linha[0]+"."+getDfault()+" "+linha[1]+" "+linha[2]+"."+getDfault()+" "+getTtl());
            }
        }
    }

    public String getCacheLine(int i){
        return AllLines.get(i);
    }
}