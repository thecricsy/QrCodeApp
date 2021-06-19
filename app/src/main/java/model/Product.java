package model;

public class Product {
    private String productId;
    private String name;
    private String price;
    private String description;
    private String picture;
    private String category;
    private String quantity;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory(){return category;}

    public void setCategory(String category){this.category = category;}

    public String getQuantity(){ return quantity;}

    public void setQuantity(String quantity){this.quantity= quantity;}
}
