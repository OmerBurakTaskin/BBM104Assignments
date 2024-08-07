public class Slot {
    private Product product;
    private int quantity = 0;

    public Slot(Product product) {
        this.product = product;
        if (product != null) {
            quantity++;
        }
    }

    public Product getProduct() {
        return product;
    }

    /**
            * Adds a product to the slot.
            *
            * @param product The product to be added to the slot.
     */
    public void addToSlot(Product product) {
        if (this.product == null) {
            this.product = product;
            this.quantity++;
        }
        else if(product.productName.equals(this.product.productName)){
            this.quantity++;
        }
    }

    public int getQuantity() {
        return quantity;
    }


    void purchaseProduct(){
        if (quantity>0){
            quantity--;
        }

    }
}
