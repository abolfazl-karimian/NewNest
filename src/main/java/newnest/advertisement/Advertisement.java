package newnest.advertisement;

public class Advertisement {
    private String url;
    private String website;
    private String title;
    public String getUrl() {
        return url;
    }

    public int getHashedUrl() {
        return url.hashCode();
    }
}
