# WeatherAlertService
This is a small web app that let users subscribes to a service that notify them about the temperature in a city of their choice, it is a java application to test the spark-java framework for web applications

## Disclaimer
This app is for learning purposes only.

## Technologie

This app uses [spark-java](https://sparkjava.com/) framework to create the web application, it also uses java's executorservice and scheduledexecutorservice to create a service that will fetch all subscribed cities using [openweathermap](https://openweathermap.org/) public Api, this service will run every 10 minutes.


## Usage

* The web application is straightforward, you can find the different routes in the Main.java class.
* On launch, the application will generate 40 cities, you can add more in the generateDataSet method inside data.cities.CityAccess.java class.
* You have a default user that is subscribed to the city of Sidon, Lebanon, email: mohamad_email@someProvider.com, password: @MyInitial20, you can Add users from the webapp.
* We have three logs in resources/logs:
userAlerts.log that is a simulation for sending emails, errors.log for logging exceptions and errors, and debug.log for debugging and tracing the app flow.
* If you want to activate sending emails, configure the parameters in resources/EmailAuth.json, as long as the Email parameter is an empty string, the service will not attempt to send emails.
