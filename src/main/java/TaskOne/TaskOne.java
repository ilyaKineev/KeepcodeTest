package TaskOne;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class TaskOne {

    public static void main(String[] args) {
        String urlGetFreeCountryList = "https://onlinesim.ru/api/getFreeCountryList";
        String urlGetFreePhoneList =
                "https://onlinesim.ru/api/getFreePhoneList?country=";
        Map<Integer, String> mapUrlGetFreeCountryList = getMapCountry(
                getJSONString(urlGetFreeCountryList));

        Map<String, FreePhone> mapUrlGetFreePhoneList = new HashMap<String, FreePhone>();

        for (Map.Entry<Integer, String> entry : mapUrlGetFreeCountryList.entrySet()) {
            mapUrlGetFreePhoneList.putAll(getFreePhone(
                    getJSONString(urlGetFreePhoneList + entry.getKey())));
        }

        for (Map.Entry<String, FreePhone> entry : mapUrlGetFreePhoneList.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    static String getJSONString(String string) {
        URL url = null;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        con.setRequestProperty("Content-Type", "application/json");

        StringBuilder content;
        try (final BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    static Map<Integer, String> getMapCountry(String string) {
        JSONObject obj = new JSONObject(string);
        int responseStatus = obj.getInt("response");

        if (responseStatus == 0) {
            return null;
        }

        JSONArray jsonArray = obj.getJSONArray("countries");
        Map<Integer, String> map = new HashMap<>();
        for (Object jsonObject : jsonArray) {
            JSONObject o = new JSONObject(jsonObject.toString());
            map.put(o.getInt("country"), o.getString("country_text"));
        }

        return map;
    }

    private static Map<String, FreePhone> getFreePhone(String string) {
        JSONObject obj = new JSONObject(string);
        int responseStatus = obj.getInt("response");

        if (responseStatus == 0) {
            return null;
        }

        Map<String, FreePhone> map = new HashMap<>();

        JSONArray jsonArray = obj.getJSONArray("numbers");

        for (Object jsonObject : jsonArray) {
            JSONObject o = new JSONObject(jsonObject.toString());
            FreePhone freePhone = new FreePhone(o.getString("number"), o.getInt("country"),
                    o.getString("updated_at"), o.getString("data_humans"), o.getString("full_number"),
                    o.getString("country_text"), o.getString("maxdate"), o.getString("status"));
            map.put(o.getString("number"), freePhone);
        }

        return map;
    }

}
