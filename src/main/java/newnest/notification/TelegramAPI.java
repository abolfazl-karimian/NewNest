package newnest.notification;

import newnest.property.DivarApartment;
import newnest.utils.HTTPRequest;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TelegramAPI {
    private final String ApiEndpoint = "https://api.telegram.org/bot";
    private String botToken;
    private String chatID;
    HTTPRequest request;

    public TelegramAPI(Map<String, String> configuration) {
        request = new HTTPRequest();
        this.botToken = configuration.get("bot_token");
        this.chatID = configuration.get("chat_id");
    }

    public boolean sendMessage(DivarApartment.Post apartment) {
        List<Map.Entry<String, String>> headers = List.of(
                Map.entry("Content-Type", "application/x-www-form-urlencoded")
        );
        String message = formatMsg(apartment);

        try {
            HttpResponse<String> response = request.post(postURI(), getPayload(chatID, message), headers);
            if (response == null) {
                System.out.println("Error sending Telegram message: Response is null.");
                return false;
            }

            if (response.statusCode() != 200) {
                System.out.println("Error sending Telegram message");
                System.out.println("Status Code: " + response.statusCode());
                System.out.println("Response Body: " + response.body());
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while sending Telegram message.");
            e.printStackTrace();
            return false;
        }

    }

    private String getURI(String message) {
        return String.format("%s%s/sendMessage?chat_id=%s&text=%s", ApiEndpoint, botToken, chatID, message);
    }

    private String postURI() {
        return String.format("%s%s/sendMessage", ApiEndpoint, botToken);
    }

    private String getPayload(String chatID, String message) {
        try {
            return "chat_id=" + URLEncoder.encode(chatID, StandardCharsets.UTF_8)
                    + "&parse_mode=" + URLEncoder.encode("HTML", StandardCharsets.UTF_8)
                    + "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode payload", e);
        }
    }

    public static String formatMsg(DivarApartment.Post apartment) {
        LocalDateTime now = LocalDateTime.now().plusMinutes(210);
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

        String str = String.format(
                "<pre>🔆🔆🔆            %s        🔆🔆🔆</pre>\n\n" +
                        "🏠<a href=\"%s\">%s</a>\n\n" +
                        "♦️<strong>متراژ: </strong><u><em>%d</em></u>\n" +
                        "♦️<strong>طبقه: </strong><u><em>%d</em></u>\n" +
                        "♦️<strong>تعداد اتاق: </strong><u><em>%s</em></u>\n\n" +
                        "🔷اجاره: <pre>%s</pre>\n" +
                        "🔷ودیعه: <pre>%s</pre>\n\n" +
                        "⚪️<strong>سال ساخت: </strong><u>%d</u>\n" +
                        "◼️پارکینگ: %s\n" +
                        "◼️آسانسور: %s\n",
                formattedTime,
                apartment.getUrl(), apartment.getTitle(),
                apartment.getRealEstateFields().getSize(),
                apartment.getRealEstateFields().getFloor(),
                apartment.getRealEstateFields().getRooms(),
                numberFormat.format(apartment.getRealEstateFields().getRent().getValue()),
                numberFormat.format(apartment.getRealEstateFields().getCredit().getValue()),
                apartment.getRealEstateFields().getYear(),
                apartment.getRealEstateFields().isParking() ? "✅" : "❌",
                apartment.getRealEstateFields().isElevator() ? "✅" : "❌"
        );

        return str;
    }


}
