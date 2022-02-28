package TaskTwo;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskTwo {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(getJSONPriceOnlineSim(
                getPriceOnlineSim("https://onlinesim.ru/price-list?country=?&type=proxy")));
    }

    private static JSONObject getJSONPriceOnlineSim(Map<String, Map<String, String>> priceOnlineSim) {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Map<String, String>> mCountry : priceOnlineSim.entrySet()) {
            jsonObject.put(mCountry.getKey(), getMapStrIntByMapStrStr(mCountry.getValue()));
        }
        return jsonObject;
    }

    public static Map<String, Map<String, String>> getPriceOnlineSim(String str)
            throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe"); // Параметры нужно будет задать в ручную. Chromedriver заработал этот https://chromedriver.storage.googleapis.com/98.0.4758.102/chromedriver_win32.zip

        WebDriver driver = new ChromeDriver();
        driver.get(str);

        driver.findElements(By.className("country-name"));

        List<WebElement> countryElements = driver.findElements(By.className("country-name"));

        Map<String, Map<String, String>> map = new HashMap<>();

        for (WebElement countryElement : countryElements) {
            countryElement.click();
            List<WebElement> priceNames = driver.findElements(By.className("price-name"));
            List<WebElement> priseTexts = driver.findElements(By.className("price-text"));

            Map<String, String> mapPrice = new HashMap<>();

            for (int i = 0; i < priseTexts.size(); i++) {
                mapPrice.put(priceNames.get(i).getText(), priseTexts.get(i).getText());
            }

            map.put(countryElement.getText(), mapPrice);
        }
        driver.quit();

        return map;
    }

    private static Map<String, Integer> getMapStrIntByMapStrStr(Map<String, String> value) {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, String> map : value.entrySet()) {
            result.put(map.getKey(), getIntByString(map.getValue()));
        }
        return result;
    }

    private static Integer getIntByString(String value) {
        return Integer.valueOf(value.substring(0, value.length() - 1));
    }
}
