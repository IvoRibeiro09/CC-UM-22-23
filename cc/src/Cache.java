
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
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
    private List<String> ns;
    private String smaller;

    private HashMap<String,String> mx;
    //private List<String> mx;
    //private List<String> mxprio;
    private HashMap<String,String> ips;
   // private List<String> www;
    //private List<String> wwwprio;
    private HashMap<String,String> www;
    private HashMap<String,String> names;

    public Cache(){
        //HashMap<String, String> doubs  = new HashMap<String, String>();
        ArrayList<String> gfg = new ArrayList<String>();
        this.dfault = "";
        this.ttl = "";
        this.soasp = "";
        this.soaadmin = "";
        this.soaserial = "";
        this.soarefresh = "";
        this.soaexpire = "";
        this.ns = gfg;
        this.smaller = "";
        HashMap<String, String> doubs  = new HashMap<String, String>();
        this.mx = doubs;
        //this.mx = new ArrayList<String>();
        //this.mxprio = gfg;
        HashMap<String, String> doubs1  = new HashMap<String, String>();
        this.ips = doubs1;
        //this.www = gfg;
        //this.wwwprio = gfg;
        HashMap<String, String> doubs2  = new HashMap<String, String>();
        this.www = doubs2;
        HashMap<String, String> doubs3  = new HashMap<String, String>();
        this.names = doubs3;

    }

    public void setdfault(String s){
        System.out.println(s);
        this.dfault = s;
    }
    public void setttl(String s){
        System.out.println(s);
        this.ttl = s;
    }
    public void setsoasp(String s){
        System.out.println(s);
        this.soasp = s;
    }
    public void setsoaadmin(String s){
        System.out.println(s);
        this.soaadmin = s;
    }
    public void setsoaserial(String s){
        System.out.println(s);
        this.soaserial = s;
    }
    public void setsoarefresh(String s){
        System.out.println(s);
        this.soarefresh = s;
    }
    public void setsoaretry(String s){
        System.out.println(s);
        this.soaretry = s;
    }
    public void setsoaexpire(String s){
        System.out.println(s);
        this.soasp = s;
    }
    public void addnslist(String s){
        System.out.println(s);
        this.ns.add(s);
    }
    public void setsmaller(String s){
        System.out.println(s);
        this.smaller=s;
    }
    /*
    public void addmxlist(String s){
        System.out.println(s);
        this.mx.add(s);
    }
    public void addmxpriolist(String s){
        System.out.println(s);
        this.mxprio.add(s);
    }
*/

    public  void addmx(String mx,String prio){
        System.out.println("mx " + mx+" " +prio);
        this.mx.put(mx,prio);
    }
    public void addipsmap(String tipo,String ip){
        System.out.println("ips " +tipo +" " + ip);
        this.ips.put(tipo,ip);
    }
/*
    public void addwwwlist(String s){
        System.out.println(s);
        this.www.add(s);
    }

    public void addwwwpriolist(String s){
        System.out.println(s);
        this.wwwprio.add(s);
    }
*/
    public  void addwww(String ip,String prio){
        System.out.println("www " + ip +" " +prio);
        this.www.put(ip,prio);
    }
    public void addnamesmap(String tipo,String nome){
        System.out.println("nome " + tipo + " "+ nome);
        this.names.put(tipo,nome);
    }

    public List<String> getNS() {
        return ns;
    }
    public HashMap<String, String> getMx() {
        return mx;
    }
    public HashMap<String,String> getNames(){
        return names;
    }
    public String getTtl(){
        return ttl;
    }
    public HashMap<String,String> getIps(){
        return ips;
    }
    public void ParserCache(){
        try {
            File ficheiro = new File("testbd.txt");
            Scanner myReader = new Scanner(ficheiro);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] linha = data.split(" ");
                if (!Objects.equals(linha[0], "#")){
                    if(Objects.equals(linha[0], "TTL")) {setttl(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "DEFAULT")) {setdfault(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "SOASP")) {setsoasp(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "SOAADMIN")) {setsoaadmin(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "SOASERIAL")) {setsoaserial(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "SOAREFRESH")) {setsoarefresh(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "SOARETRY")) {setsoaretry(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "SOAEXPIRE")) {setsoaexpire(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "NS")) {
                        String aux = linha[1]+" "+linha[2];
                        addnslist(aux);
                    }
                    else if(Objects.equals(linha[0], "Smaller.@")) {setsmaller(linha[2]);}
                    else if(Objects.equals(linha[0], "@") && Objects.equals(linha[1], "MX")) {
                        String aux = linha[1]+" "+linha[2];
                        addmx(aux,linha[4]);
                    }
                    else if(Objects.equals(linha[1], "A") && !Objects.equals(linha[0], "www")) {addipsmap(linha[0],linha[2]);}
                    else if(Objects.equals(linha[1], "A") && Objects.equals(linha[0], "www")) {
                        //addwwwlist(linha[2]);
                        //addwwwpriolist(linha[4]);
                        addwww(linha[2],linha[4]);
                    }
                    else if(Objects.equals(linha[1], "CNAME")) {addnamesmap(linha[0],linha[2]);}
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong.");
        }
    }
}