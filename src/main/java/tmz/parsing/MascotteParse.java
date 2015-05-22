package tmz.parsing;

import org.jsoup.Connection;
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
import java.util.*;


public class MascotteParse {

    public static ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    public static Integer i = 0, timeoutErrors= 0;
    public static String category = "";

    public static List<InventTable> items = new ArrayList<InventTable>();
    public static List<PricesCompetitors> prices = new ArrayList<PricesCompetitors>();

    public static Map<String,String> cookiesMap = new HashMap();

    public void run() throws IOException {

        List<String> urls = new ArrayList<String>();

        System.out.println("Start parse MASCOTTE...");

        String main = "https://shop.mascotte.ru/";

//        String mens = "https://shop.mascotte.ru/obuv/dlya-muzhchin/vesna-leto-2015";
        String mBags = "https://shop.mascotte.ru/sumki/dlya-muzhchin/vesna-leto-2015";
        String mAccessories = "https://shop.mascotte.ru/aksessuari/dlya-muzhchin/vesna-leto-2015";
        String mAccompanying = "https://shop.mascotte.ru/aksessuary-dlya-obuvi/dlya-muzhchin/";

//        String womens = "https://shop.mascotte.ru/obuv/dlya-zhenshchin/vesna-leto-2015";
        String wBags = "https://shop.mascotte.ru/sumki/dlya-zhenshchin/vesna-leto-2015";
        String wAccessories = "https://shop.mascotte.ru/aksessuari/dlya-zhenshchin/vesna-leto-2015";
        String wAccompanying = "https://shop.mascotte.ru/aksessuary-dlya-obuvi/dlya-zhenshchin/";
//
//        urls.add(mens);
//        urls.add(womens);

        urls.add(mBags);
        urls.add(mAccessories);
        urls.add(wBags);
        urls.add(wAccessories);

        urls.add(wAccompanying);
        urls.add(mAccompanying);

        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/baletki/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/bosonozhki/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/botiloni/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/botinki/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/mokasini/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/polubotinki/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/sabo/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/sandalii/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/sapogi/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-zhenshchin/tufli/city597");


        urls.add("https://shop.mascotte.ru/obuv/dlya-muzhchin/botinki/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-muzhchin/mokasini/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-muzhchin/polubotinki/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-muzhchin/sabo/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-muzhchin/sandalii/city597");
        urls.add("https://shop.mascotte.ru/obuv/dlya-muzhchin/sapogi/city597");


        Connection.Response res = Jsoup.connect(main)
                .method(Connection.Method.POST)
                .execute();

        cookiesMap.put("PHPSESSID", res.cookie("PHPSESSID"));
        cookiesMap.put("BPC", "b5fb3eb9a5aaa1f3da01b6f689eace13");

        for(String url : urls){

            if (url.contains("/dlya-muzhchin")) {category = "мужская";}
            if  (url.contains("/dlya-zhenshchin")){category = "женская";}

            Document document = Jsoup.connect(url).cookies(cookiesMap).timeout(100 * 10000000).get();
            Element activePage = document.select("ul.pagination > li.active > a[href]").first();

            //parsePage(url);
            if(activePage != null){parsePage("https://shop.mascotte.ru" + activePage.attr("href"));}
            else{parsePage(url);}
        }
        writeDB(items, prices);
    }
    public static String goNextPage(String url) throws IOException {
        Document document = Jsoup.connect(url).cookies(cookiesMap).timeout(100 * 10000000).get();
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

            Document activePage = Jsoup.connect(url).cookies(cookiesMap).timeout(100 * 10000000).get();
            Elements links = activePage.select("div > a[href].thumbnail");

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

        Document docSCU = Jsoup.connect(scu).cookies(cookiesMap).get();
        String item = "", price = "", priceFirst = "0", kindshoes = "";


        item = trimArtikul(docSCU.select("ol > li.active").text());
        kindshoes =  docSCU.select("a[href]").get(33).text();
        price = docSCU.select("div.main-price").text().replaceAll("\\D","");

        //STUM 16.01.2015 Добавление зачеркнутой(первой) цены
        try {
            Elements priceFirstElements = docSCU.select("div.old-price");
            if(!priceFirstElements.isEmpty()){
                priceFirst = priceFirstElements.text().replaceAll("\\D", "");
            }else{priceFirst = price;}
        }catch(NullPointerException ex){priceFirst = price;}
        //-----------------------------------------------------

        Element table = docSCU.select("table").get(0); //select the first table.
        Elements  pElems = table.select("tr");

        try{

            parseElements(item, kindshoes, Integer.valueOf(price), Integer.valueOf(priceFirst), category, pElems);
                i++;
            System.out.println("SCU #: " + item + " , " + Integer.valueOf(price.split(" ")[0])
                + " , " + Integer.valueOf(priceFirst.split(" ")[0]) + " , "+ i);
        }catch (NumberFormatException ex){System.out.println("NumberFormatException for SCU: " + item);}
    }
    public static void parseElements(String scu, String kindshoes,
                                       Integer price, Integer priceFirst,
                                       String category, Elements pElems) throws UnsupportedEncodingException {
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
                                      price,             //цена
                                      priceFirst        //Первая цена
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

    public static String  trimArtikul(String s) {
        return s.substring(s.lastIndexOf("Артикул") + 8);
    }

    public static String  trimElement(String s){

        return s.substring(s.lastIndexOf(":") + 1);
    }


}
