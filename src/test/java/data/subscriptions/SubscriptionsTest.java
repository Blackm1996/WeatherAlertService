package data.subscriptions;


import data.cities.CityAccess;
import data.users.UsersAccess;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionsTest
{
    @Test
    public void getSubExists()
    {
        CityAccess.generateDataSet();
        UsersAccess.generateInitial();
        SubscriptionsAccess.clearSubs();
        Subscription subscription = new Subscription(UsersAccess.getUserById(1), CityAccess.getCityById(1),18,2);
        SubscriptionsAccess.addSub(subscription);
        assertEquals(subscription,SubscriptionsAccess.getUserSubscription(1));
    }
    @Test
    public void getSubNotExists()
    {
        CityAccess.generateDataSet();
        SubscriptionsAccess.clearSubs();
        UsersAccess.generateInitial();
        assertNull(SubscriptionsAccess.getUserSubscription(1));
    }
    @Test
    public void SubscribeSuccessful()
    {
        CityAccess.generateDataSet();
        SubscriptionsAccess.clearSubs();
        UsersAccess.generateInitial();
        assertEquals(SubscriptionsAccess.SUCCESSFUL, SubscriptionsAccess.subscribe(1,1,20,10));
    }
    @Test
    public void SubscribeWrongCityId()
    {
        CityAccess.generateDataSet();
        SubscriptionsAccess.clearSubs();
        UsersAccess.generateInitial();
        assertEquals(SubscriptionsAccess.ERROR, SubscriptionsAccess.subscribe(1,42,20,10));
    }
    @Test
    public void SubscribeWrongUserId()
    {
        CityAccess.generateDataSet();
        SubscriptionsAccess.clearSubs();
        UsersAccess.generateInitial();
        assertEquals(SubscriptionsAccess.ERROR, SubscriptionsAccess.subscribe(2,1,20,10));
    }
    @Test
    public void SubscribeAlreadySub()
    {
        CityAccess.generateDataSet();
        SubscriptionsAccess.clearSubs();
        UsersAccess.generateInitial();
        SubscriptionsAccess.subscribe(1,1,20,10);
        assertEquals(SubscriptionsAccess.ALREADYSUB, SubscriptionsAccess.subscribe(1,2,20,10));
    }
    @Test
    public void SubscribeMinTempBigger()
    {
        CityAccess.generateDataSet();
        SubscriptionsAccess.clearSubs();
        UsersAccess.generateInitial();
        assertEquals(SubscriptionsAccess.TEMPERROR, SubscriptionsAccess.subscribe(1,1,10,20));
    }
    @Test
    public void UnsubscribeSuccessful()
    {
        CityAccess.generateDataSet();
        SubscriptionsAccess.clearSubs();
        UsersAccess.generateInitial();
        SubscriptionsAccess.subscribe(1,1,20,10);
        assertTrue(SubscriptionsAccess.unsubscribe(1));
    }
    @Test
    public void UnsubscribeUnsuccessful()
    {
        CityAccess.generateDataSet();
        SubscriptionsAccess.clearSubs();
        UsersAccess.generateInitial();
        assertFalse(SubscriptionsAccess.unsubscribe(1));
    }
}