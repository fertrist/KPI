package app.entity;

public class User {
    private int id;
    private String name;
    private String surname;
    private String families;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public User(String name, String surname, String families, String email) {
        this.name = name;
        this.surname = surname;
        this.families = families;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFamilies() {
        return families;
    }

    public void setFamilies(String families) {
        this.families = families;
    }

    public void addFamily(int... ids) {
        if (ids != null && ids.length > 0) {
            for (int id1 : ids) {
                this.families += "," + id1;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        User u;
        if (o instanceof User) {
            u = (User) o;
        } else {
            return false;
        }
        return this.name.equals(u.getName())
            && this.surname.equals(u.getSurname())
            && this.getFamilies().equals(u.getFamilies())
            && this.getEmail().equals(u.getEmail());
    }

    @Override
    public String toString() {
        return String.format("%4s[%s]:%10s %10s [%10s] %10s%n", this.id, this.hashCode(),
            this.name,
            this.surname,
            this.families, this.email);
    }
}
