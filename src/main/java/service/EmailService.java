package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static logging.AppLogger.*;
public class EmailService
{
    private final ExecutorService emailExecutor;

    private final String email,password,host,port,auth;
    public EmailService()
    {
        emailExecutor= Executors.newCachedThreadPool();
        JsonReader reader=null;
        try {
            reader = new JsonReader(new FileReader("src/main/resources/EmailAuth.json"));
        }
        catch (IOException e)
        {
            logError("Error getting email service settings: "+e);
        }
        if(reader != null)
        {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            email=obj.get("Email").getAsString();
            password=obj.get("Password").getAsString();
            host=obj.get("Host").getAsString();
            port=obj.get("Port").getAsString();
            auth=obj.get("Auth").getAsString();
        }
        else
        {
            email="";
            password="";
            host="";
            port="";
            auth="";
        }
    }

    public ExecutorService getEmailExecutor()
    {
        return emailExecutor;
    }
    public void senEmail(String to, String subject, String body)
    {
        emailExecutor.submit(()->{

            if(!email.isEmpty())
            {
                Properties prop = new Properties();
                prop.put("mail.smtp.host", host);
                prop.put("mail.smtp.port", port);
                prop.put("mail.smtp.auth", auth);
                prop.put("mail.smtp.starttls.enable", "true"); //TLS

                Session session = Session.getInstance(prop,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(email, password);
                            }
                        });

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(email));
                    message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(to)
                    );
                    message.setSubject(subject);
                    message.setText(body);

                    Transport.send(message);

                    logDebug("Email was sent successfully to "+to);

                } catch (MessagingException e) {
                    logError("Exception sending email: "+e);
                }
            }
        });
    }
}
