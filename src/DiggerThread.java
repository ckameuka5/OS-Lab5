import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;

public class DiggerThread extends Thread{
    public static boolean exploringEnd = false;
    public static boolean countingEnd = false;

    private RefsQueue queue;
    private WordsContainer wordsContainer;
    private int periodInDays;

    DiggerThread(RefsQueue queue, WordsContainer wordsContainer, int periodInDays) {
        this.queue = queue;
        this.wordsContainer = wordsContainer;
        this.periodInDays = periodInDays;
    }

    @Override
    public void run() {
        while (!countingEnd) {
            Document doc = null;
            String ref = queue.get();
            if(ref == null) continue;
            try {
                doc = Jsoup.connect(ref).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(doc.title());

            //<div id="article_body" itemprop="articleBody" class="">
            Elements textBlock = doc.select("div#article_body > p");

            if (!exploringEnd) {
                /*<div class="news-entry *****">*/
                Elements hrefs = doc.select("div.news-entry > a");

                for (Element textLine : textBlock.select("a")) {
                    queue.put(textLine.attr("href"));
                }

                for (Element href : hrefs) {
                    queue.put(href.attr("href"));
                }
            }

            //<time itemprop="datePublished" datetime="2021-05-07T09:50:00+03:00">7 мая 2021 в 9:50</time>
            Element dateTime = doc.select("time").first();

            if (dateTime != null && dateTime.attr("itemprop").equals("datePublished")) {
                LocalDate date = LocalDate.parse(dateTime.attr("datetime").substring(0, 10));
                if (date.until(LocalDate.now()).getDays() < periodInDays) {
                    System.out.println(date);

                    for (Element textLine : textBlock) {
                        for (String word : textLine.text().split(" "))
                            wordsContainer.registerWord(word.toLowerCase());
                    }
                } else {
                    System.out.println("Page published: " + date + ".\tToday: " + LocalDate.now() + ". Old Page!");
                }
            } else {
                System.out.println("Incorrect date parsing");
                //return;
            }
        }
    }
}
