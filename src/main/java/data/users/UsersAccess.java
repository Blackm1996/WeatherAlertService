package data.users;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

import static logging.AppLogger.*;

public class UsersAccess
{
    private static final HashMap<Integer, User> users = new HashMap<>();
    public static final int EMPTYFIELD = -1;
    public static final int USERNOTEXIST = -2;
    public static final int PASSANDCONFIRMNOMATCH = -3;
    public static final int USERALREADYREGISTERED = -4;
    public static final int UNAUTHENTICATED = 0;
    public static int generateInitial()
    {
        users.clear();
        //default user mohamad
        return addUser("mohamad", "mohamad_email@someProvider.com","@MyInitial20","@MyInitial20");
    }
    //get the User if exists, return null if not
    public  static User getUserByEmail(String email)
    {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (entry.getValue().getEmail().equals(email)) {
                return entry.getValue(); // Return the user if email matches
            }
        }
        return null; // Return null if the email is not found
    }

    public static User getUserById(int id)
    {
        try {
            return users.get(id);
        }
        catch (Exception e)
        {
            logError("Error getting User: "+e);
        }
        return null;
    }
    //Add new User, and return its ID
    public static int addUser(String name, String email, String password, String confirmPass)
    {
        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty())
            return EMPTYFIELD;
        if(!password.equals(confirmPass))
            return PASSANDCONFIRMNOMATCH;

        User user =getUserByEmail(email);
        int id = user == null ? -1 : user.getId();
        if(id != -1)
            return USERALREADYREGISTERED;

        id = users.size()+1;
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(password,salt);
        users.put(id,new User(id, name, email, salt, hash));
        return id;
    }

    public static int authenticate(String email, String password)
    {
        if(email.isEmpty() || password.isEmpty())
            return EMPTYFIELD;  //Empty Field

        User user = getUserByEmail(email);
        if( user == null)
            return USERNOTEXIST;
        String hashed =BCrypt.hashpw(password, user.getSalt());

        return hashed.equals(user.getPassword()) ? user.getId() : UNAUTHENTICATED;
    }
}
