package hello.itemservice.domain.item;

public class Item {

    public int getPrice;
    private Long id;
    private String itemName;
    private Integer price;
    private int quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public void setItemName(String itemName) {
    }

    public void setPrice(int price) {
    }

    public void setQuantity(Integer quantity) {
    }

    public Object getId() {
    }

    public String getItemName() {
    }

    public Integer getQuantity() {
    }
}