import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        GMM gmm= new GMM(args[0],args[2]);
        String[] lines = FileInput.readFile(args[0], true, true);
        List<Product> products = new ArrayList<Product>();
        for (String line : lines) {
            String[] parts = line.split("\t");
            String[] nutrientValues = parts[2].split(" ");
            String productName = parts[0];
            int price = Integer.parseInt(parts[1]);
            float protein = Float.parseFloat(nutrientValues[0]);
            float carb = Float.parseFloat(nutrientValues[1]);
            float fat = Float.parseFloat(nutrientValues[2]);
            Product product = new Product(productName, price, protein, carb, fat);
            products.add(product);
        }
        // Fill GMM with list of all products
        GMM.fillGMM(products);
        // Print GMM's current Products
        GMM.printGMM(args[2]);

        // Purchase processes
        String[] purchases = FileInput.readFile(args[1], true, true);
        for(String purchaseDetails:purchases){
            String[] parts = purchaseDetails.split("\t");
            String purchaseType=parts[2];
            String[] moneys = parts[1].split(" ");
            int totalMoney=0;
            for(String money:moneys){
                try{
                    int parsedMoney=Integer.parseInt(money);
                    totalMoney+=parsedMoney;
                }catch (Exception e){

                }
            }
            if (purchaseType.equals("NUMBER")){
                int slotNumber= Integer.parseInt(parts[3]);
                GMM.purchaseWithLocation(totalMoney,slotNumber,purchaseDetails);
            }else{
                int exptectedValue = Integer.parseInt(parts[3]);
                GMM.purchaseWithNutrient(totalMoney,purchaseType,exptectedValue,purchaseDetails);
            }
        }
        GMM.printGMM(args[2]);

    }
}
