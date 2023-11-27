package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data.cities.City;
import data.cities.CityAccess;
import data.subscriptions.Subscription;
import data.subscriptions.SubscriptionsAccess;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static logging.AppLogger.*;

public class ProcessCityWeather
{
    private static final String apiKey = "&appid=54b8a2898ed3108c18f0ddc3dee12cb5";
    private static final String baseURL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String unit = "&units=metric";
    public static void process(int cityId, Map<Integer, Subscription> citySubs, EmailService emailService)
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
                //if success
                if (response.statusCode() == 200) {
                    // Parse the JSON response to get the Temperature
                    String jsonResponse = response.body();
                    JsonObject res =  gson.fromJson(jsonResponse,JsonObject.class);
                    JsonObject obj = res.getAsJsonObject("main");
                    double temp = obj.get("temp").getAsDouble();
                    sendAlerts(temp, citySubs,emailService);
                } else {
                    // data not found in openweathermap's database or wrong coordinates, we should delete the city from our database (from hashmap)
                    if(response.statusCode() == 400)
                    {
                        logError("Error fetching weather information. Error in the coordinates for "+ CityAccess.getCityById(cityId).getName()+
                                " or they do not match any data in openweathermap's database, deleting the city and it's subscriptions. Returned response: " + response.body());
                        SubscriptionsAccess.getSubscriptions().remove(cityId);
                        CityAccess.getCities().remove(cityId);
                    }
                    else
                        logError("Error fetching weather information. RESPONSE: " + response.body());
                }
            } catch (Exception e) {
                logError(e.getMessage());
            }
        }
    }

    private static void sendAlerts(double temp, Map<Integer, Subscription> citySubs, EmailService emailService)
    {
        for (Map.Entry<Integer,Subscription> sub:citySubs.entrySet())
        {
            String message="";
            String subject="";
            String logMessage="";
            if(temp >= sub.getValue().getMaxTempCelsius())
            {
                logMessage="Temperature has exceeded "+sub.getValue().getMaxTempCelsius()+" degrees at "+temp+" degrees in "+sub.getValue().getCity().getName()
                        +" ,Sending Notification to: "+sub.getValue().getUser().getEmail();
                message="Temperature has exceeded "+sub.getValue().getMaxTempCelsius()+" degrees at "+temp+" degrees in "+sub.getValue().getCity().getName();
                subject = sub.getValue().getCity().getName()+" - Temperature has risen";
            }
            else if(temp <= sub.getValue().getMinTempCelsius())
            {
                logMessage="Temperature has gone below "+sub.getValue().getMinTempCelsius()+" degrees at "+temp+" degrees in "+sub.getValue().getCity().getName()
                        +" ,Sending Notification to: "+sub.getValue().getUser().getEmail();
                message="Temperature has gone below "+sub.getValue().getMinTempCelsius()+" degrees at "+temp+" degrees in "+sub.getValue().getCity().getName();
                subject= sub.getValue().getCity().getName()+" - Temperature has dropped";
            }
            if(!message.isEmpty())
            {
                emailService.senEmail(sub.getValue().getUser().getEmail(), subject, message);
                logAlert(logMessage);
            }
        }
    }
}
