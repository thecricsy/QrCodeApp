package model;

public class Category {
    String name;
    String id;
    String quantity;
    String expectedQuantity;
    String pendingQuantity;
    String inProgressQuantity;
    String des;

    public Category(String id, String name, String quantity, String expectedQuantity, String pendingQuantity, String des) {
        this.id = id;
        this.name = name;
        this.quantity= quantity;
        this.expectedQuantity= expectedQuantity;
        this.pendingQuantity = pendingQuantity;
        this.des = des;
        this.inProgressQuantity = String.valueOf((Integer.parseInt(expectedQuantity) - Integer.parseInt(quantity) - Integer.parseInt(pendingQuantity)));
    }
    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public Category() {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(String expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public String getPendingQuantity() {
        return pendingQuantity;
    }

    public void setPendingQuantity(String pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }

    public String getInProgressQuantity() {
        return inProgressQuantity;
    }

    public void setInProgressQuantity(String inProgressQuantity) {
        this.inProgressQuantity = inProgressQuantity;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



}

