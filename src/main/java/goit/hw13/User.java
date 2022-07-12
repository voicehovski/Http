package goit.hw13;

public class User {
    private long id;
    private String name;
    private String username;
    private String email;
    private Object address;
    private String phone;
    private String website;
    private Object company;

    public static User createRandomUser ( long id ) {
        User user = new User (  );
        user .id = id;
        user .name = "Comrade " + (int)(Math .random (  ) * 100);
        user .username = user .name .replace ( " ", "_" );
        user .email = user .username + "@mail.com";
        user .phone = "123 456 789";
        user .website = "www." + user .username + ".com";

        return user;
    }
    public static User createRandomUser (  ) {
        return createRandomUser ( 0 );
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username=" + username +
                ", id=" + id +
                '}';
    }
}
