import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Hello world!");
        ServerP sp = new ServerP();
        sp.ParserSp();
        System.out.println("Sp done!");
        ServerS ss = new ServerS();
        ss.ParserSs();
        System.out.println("Ss done!");
        Cache ca = new Cache();
        ca.ParserCache();
        System.out.println("Cache done!");


    }
}