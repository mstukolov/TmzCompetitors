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

        String mBoots = "http://www.ecco-shoes.ru/men/shoes/boots/?newcollection=all&&pr=1&c=cl#ch";
        String mHome = "http://www.ecco-shoes.ru/men/shoes/home/?newcollection=all&&pr=1&c=cl#ch";
        String mSneakers = "http://www.ecco-shoes.ru/men/shoes/sneakers/?newcollection=all&&pr=1&c=cl#ch";
        String mMoccasins = "http://www.ecco-shoes.ru/men/shoes/moccasins/?newcollection=all&&pr=1&c=cl#ch";
        String mLowshoes = "http://www.ecco-shoes.ru/men/shoes/lowshoes/?newcollection=all&&pr=1&c=cl#ch";
        String mSandal = "http://www.ecco-shoes.ru/men/shoes/sandal/?newcollection=all&&pr=1&c=cl#ch";
        String mShoe = "http://www.ecco-shoes.ru/men/shoes/shoe/?newcollection=all&&pr=1&c=cl#ch";

        String ballerinas = "http://www.ecco-shoes.ru/women/shoes/ballerinas/?newcollection=all&&pr=1&c=cl#ch";
        String ankle = "http://www.ecco-shoes.ru/women/shoes/ankle/?newcollection=all&&pr=1&c=cl#ch";
        String boots = "http://www.ecco-shoes.ru/women/shoes/boots/?newcollection=all&&pr=1&c=cl#ch";
        String home = "http://www.ecco-shoes.ru/women/shoes/home/?newcollection=all&&pr=1&c=cl#ch";
        String sneakers = "http://www.ecco-shoes.ru/women/shoes/sneakers/?newcollection=all&&pr=1&c=cl#ch";
        String lowshoes = "http://www.ecco-shoes.ru/women/shoes/lowshoes/?newcollection=all&&pr=1&c=cl#ch";
        String lowboots = "http://www.ecco-shoes.ru/women/shoes/lowboots/?newcollection=all&&pr=1&c=cl#ch";
        String sandal = "http://www.ecco-shoes.ru/women/shoes/sandal/?newcollection=all&&pr=1&c=cl#ch";
        String highboots = "http://www.ecco-shoes.ru/women/shoes/highboots/?newcollection=all&&pr=1&c=cl#ch";
        String shoe = "http://www.ecco-shoes.ru/women/shoes/shoe/?newcollection=all&&pr=1&c=cl#ch";

        String boys = "http://www.ecco-shoes.ru/kids/boys/?newcollection=all&&pr=1&c=cl#ch";
        String girls = "http://www.ecco-shoes.ru/kids/girls/?newcollection=all&&pr=1&c=cl#ch";
        String infants = "http://www.ecco-shoes.ru/kids/infants/?newcollection=all&&pr=1&c=cl#ch";

        String bags = "http://www.ecco-shoes.ru/accessories/bags/?newcollection=all&&pr=1&c=cl#ch";
        String wallets = "http://www.ecco-shoes.ru/accessories/wallets/?newcollection=all&&pr=1&c=cl#ch";
        String belts = "http://www.ecco-shoes.ru/accessories/belts/?newcollection=all&&pr=1&c=cl#ch";
        String other = "http://www.ecco-shoes.ru/accessories/other/?newcollection=all&&pr=1&c=cl#ch";

        urls.add(mBoots);
        urls.add(mHome);
        urls.add(mSneakers);
        urls.add(mMoccasins);
        urls.add(mLowshoes);
        urls.add(mSandal);
        urls.add(mShoe);

        urls.add(ballerinas);
        urls.add(ankle);
        urls.add(boots);
        urls.add(home);

        urls.add(sneakers);
        urls.add(lowshoes);
        urls.add(lowboots);
        urls.add(sandal);
        urls.add(highboots);
        urls.add(shoe);

        urls.add(boys);
        urls.add(girls);
        urls.add(infants);

        urls.add(bags);
        urls.add(wallets);
        urls.add(belts);
        urls.add(other);



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
        //writeDB(items, prices);
    }
    private static void printPrices(String scu, String category) throws IOException {
        try {
            Document docSCU = Jsoup.connect(scu).get();
            String item = "", price = "", priceFirst = "", kindshoes = "";

            kindshoes = docSCU.select("#model_container > h1 > span").first().text();
            item = trimElement(docSCU.select("div.block > p.art").first().text());
            price = docSCU.select("dd.new").first().text().replaceAll("\\Dруб.*", "");

            //STUM 16.01.2015 Добавление зачеркнутой(первой) цены-------
            try {
                priceFirst = docSCU.select("dd.old").first().text().replaceAll("\\Dруб.*", "");
            }catch(NullPointerException ex){priceFirst = "0";}
            //-----------------------------------------------------

            Elements pElems = docSCU.select("div.main > dl");

            parseElements(item, kindshoes, Integer.valueOf(price), Integer.valueOf(priceFirst), category, pElems);

            i++;
            System.out.println("SCU #: " + item + " , " + Integer.valueOf(price.split(" ")[0])
                    + " , " + Integer.valueOf(priceFirst.split(" ")[0]) + " , "+ i);

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
                new InventTable(scu,
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

        return s.substring(s.lastIndexOf(":") + 1);
    }

}
