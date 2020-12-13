package lk.ijse.dep.web.model;

public class Customer {
    private int id;
    private String name;
    private String address;


    public Customer(int id, String name, String address) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
    }

    public Customer(){

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
