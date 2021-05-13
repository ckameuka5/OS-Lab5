import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DiggerThread extends Thread{
    public static boolean exploringEnd = false;

    private RefsQueue queue;
    private String currentRef;
    private String exploringRef;
    private int pagesToVisitNum;

    DiggerThread(RefsQueue queue, String startRef, int pagesToVisitNum) {
        this.queue = queue;
        exploringRef = startRef;
        this.pagesToVisitNum = pagesToVisitNum;
    }

    @Override
    public void run() {
        while ((pagesToVisitNum--) > 0) {
            try {
                Document doc = Jsoup.connect(exploringRef).get();

                /*<div class="news-entry *****">*/
                Elements news = doc.select("div.news-entry > a");

                for (Element line : news) {
                    currentRef = line.attr("href");
                    if(currentRef.contains("tut")) {
                        System.out.println(currentRef);
                        queue.put(currentRef);
                        exploringRef = currentRef;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        exploringEnd = true;
    }
}
