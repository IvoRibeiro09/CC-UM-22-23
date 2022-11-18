import java.io.File;
import java.util.*;


public class CacheSS {

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


        public CacheSS(){
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

        public void setAllva(HashMap<String,String> mapallva){
            this.allva=mapallva;
        }
        public void setns(HashMap<String,String> mapns){
            this.ns = mapns;
        }
        public void setmx(HashMap<String,String> mapmx){
           this.mx = mapmx;
        }
        public void setAips(HashMap<String,String> mapaips){
            this.Aips = mapaips;
        }
        public void setCnames(HashMap<String,String> mapcnames){
            this.cnames = mapcnames;
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

        public void ParserCacheSS(){
            try {
              CacheSP sp = new CacheSP();
                setdfault(sp.getDfault());
                setsoasp(sp.getSoasp());
                setsoaadmin((sp.getSoaadmin()));
                setsoaserial((sp.getSoaserial()));
                setsoarefresh(sp.getSoarefresh());
                setsoaretry(sp.getSoaretry());
                setsoaexpire(sp.getSoaexpire());
                setAllva(sp.getAllva());
                setns(sp.getNs());
                setmx(sp.getMx());
                setAips(sp.getAIps());
                setCnames(sp.getCnames());
            } catch (Exception e) {
                System.out.println("!!!!Erro no acesso á cache do SP!!!!");
            }
        }
}

