package data.users;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {

    @Test
    public void getUserByEmailExists()
    {
        UsersAccess.generateInitial();
        User real = UsersAccess.getUserById(1);
        assertEquals(real, UsersAccess.getUserByEmail("mohamad_email@someProvider.com"));
    }
    @Test
    public void getUserByEmailNotExists()
    {
        UsersAccess.generateInitial();
        assertNull(UsersAccess.getUserByEmail("ohamad_email@someProvider.com"));
    }

    @Test
    public void getUserByIdExists()
    {
        UsersAccess.generateInitial();
        User real = UsersAccess.getUserByEmail("mohamad_email@someProvider.com");
        assertEquals(real, UsersAccess.getUserById(1));
    }
    @Test
    public void getUserByIdNotExists()
    {
        UsersAccess.generateInitial();
        assertNull(UsersAccess.getUserById(2));
    }
    @Test
    public void addUserSuccefful()
    {
        int id = UsersAccess.addUser("mohamad", "mohamad_email@someProvider.com","@MyInitial20","@MyInitial20");
        assertEquals(1, id);
    }

    @Test
    public void addUserEmptyField()
    {
        int id = UsersAccess.addUser("", "mohamad_email@someProvider.com","@MyInitial20","@MyInitial20");
        assertEquals(UsersAccess.EMPTYFIELD, id);
    }
    @Test
    public void addUserPassNotMatchConfirm()
    {
        int id = UsersAccess.addUser("mohamad", "mohamad_email@someProvider.com","@MyInitial20","@MyInitissal20");
        assertEquals(UsersAccess.PASSANDCONFIRMNOMATCH, id);
    }
    @Test
    public void addUserAlreadyExists()
    {
        UsersAccess.generateInitial();
        int id = UsersAccess.addUser("sdsd", "mohamad_email@someProvider.com","@MyInitial2023","@MyInitial2023");
        assertEquals(UsersAccess.USERALREADYREGISTERED, id);
    }

    @Test
    public void AuthenticateUserSucceful()
    {
        UsersAccess.generateInitial();
        int id = UsersAccess.authenticate("mohamad_email@someProvider.com","@MyInitial20");
        assertEquals(1,id);
    }
    @Test
    public void AuthenticateUserEmptyField()
    {
        UsersAccess.generateInitial();
        int id = UsersAccess.authenticate("","@MyInitial20");
        assertEquals(UsersAccess.EMPTYFIELD,id);
    }
    @Test
    public void AuthenticateUserNotExists()
    {
        UsersAccess.generateInitial();
        int id = UsersAccess.authenticate("amohamad_email@someProvider.com","@MyInitial20");
        assertEquals(UsersAccess.USERNOTEXIST,id);
    }

    @Test
    public void AuthenticateUserWrongPass()
    {
        UsersAccess.generateInitial();
        int id = UsersAccess.authenticate("mohamad_email@someProvider.com","@MyInitial2023");
        assertEquals(UsersAccess.UNAUTHENTICATED,id);
    }

}