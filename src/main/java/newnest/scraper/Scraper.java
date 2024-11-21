package newnest.scraper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public abstract class Scraper {
    protected String website;
    protected Document doc;

    protected abstract List<?> scrape() throws IOException;
}
