package data.subscriptions;

import data.cities.City;
import data.cities.CityAccess;
import data.users.User;
import data.users.UsersAccess;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionsAccess
{
    private static final Map<Integer, Map<Integer, Subscription>> subscriptions=new HashMap<>();

    public static Map<Integer, Map<Integer, Subscription>> getSubscriptions()
    {
        return subscriptions;
    }
    public static Subscription getUserSubscription(int userId)
    {
        for (Map.Entry<Integer, Map<Integer, Subscription>> entry:subscriptions.entrySet())
        {
            if(entry.getValue().containsKey(userId))
                return entry.getValue().get(userId);
        }
        return null;
    }

    public static int subscribe(int userId ,int cityId, int maxTemp, int minTemp)
    {
        User user = UsersAccess.getUserById(userId);
        City city = CityAccess.getCityById(cityId);
        if(city!=null && user!=null) {
            Subscription prev = getUserSubscription(userId);
            if(prev == null)
            {
                Map<Integer, Subscription> citySubs = subscriptions.computeIfAbsent(cityId, k -> new HashMap<>());
                citySubs.put(userId, new Subscription(user, city, maxTemp, minTemp));
                return 1;
            }
            return 0;
        }
        return -1;
    }

    public static boolean unsubscribe(int userId)
    {
        Subscription subscription = getUserSubscription(userId);
        if(subscription != null)
        {
            Map<Integer, Subscription> citySubs = subscriptions.get(subscription.getCity().getId());
            citySubs.remove(userId);
            if(citySubs.isEmpty())
                subscriptions.remove(subscription.getCity().getId());
            return true;
        }
        return false;
    }

    public Map<Integer, Subscription> getCitySubscriptions(int cityId)
    {
        return subscriptions.get(cityId);
    }
}
