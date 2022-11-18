import java.io.File;
import java.util.*;

public class CacheSP {

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
     private HashMap<String,String> mx;
     private HashMap<String,String> Aips;
    private HashMap<String,String> cnames;


    public CacheSP(){
        this.dfault = "";
        this.ttl = "";
        this.soasp = "";
        this.soaadmin = "";
        this.soaserial = "";
        this.soarefresh = "";
        this.soaexpire = "";
        this.allva = new HashMap<>();
        this.ns = new HashMap<>();
        this.mx = new HashMap<>();
        this.Aips = new HashMap<>();
        this.cnames = new HashMap<>();
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
    public void addAllva(String key,String value){
        System.out.println("allva-v-"+value+" k-"+key);
        this.allva.put(value,key);
    }
    public void addns(String key,String value){
        System.out.println("ns-"+key+" "+value);
        this.ns.put(key,value);
    }
    public void addmx(String mx,String value){
        System.out.println("mx-"+mx+" "+value);
        this.mx.put(mx,value);
    }
    public void addAips(String tipo,String ip){
        System.out.println("Aips-"+tipo+" "+ip);
        this.Aips.put(tipo,ip);
    }
    public void addCnames(String tipo,String nome){
        System.out.println("Cnames-"+tipo+" "+nome);
        this.cnames.put(tipo,nome);
    }

    public String getDfault() { return dfault;}

    public String getSoasp() { return soasp;}

    public String getSoaadmin() { return dfault;}

    public String getSoaserial() { return dfault;}

    public String getSoarefresh() { return dfault;}

    public String getSoaretry() { return dfault;}

    public String getSoaexpire() { return dfault;}

    public HashMap<String,String> getMx(){
        return mx;
    }

    public HashMap<String,String> getNs(){return ns;}

    public HashMap<String,String> getAllva(){return allva;}
    public String getTtl(){
        return ttl;
    }
    public HashMap<String,String> getAIps(){
        return Aips;
    }

    public HashMap<String,String> getCnames(){
        return cnames;
    }
    public void ParserCache(){
        try {
            File ficheiro = new File("testbd.txt");
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if (!Objects.equals(linha[0], "#")) {
                    if(Objects.equals(linha[0], "@")){
                        if (Objects.equals(linha[1], "DEFAULT")) {
                            setdfault(linha[2]);
                        } else if (Objects.equals(linha[1], "SOASP")) {
                            setsoasp(linha[2]);
                        } else if (Objects.equals(linha[1], "SOAADMIN")) {
                            setsoaadmin(linha[2]);
                        } else if (Objects.equals(linha[1], "SOASERIAL")) {
                            setsoaserial(linha[2]);
                        } else if (Objects.equals(linha[1], "SOAREFRESH")) {
                            setsoarefresh(linha[2]);
                        } else if (Objects.equals(linha[1], "SOARETRY")) {
                            setsoaretry(linha[2]);
                        } else if (Objects.equals(linha[1], "SOAEXPIRE")) {
                            setsoaexpire(linha[2]);
                        } else if (Objects.equals(linha[1], "NS")) {
                            String[] splt = linha[2].split("\\.");
                            addns(splt[0], linha[2]);
                            addAllva(linha[1], linha[2]);
                        } else if (Objects.equals(linha[1], "MX")) {
                            String[] splt = linha[2].split("\\.");
                            String aux = linha[2] + " " + linha[4];
                            addmx(splt[0], aux);
                            addAllva(linha[1], aux);
                        } else addAllva(linha[1], linha[2]);
                    } else if (Objects.equals(linha[0], "TTL")) {
                        setttl(linha[2]);
                    } else if (Objects.equals(linha[1], "A") && Objects.equals(linha[0], "www")) {
                        String aux = linha[2] + " " + linha[4];
                        addAips(linha[0], aux);
                    } else if (Objects.equals(linha[1], "A") && !Objects.equals(linha[0], "www")) {
                        addAips(linha[0], linha[2]);
                    } else if (Objects.equals(linha[1], "CNAME")) {
                        addCnames(linha[2], linha[0]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("!!!!Erro no parse do ficheiro de Base de Dados!!!!");
        }
    }
}