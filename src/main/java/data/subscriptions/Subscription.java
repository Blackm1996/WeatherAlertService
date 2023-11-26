package data.subscriptions;

import data.cities.City;
import data.users.User;

public class Subscription
{
    private final User user;
    private final City city;
    private final int maxTempCelsius;
    private final int minTempCelsius;

    public Subscription(User user, City city, int maxTempCelsius, int minTempCelsius) {
        this.user = user;
        this.city = city;
        this.maxTempCelsius = maxTempCelsius;
        this.minTempCelsius = minTempCelsius;
    }

    public User getUser() {
        return user;
    }

    public City getCity() {
        return city;
    }

    public int getMaxTempCelsius() {
        return maxTempCelsius;
    }

    public int getMinTempCelsius() {
        return minTempCelsius;
    }
}
