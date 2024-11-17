package newnest.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public abstract class Scraper {
    private String website;

    protected abstract List<?> scrape() throws IOException;
}
