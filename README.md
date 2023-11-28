# WeatherAlertService
This is a small web app where people can sign up for updates on the temperature in a city they pick, it is a java application to test the spark java framework for web applications.

## Disclaimer
This app is for learning purposes only.

## Technologie

This app uses [spark-java](https://sparkjava.com/) framework to create the web application, it also uses java's Executorservice and ScheduledExecutorService to create a service that will fetch all subscribed cities's weather data using [openweathermap](https://openweathermap.org/) public API, this service will run every 10 minutes.


## Usage

- The web application is straightforward, you can find the defined routes in the Main.java class.
- On launch, the application will generate 40 cities, You can add more by editing the generateDataSet method in the CityAccess.java class.
- There's a default user subscribed to Sidon, Lebanon. You can log in with the credentials: email: mohamad_email@someProvider.com, password: @MyInitial20. You can also add more users through the web app.
- Three types of logs are stored in resources/logs:
  - userAlerts.log for simulating sending emails.
  - errors.log for logging exceptions and errors.
  -  debug.log for debugging and tracing the app flow.
- If you want to activate sending emails, configure the parameters in resources/EmailAuth.json, as long as the Email parameter is an empty string, the service will not attempt to send emails.
- To subscribe to a city or view subscriptions, you need to log in to any account.
