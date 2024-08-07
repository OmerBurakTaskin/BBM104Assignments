public class Product {
    String productName;
    int price;
    float protein;
    float carb;
    float fat;
    float calorie;

    public Product(String productName, int price, float protein, float carb, float fat) {
        this.productName = productName;
        this.price = price;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
        this.calorie=4*protein+4*carb+9*fat;
    }

    float getNutrientValue(String nutrientType){
        switch (nutrientType){
            case "PROTEIN":
                return this.protein;
            case "CARB":
                return this.carb;
            case "FAT":
                return this.fat;
            case "CALORIE":
                return this.calorie;
        }
        return 0.0f;
    }


}
