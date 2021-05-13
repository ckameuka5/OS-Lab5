import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;

public class CounterThread  extends Thread{

    private RefsQueue queue;
    private WordsContainer wordsContainer;
    private int periodInDays;

    CounterThread(RefsQueue queue, WordsContainer wordsContainer, int periodInDays) {
        this.queue = queue;
        this.wordsContainer = wordsContainer;
        this.periodInDays = periodInDays;
    }

    @Override
    public void run() {
        while (true) {

            String ref = queue.get();
            if(ref == null) break;

            try {
                Document doc = Jsoup.connect(ref).get();

                System.out.println(doc.title());

                //<time itemprop="datePublished" datetime="2021-05-07T09:50:00+03:00">7 мая 2021 в 9:50</time>
                Element dateTime = doc.select("time").first();

                if (dateTime != null && dateTime.attr("itemprop").equals("datePublished")) {
                    LocalDate date = LocalDate.parse(dateTime.attr("datetime").substring(0, 10));
                    if (date.until(LocalDate.now()).getDays() < periodInDays) {
                        ;
                        System.out.println("\u001B[32mPage published: " + date + ".\tToday: " + LocalDate.now() + ". Suitable page!\u001B[0m");

                        //<div id="article_body" itemprop="articleBody" class="">
                        Elements textBlock = doc.select("div#article_body > p");

                        for (Element textLine : textBlock) {
                            for (String word : textLine.text().split("[\s/,/.]"))
                                wordsContainer.registerWord(word.toLowerCase());
                        }
                    } else {
                        System.out.println("\u001B[35mPage published: " + date + ".\tToday: " + LocalDate.now() + ". Old page!\u001B[0m");
                    }
                } else {
                    System.out.println("Incorrect date parsing");
                    //return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
