package tmz;


import tmz.parsing.CarloPazoliniParse;
import tmz.parsing.EccoParse;
import tmz.parsing.EconikaParse;
import tmz.parsing.MascotteParse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {


    public static Date startDate, endDate;

    public static void main(String[] args) throws IOException {

        SimpleDateFormat df = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        startDate = new Date();
        System.out.println("Start parsing schedule at: " + df.format(startDate));


//        System.setProperty("http.proxyHost", "proxyn.mshoes.local");
//        System.setProperty("http.proxyPort", "3128");
//        System.setProperty("http.proxyUser", "stukolov_m");
//        System.setProperty("http.proxyPassword", "4926");

        //System.setProperty("sun.net.client.defaultReadTimeout", "30000000");
        //System.setProperty("sun.net.client.defaultConnectTimeout", "30000000");


//        CarloPazoliniParse carloPazoliniParse = new CarloPazoliniParse();
//        carloPazoliniParse.run();

        MascotteParse mascotteParse = new MascotteParse();
        mascotteParse.run();

        EccoParse eccoParse = new EccoParse();
        eccoParse.run();

        EconikaParse econikaParse = new EconikaParse();
        econikaParse.run();
//
        endDate = new Date();
        long totalTime = (endDate.getTime() - startDate.getTime())/(1000);
        System.out.println("End parsing schedule at: " + df.format(endDate));
        System.out.println("Total time: " + totalTime);


    }

}
