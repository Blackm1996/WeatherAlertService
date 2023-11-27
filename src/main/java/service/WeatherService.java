package service;

import data.subscriptions.Subscription;
import data.subscriptions.SubscriptionsAccess;

import static logging.AppLogger.*;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class WeatherService
{
    private final ExecutorService executorService;
    private final EmailService emailService;
    public WeatherService()
    {
        //intentionally make the max number of threads less than the number of cities
        executorService= Executors.newFixedThreadPool(20);
        emailService=new EmailService();
    }
    public ExecutorService getExecutorService()
    {
        return executorService;
    }
    public EmailService getEmailService()
    {
        return emailService;
    }
    public void ProcessWeather()
    {
        int i=1;
        for (Map.Entry<Integer, Map<Integer, Subscription>> citySubs:SubscriptionsAccess.getSubscriptions().entrySet())
        {
            /*Current Weather API will not return a response indicating exceeding the quota which is 60 request per minute,
               so we have to keep track of that number manually, just to be safe, we are pausing at 50 requests*/
            try {
                if(i % 50 == 0) {
                    logError("The api request quota of 60 requests/minute is almost reached, waiting one minute before continuing");
                    sleep(60000);
                }
            }catch (InterruptedException ie)
            {
                logError("Error waiting for api quota cooldown: "+ie);
            }
            executorService.submit(() -> {
                ProcessCityWeather.process(citySubs.getKey(), citySubs.getValue(), emailService);
            });
            i++;
        }
    }
}
