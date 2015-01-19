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


public class MascotteParse {

    public static ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    public static Integer i = 0, timeoutErrors= 0;
    public static String category = "";

    public static List<InventTable> items = new ArrayList<InventTable>();
    public static List<PricesCompetitors> prices = new ArrayList<PricesCompetitors>();

    public void run() throws IOException {

        List<String> urls = new ArrayList<String>();

        System.out.println("Start parse MASCOTTE...");


        String mens = "https://shop.mascotte.ru/obuv/dlya-muzhchin";
        String mBags = "https://shop.mascotte.ru/sumki/dlya-muzhchin/";
        String mAccessories = "https://shop.mascotte.ru/aksessuari/dlya-muzhchin/";
        String mAccompanying = "https://shop.mascotte.ru/aksessuary-dlya-obuvi/dlya-muzhchin/";

        String womens = "https://shop.mascotte.ru/obuv/dlya-zhenshchin";
        String wBags = "https://shop.mascotte.ru/sumki/dlya-zhenshchin/";
        String wAccessories = "https://shop.mascotte.ru/aksessuari/dlya-zhenshchin/";
        String wAccompanying = "https://shop.mascotte.ru/aksessuary-dlya-obuvi/dlya-zhenshchin/";


        urls.add(mens);
        urls.add(mBags);
        urls.add(mAccessories);
        urls.add(mAccompanying);
        urls.add(womens);
        urls.add(wBags);
        urls.add(wAccessories);
        urls.add(wAccompanying);

        for(String url : urls){

            if (url.contains("/dlya-muzhchin")) {category = "мужская";}
            if  (url.contains("/dlya-zhenshchin")){category = "женская";}

            Document document = Jsoup.connect(url).timeout(100 * 10000000).get();
            Element activePage = document.select("ul.pagination > li.active > a[href]").first();

            //parsePage(url);
            if(activePage != null){parsePage("https://shop.mascotte.ru" + activePage.attr("href"));}
            //else{parsePage(url);}
        }
        //writeDB(items, prices);
    }
    public static String goNextPage(String url) throws IOException {
        Document document = Jsoup.connect(url).timeout(100 * 10000000).get();
        Element activePage = document.select("ul.pagination > li.active > a[href]").first();
        Elements pages = document.select("ul.pagination > li > a[href]");

        String next = null;

        for(Element pgs : pages) {
            if(Integer.valueOf(pgs.attr("data-page"))
                    > Integer.valueOf(activePage.attr("data-page"))) {
                next = "https://shop.mascotte.ru" + pgs.attr("href");
                break;
            }
        }
        return next;
    }
    private static void parsePage(String url) throws IOException {
            System.out.println("Active page is: " + url);

            Document activePage = Jsoup.connect(url).timeout(100 * 10000000).get();
            Elements links = activePage.select("a[href].thumbnail");
            for (Element lnk : links) {
                try {
                     printPrices(lnk.attr("abs:href"), category);
                }catch(java.net.SocketTimeoutException ex){
                    System.out.println("Read Timeout Exception");
                    timeoutErrors++;
                }
            }
        goNextPage(url);
        if(goNextPage(url) != null){parsePage(goNextPage(url));}
    }
    private static void printPrices(String scu, String category) throws IOException {

        Document docSCU = Jsoup.connect(scu).get();
        String item = "", price = "", priceFirst = "", kindshoes = "";


        item = trimArtikul(docSCU.select("ol > li.active").text());
        kindshoes =  docSCU.select("a[href]").get(33).text();
        price = docSCU.select("div.main-price").text().replaceAll("\\D","");

        //STUM 16.01.2015 Добавление зачеркнутой(первой) цены
        try {
            priceFirst = docSCU.select("div.old-price").text().replaceAll("\\D", "");
        }catch(NullPointerException ex){priceFirst = "0";}
        //-----------------------------------------------------

        Element table = docSCU.select("table").get(0); //select the first table.
        Elements  pElems = table.select("tr");

        parseElements(item, kindshoes, Integer.valueOf(price), category, pElems);

        i++;
        System.out.println("SCU #: " + item + " , " + Integer.valueOf(price.split(" ")[0])
                + " , " + Integer.valueOf(priceFirst.split(" ")[0]) + " , "+ i);
    }
    public static void parseElements(String scu, String kindshoes,
                                       Integer price, String category, Elements pElems) throws UnsupportedEncodingException {
        String upperMaterial = new String("Материал".getBytes("UTF8"));
        String soleMaterial = new String("Материал подошвы".getBytes("UTF8"));
        String liningMaterial = new String("Материал подкладки".getBytes("UTF8"));
        String countryElement = new String("Страна".getBytes("UTF8"));

        String upper= "", lining = "", sole = "", country = "";

        for(Element element: pElems){
            Elements tds = element.select("td");
            String attribute = tds.get(0).text();
            String value = tds.get(1).text();

            if(attribute.equals(upperMaterial)){upper = value;}
            else if(attribute.equals(soleMaterial)){sole =  value;}
            else if(attribute.equals(liningMaterial)){lining =  value;}
            else if(attribute.equals(countryElement)){country = value;}

        }
        PricesCompetitors nPrice =
                new PricesCompetitors("Mascotte",  //Бренд
                                      scu,              //Артикул
                                      new Date(),       //Дата цены
                                      price             //цена
                );
        InventTable inventTable =
                new InventTable(scu,
                                "Mascotte",
                                new String(category.getBytes(),"utf-8"),
                                kindshoes,
                                new String(upper.getBytes(), "utf8"),
                                lining,
                                "",
                                "",
                                sole,
                                country,
                                new Date());

        System.out.println("SCU #: " + inventTable.getScu() + ","  + inventTable.getCategory() + ",#" + i);
        items.add(inventTable);
        //prices.add(nPrice);
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

    public static String  trimArtikul(String s) {
        return s.substring(s.lastIndexOf("Артикул") + 8);
    }

    public static String  trimElement(String s){

        return s.substring(s.lastIndexOf(":") + 1);
    }


}
