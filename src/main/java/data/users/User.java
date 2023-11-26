package data.users;

public class User
{
    private final int id;
    private String name;
    private String email;
    private final String salt;
    private String password;

    public User(int id,String name, String email, String salt, String password) {
        this.id = id;
        this.name=name;
        this.email = email;
        this.salt = salt;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getSalt() {
        return salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "name: "+name+"\n"+"email: "+email;
    }
}
