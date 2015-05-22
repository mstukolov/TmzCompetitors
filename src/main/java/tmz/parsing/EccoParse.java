package tmz.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import tmz.model.InventTable;
import tmz.model.PricesCompetitors;
import tmz.service.InventTableService;
import tmz.service.PricesCompetitorsService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EccoParse {

    public static ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    public static Integer i = 0, timeoutErrors= 0;
    public static String category = "";

    public static List<InventTable> items = new ArrayList<InventTable>();
    public static List<PricesCompetitors> prices = new ArrayList<PricesCompetitors>();


    public void run() throws IOException {

        List<String> urls = new ArrayList<String>();

        System.out.println("Start parse Ecco...");

        String women = "http://www.ecco-shoes.ru/women/shoes/all/?newcollection=1&type=3937,3970,4117,3922,5409,3912,4059&&c=cl#ch";
        String women2 = "http://www.ecco-shoes.ru/women/shoes/all/?newcollection=1&type=3882,3958,3908,3941&&c=cl#ch";
        String women3 = "http://www.ecco-shoes.ru/women/shoes/all/?newcollection=1&type=3980,3982&&c=cl#ch";


        String men = "http://www.ecco-shoes.ru/men/shoes/all/?newcollection=1&type=3922,5409,3912,4059&&c=cl#ch";
        String men2 = "http://www.ecco-shoes.ru/men/shoes/all/?newcollection=1&type=3882&&n=1&c=cl#ch";
        String men3 = "http://www.ecco-shoes.ru/men/shoes/all/?newcollection=1&type=3908,3980&&n=1&c=cl#ch";

        String bags = "http://www.ecco-shoes.ru/accessories/bags/?newcollection=1&&c=cl#ch";
        String wallets = "http://www.ecco-shoes.ru/accessories/wallets/?newcollection=1&&c=cl#ch";
        String accessories = "http://www.ecco-shoes.ru/accessories/belts/?newcollection=1&&c=cl#ch";

//        String womenAccessories = "http://www.ecco-shoes.ru/women/accessories/?newcollection=1&&c=cl#ch";
//        String menAccessories = "http://www.ecco-shoes.ru/men/accessories/?newcollection=1&&c=cl#ch";


        urls.add(women);
        urls.add(women2);
        urls.add(women3);

        urls.add(men);
        urls.add(men2);
        urls.add(men3);

        urls.add(bags);
        urls.add(wallets);
        urls.add(accessories);



        for(String url : urls){

            if       (url.contains("/men/")) {category = "мужская";}
            else if  (url.contains("/women/")){category = "женская";}
            else if  (url.contains("/kids/")){category = "детская";}

            System.out.println("Start parse URL = " + url);

            Document document = Jsoup.connect(url).timeout(100 * 10000000).get();


            Elements links = document.select("ul.models > li > a[href]");
            for (Element lnk : links) {
                try {
                    printPrices(lnk.attr("abs:href"), category);
                }catch(java.net.SocketTimeoutException ex){
                    System.out.println("Read Timeout Exception");
                    timeoutErrors++;
                    //break;
                }
            }
        }
        writeDB(items, prices);
    }
    private static void printPrices(String scu, String category) throws IOException {
        try {
            Document docSCU = Jsoup.connect(scu).get();
            String item = "", price = "", priceFirst = "", kindshoes = "";

            kindshoes = docSCU.select("#model_container > h1").first().text().split("\\s")[0];
            item = trimElement(docSCU.select("div.block > p.art").first().text());
            price = docSCU.select("dd.new").first().text().replaceAll("\\Dруб.*", "");

            //STUM 16.01.2015 Добавление зачеркнутой(первой) цены-------
            try {
                priceFirst = docSCU.select("dd.old").first().text().replaceAll("\\Dруб.*", "");
            }catch(NullPointerException ex){priceFirst = price;}
            //-----------------------------------------------------

            Elements pElems = docSCU.select("div.main > dl");

            parseElements(item, kindshoes, Integer.valueOf(price), Integer.valueOf(priceFirst), category, pElems);

            i++;
            System.out.println("SCU #: " + item + " , " + Integer.valueOf(price.split(" ")[0])
                    + " , " + Integer.valueOf(priceFirst.split(" ")[0]) + " , "+"," + kindshoes + ","+ i);

        }catch (java.net.SocketException ex){System.out.println("java.net.SocketException: Connection reset");}
    }
    public static void parseElements(String scu, String kindshoes,
                                       Integer price, Integer priceFirst,
                                       String category, Elements pElems) throws UnsupportedEncodingException {
        String upperMaterial = new String("Верх".getBytes("UTF8"));
        String soleMaterial = new String("Подошва".getBytes("UTF8"));
        String liningMaterial = new String("Подкладка".getBytes("UTF8"));
        String countryElement = new String("Страна производства".getBytes("UTF8"));

        String upper= "", lining = "", sole = "", country = "";


        for(Element element: pElems){
            if(element.text().indexOf(upperMaterial) != -1){upper = trimElement(element.select("span.show_1").text());}
            else if(element.text().indexOf(soleMaterial) != -1){sole =  trimElement(element.select("span.show_1").text());}
            else if(element.text().indexOf(liningMaterial) != -1){lining =  trimElement(element.select("span.show_1").text());}
            else if(element.text().indexOf(countryElement) != -1){country = trimElement(element.select("span.show_1").text());}

        }
        PricesCompetitors nPrice =
                new PricesCompetitors("Ecco",  //Бренд
                                      scu,              //Артикул
                                      new Date(),       //Дата цены
                                      price,            //цена
                                      priceFirst        //Первая цена
                );

        InventTable inventTable =
                new InventTable(scu.replaceAll(" ", ""),
                                "Ecco",
                                new String(category.getBytes(),"utf-8"),
                                kindshoes,
                                new String(upper.getBytes(), "utf8"),
                                lining,
                                "",
                                "",
                                sole,
                                country,
                                new Date());
        items.add(inventTable);
        prices.add(nPrice);
    }

    public void writeDB(List<InventTable> items, List<PricesCompetitors> prices){

        SimpleDateFormat df = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        System.out.println("Налачась запись в базу данных:  " + df.format(new Date()));
        System.out.println("Кол-во загруженных цен: " + prices.size());

        InventTableService inventTableService = (InventTableService) context.getBean("inventTableService");
        PricesCompetitorsService priceService = (PricesCompetitorsService) context.getBean("pricesCompetitorsService");

        //Создание нового артикула
        for(InventTable inventTable : items) {
            if(inventTableService.findScu(inventTable) == null){inventTableService.persistScu(inventTable);}
        }
        //Запись цены
        for(PricesCompetitors price : prices) {
            priceService.persistPrices(price);
        }
        System.out.println("Закончилась запись в базу данных: " + df.format(new Date()));
        System.out.println("Кол-во не загруженных SCU: " + timeoutErrors);

    }
    public static String  trimElement(String s){

        return (s.substring(s.lastIndexOf(":") + 1)).replaceAll(" ", "");
    }

}
