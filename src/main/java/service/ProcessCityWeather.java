package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data.cities.City;
import data.cities.CityAccess;
import data.subscriptions.Subscription;
import logging.AppLogger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ProcessCityWeather
{
    private static final String apiKey = "&appid=54b8a2898ed3108c18f0ddc3dee12cb5";
    private static final String baseURL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String unit = "&units=metric";
    public static void process(int cityId, Map<Integer, Subscription> citySubs)
    {
        Gson gson = new Gson();
        City city = CityAccess.getCityById(cityId);
        if(city!= null)
        {
            String url = baseURL+"lat="+city.getLatitude()+"&lon="+city.getLongitude()+unit+apiKey;
            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    // Parse the JSON response to get the Temperature
                    String jsonResponse = response.body();
                    JsonObject res =  gson.fromJson(jsonResponse,JsonObject.class);
                    JsonObject obj = res.getAsJsonObject("main");
                    double temp = obj.get("temp").getAsDouble();
                    sendAlerts(temp, citySubs);
                } else {
                    AppLogger.logError("Error fetching weather information. RESPONSE: " + response.body());
                }
            } catch (Exception e) {
                AppLogger.logError(e.getMessage());
            }
        }
    }

    private static void sendAlerts(double temp, Map<Integer, Subscription> citySubs)
    {
        for (Map.Entry<Integer,Subscription> sub:citySubs.entrySet())
        {
            String message="";
            if(temp >= sub.getValue().getMaxTempCelsius())
            {
                message="Temperature has exceeded "+sub.getValue().getMaxTempCelsius()+" degrees at "+temp+" degrees in "+sub.getValue().getCity().getName()
                        +" ,Sending Notification to: "+sub.getValue().getUser().getEmail();
            }
            else if(temp <= sub.getValue().getMinTempCelsius())
            {
                message="Temperature has gone below "+sub.getValue().getMinTempCelsius()+" degrees at "+temp+" degrees in "+sub.getValue().getCity().getName()
                        +" ,Sending Notification to: "+sub.getValue().getUser().getEmail();
            }
            if(!message.isEmpty())
            {
                AppLogger.logAlert(message);
            }
        }
    }
}
