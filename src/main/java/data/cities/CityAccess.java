package data.cities;

import java.util.HashMap;

public class CityAccess
{
    private static final HashMap<Integer, City> cities = new HashMap<>();
    public static void generateDataSet()
    {
        cities.put(1, new City(1, "Abu Dhabi, UAE", 24.4539, 54.3773));
        cities.put(2, new City(2, "Al Ain, UAE", 24.1302, 55.8027));
        cities.put(3, new City(3, "Beirut, Lebanon", 33.8886, 35.4955));
        cities.put(4, new City(4, "Berlin, Germany", 52.5200, 13.4050));
        cities.put(5, new City(5, "Birmingham, UK", 52.4862, -1.8904));
        cities.put(6, new City(6, "Brisbane, Australia", -27.4698, 153.0251));
        cities.put(7, new City(7, "Calgary, Canada", 51.0486, -114.0708));
        cities.put(8, new City(8, "Chicago, USA", 41.8781, -87.6298));
        cities.put(9, new City(9, "Dubai, UAE", 25.2769, 55.2962));
        cities.put(10, new City(10, "Edinburgh, UK", 55.9533, -3.1883));
        cities.put(11, new City(11, "Frankfurt, Germany", 50.1109, 8.6821));
        cities.put(12, new City(12, "Hamburg, Germany", 53.5511, 9.9937));
        cities.put(13, new City(13, "Houston, USA", 29.7604, -95.3698));
        cities.put(14, new City(14, "Jeddah, Saudi Arabia", 21.4858, 39.1925));
        cities.put(15, new City(15, "Kyoto, Japan", 35.0116, 135.7681));
        cities.put(16, new City(16, "London, UK", 51.5099, -0.1180));
        cities.put(17, new City(17, "Los Angeles, USA", 34.0522, -118.2437));
        cities.put(18, new City(18, "Lyon, France", 45.7640, 4.8357));
        cities.put(19, new City(19, "Manchester, UK", 53.4830, -2.2446));
        cities.put(20, new City(20, "Mecca, Saudi Arabia", 21.3891, 39.8579));
        cities.put(21, new City(21, "Melbourne, Australia", -37.8136, 144.9631));
        cities.put(22, new City(22, "Medina, Saudi Arabia", 24.5247, 39.5692));
        cities.put(23, new City(23, "Munich, Germany", 48.1351, 11.5820));
        cities.put(24, new City(24, "Nagoya, Japan", 35.1815, 136.9066));
        cities.put(25, new City(25, "New York, USA", 40.7128, -74.0060));
        cities.put(26, new City(26, "Osaka, Japan", 34.6937, 135.5023));
        cities.put(27, new City(27, "Paris, France", 48.8566, 2.3522));
        cities.put(28, new City(28, "Perth, Australia", -31.9505, 115.8605));
        cities.put(29, new City(29, "Riyadh, Saudi Arabia", 24.7136, 46.6753));
        cities.put(30, new City(30, "San Francisco, USA", 37.7749, -122.4194));
        cities.put(31, new City(31, "Sharjah, UAE", 25.3463, 55.4209));
        cities.put(32, new City(32, "Sidon, Lebanon", 33.5632, 35.3688));
        cities.put(33, new City(33, "Sydney, Australia", -33.8688, 151.2093));
        cities.put(34, new City(34, "Tokyo, Japan", 35.6895, 139.6917));
        cities.put(35, new City(35, "Toronto, Canada", 43.6532, -79.3832));
        cities.put(36, new City(36, "Tripoli, Lebanon", 34.4367, 35.8497));
        cities.put(37, new City(37, "Toulouse, France", 43.6047, 1.4442));
        cities.put(38, new City(38, "Vancouver, Canada", 49.2827, -123.1207));
        cities.put(39, new City(39, "Yokohama, Japan", 35.4437, 139.6380));
        cities.put(40, new City(40, "Sydney, Australia", -33.8688, 151.2093));
    }

    public static City getCityById(int id)
    {
        return cities.get(id);
    }

    public static HashMap<Integer, City> getCities()
    {
        return cities;
    }
}
