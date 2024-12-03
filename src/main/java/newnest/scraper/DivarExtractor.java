package newnest.scraper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import newnest.property.DivarApartment;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class DivarExtractor {
    private WebDriver driver;
    protected Document doc;
    private String initialUrl = "https://divar.ir/s/tehran/rent-apartment/ararat?districts=301%2C68%2C943%2C85%2C360%2C91%2C67%2C934%2C86%2C1028%2C1025%2C940%2C71%2C81%2C84%2C75%2C95%2C72%2C300%2C64%2C941%2C70%2C65%2C944%2C96%2C74%2C939%2C315%2C90%2C127";
    private String filt = "&size=60-&rooms=%D8%AF%D9%88&rent=10000000-20000000&parking=true&elevator=true&credit=100000000-400000000";
    private String url = initialUrl + filt;
    private final String postEndpoint = "https://divar.ir/v/";

    public DivarExtractor() {
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        // Configure Chrome options (optional)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode to prevent GUI window (remove if GUI is needed)
        options.addArguments("--disable-gpu"); // Disable GPU for stability in headless mode
        options.addArguments("--window-size=1920,1200"); // Set window size (helpful in headless mode)
        driver = new ChromeDriver(options);


    }

    public List<DivarApartment.Post> getApartments() {
        List<String> apartmentUrls = getApartmentUrls();
        System.out.println("step1");
        List<DivarApartment.Post> apartments = extractApartments(apartmentUrls);
        driver.quit();
        return new ArrayList<>();
    }


    private List<String> getApartmentUrls() {
        WebDriverWait wait = new WebDriverWait(driver, 10, 2000);
        driver.get(url);
        List<String> urls = new ArrayList<>();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("kt-post-card__action")));
        List<WebElement> elements = driver.findElements(By.className("kt-post-card__action"));
        for (WebElement element : elements) {
            String[] parts = element.getAttribute("href").split("/");
            urls.add(parts[parts.length - 1]);
        }
        return urls;
    }


    private List<DivarApartment.Post> extractApartments(List<String> urls) {
        System.out.println("1.1");

        for (String url : urls) {
            try {
                url = postEndpoint + url;
                System.out.println(url);

                driver.get(url);/*
            WebDriverWait wait = new WebDriverWait(driver, 10,2000);
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("kt-page-title__title--responsive-sized")));
            */

                WebDriverWait wait = new WebDriverWait(driver, 15);
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.className("kt-page-title__title--responsive-sized")));

                System.out.println("step2");
                System.out.println(extractTitle());
                System.out.println("step3");
            } catch (Exception e) {
                System.err.println("Error processing URL: " + url);
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private String extractTitle() {
        return this.driver.findElement(By.className("kt-page-title__title--responsive-sized")).getText();
    }

    private String extractDistrict() {
        return this.doc.getElementsByClass("kt-page-title__subtitle kt-page-title__subtitle--responsive-sized").text();
    }

    private Elements extractFeatures() {
        return this.doc.getElementsByClass("kt-group-row-item kt-group-row-item__value kt-body kt-body--stable");
    }

    private Boolean extractElevator() {
        return this.extractFeatures().get(0).text().equals("آسانسور");
    }

    private Boolean extractParking() {
        return this.extractFeatures().get(1).text().equals("پارکینگ");
    }


    private Elements extractSpecs() {
        return this.doc.getElementsByClass("kt-group-row-item kt-group-row-item__value kt-group-row-item--info-row");
    }

    private int extractSize() {
        return Integer.parseInt(extractSpecs().get(0).text());
    }

    private int extractYear() {
        return 3;
    }

    private int extractBedroom() {
        return Integer.parseInt(extractSpecs().get(2).text());
    }

    private Elements extractConds() {
        return this.doc.getElementsByClass("kt-unexpandable-row__value");
    }


    private int extractCredit() {
        return getEnglishDigits(extractConds().get(0).text().split(" ")[0].replace("٬", ""));
    }

    private int extractRent() {
        return getEnglishDigits(extractConds().get(1).text().split(" ")[0].replace("٬", ""));

    }

    private int isInterchangeable() {
        return getEnglishDigits(extractConds().get(2).text().split(" ")[0].replace("٬", ""));

    }

    private int extractFloor() {
        return getEnglishDigits(extractConds().get(3).text());

    }

    private int getEnglishDigits(String persianDigits) {
        StringBuilder englishDigits = new StringBuilder();
        for (char c : persianDigits.toCharArray()) {
            if (Character.isDigit(c))
                englishDigits.append(Character.getNumericValue(c));
        }
        return Integer.parseInt(englishDigits.toString());

    }
}
