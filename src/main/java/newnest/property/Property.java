package newnest.property;

public class Property {
    private String url;
//    private String website;
    private String title;
    public String getUrl() {
        return url;
    }

    public int toHash() {
        return toString().hashCode();
    }
}
