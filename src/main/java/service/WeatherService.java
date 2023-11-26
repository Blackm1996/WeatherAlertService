package service;

import data.subscriptions.Subscription;
import data.subscriptions.SubscriptionsAccess;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherService
{
    private final ExecutorService executorService;

    public WeatherService()
    {
        //intentionally make the max number of threads less than the number of cities
        executorService= Executors.newFixedThreadPool(20);
    }
    public void ProcessWeather()
    {
        System.out.println("Starting Process");
        for (Map.Entry<Integer, Map<Integer, Subscription>> citySubs:SubscriptionsAccess.getSubscriptions().entrySet())
        {
            executorService.submit(() -> {
                ProcessCityWeather.process(citySubs.getKey(), citySubs.getValue());
            });

        }
    }
}
