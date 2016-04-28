package app.entity;

import java.util.Date;

public class Item {
    private int id;
    private String name;
    private Date date;
    private double price;
    private int user;
    private int family;

    public Item() {
    }

    public Item(String name, double price, int user, int family) {
        this.name = name;
        this.price = price;
        this.user = user;
        this.family = family;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Item && this.name.equals(((Item) o).getName()) &&
            this.price == ((Item) o).getPrice() && this.user == ((Item) o).getUser() &&
            this.family == ((Item) o).getFamily();
    }
}
