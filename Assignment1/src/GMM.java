import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.List;

public class GMM {
    //rows and columns in GMM will be portrayed as a 2D list in code.
    static List<List<Slot>> slots = new ArrayList<List<Slot>>();
    private static String InputFilePath;
    private static String OutputFilePath;


    // creating an empty GMM
    GMM(String inputPath, String outputFilePath) {
        InputFilePath = inputPath;
        OutputFilePath = outputFilePath;

        for (int i = 0; i < 6; i++) {
            List<Slot> row = new ArrayList<Slot>();
            for (int a = 0; a < 4; a++) {
                Slot slot = new Slot(null);
                row.add(slot);
            }
            slots.add(row);
        }
    }
    /**
     * Fills the Gym Meal Machine with products.
     *
     * @param products  List of products to be added to the machine.
     * @return          1 if successful, -1 if the machine is already full.
     */
    static int fillGMM(List<Product> products) {
        for (Product product : products) {
            int b=getSum();
            placeProduct(product);
            if (b==240) {
                FileOutput.writeToFile(OutputFilePath,"INFO: The machine is full!",true,true);
                return -1;
            }

        }
        return 1;
    }
    /**
     * Calculates the total quantity of products in the machine.
     *
     * @return  Total quantity of products.
     */
    static int getSum(){
        int r=0;
        for (List<Slot> row:slots){
            for (Slot slot:row){
                r+= slot.getQuantity();
            }
        }
        return r;
    }
    /**
     * Places a product into the Gym Meal Machine.
     *
     * @param product  The product to be placed.
     */
    static void placeProduct(Product product) {
        for (List<Slot> slotRow : slots) {
            for (Slot slot : slotRow) {
                if (slot.getProduct() == null) {
                    slot.addToSlot(product);
                    return;
                } else {
                    if (slot.getProduct().productName.equals(product.productName)) {
                        if (slot.getQuantity() < 10) {
                            slot.addToSlot(product);
                            return;
                        }
                    }

                }
            }
        }
        String msg= String.format("INFO: There is no available place to put %s",product.productName);
        FileOutput.writeToFile(OutputFilePath,msg,true,true);
    }

    /**
     * Processes a purchase request based on the slot number.
     *
     * @param insertedMoney  The amount of money inserted by the user.
     * @param slotNumber     The slot number corresponding to the desired product.
     * @param line           The input line from Purchase.
     */
    static void purchaseWithLocation(int insertedMoney, int slotNumber, String line) {
        MachineOutputs machineOutputs = new MachineOutputs(OutputFilePath);
        int row = slotNumber / 4;
        int column = slotNumber % 4;
        FileOutput.writeToFile(OutputFilePath, "INPUT: " + line, true, true);
        if (slotNumber > 23) {
            machineOutputs.invalidNumber(insertedMoney);
            return;
        }
        Slot slot = slots.get(row).get(column);
        Product product = slot.getProduct();
        if (product != null && slot.getQuantity() > 0) {
            if (product.price <= insertedMoney) {
                // PURCHASE SUCCESSFULL
                slot.purchaseProduct();
                int returnedMoney = insertedMoney - product.price;
                machineOutputs.successfulPurchase(product.productName, returnedMoney);
                returnedMoney = 0;

            } else {
                // not enough money
                machineOutputs.notEnoughMoney(insertedMoney);
            }
        } else {
            // empty slot
            machineOutputs.emptySlot(insertedMoney);
        }
    }

    /**
     * Processes a purchase request based on nutrient type.
     *
     * @param insertedMoney          The amount of money inserted by the user.
     * @param nutrientType           The type of nutrient desired by the user.
     * @param expectedNutrientValue  The expected value of the nutrient.
     * @param line                   The input line from Purchase.
     */
    static void purchaseWithNutrient(int insertedMoney, String nutrientType, int expectedNutrientValue, String line) {
        MachineOutputs machineOutputs = new MachineOutputs(OutputFilePath);
        FileOutput.writeToFile(OutputFilePath, "INPUT: " + line, true, true);
        boolean productFound = false;
        for (List<Slot> slotRow : slots) {
            for (Slot slot : slotRow) {
                Product product = slot.getProduct();
                if (product != null && slot.getQuantity() > 0) {
                    float productNutrientValue = product.getNutrientValue(nutrientType);
                    float nutrientGap = productNutrientValue > expectedNutrientValue ? productNutrientValue - expectedNutrientValue : expectedNutrientValue - productNutrientValue;

                    if (nutrientGap <= 5) {
                        productFound = true;
                        if (product.price <= insertedMoney) {
                            //purchase successful
                            slot.purchaseProduct();
                            int returnedMoney = insertedMoney - product.price;
                            machineOutputs.successfulPurchase(product.productName, returnedMoney);
                            returnedMoney = 0;
                            return;
                        }

                    }
                }
            }
        }
        if (productFound) {
            //not enough money
            machineOutputs.notEnoughMoney(insertedMoney);

        } else {
            //product not found
            machineOutputs.productNotFound(insertedMoney);
        }
    }
    /**
     * Prints the current status of the Gym Meal Machine.
     *
     * @param outputFilePath  The file path for output logging.
     */
    static void printGMM(String outputFilePath) {
        FileOutput.writeToFile(outputFilePath, "-----Gym Meal Machine-----", true, false);
        for (int i = 0; i < slots.size(); i++) {
            FileOutput.writeToFile(outputFilePath, "\n", true, false);
            for (Slot slot : slots.get(i)) {
                if (slot.getProduct() != null && slot.getQuantity()!=0) {
                    Product product = slot.getProduct();
                    int calorie = product.calorie - (int) product.calorie < 0.5f ? (int) product.calorie : (int) product.calorie + 1;
                    String msg = String.format("%s(%d, %d)___", slot.getProduct().productName, calorie, slot.getQuantity());
                    FileOutput.writeToFile(outputFilePath, msg, true, false);
                } else {
                    FileOutput.writeToFile(outputFilePath, "___(0, 0)___", true, false);

                }

            }
        }
        FileOutput.writeToFile(outputFilePath, "\n----------", true, true);

    }

}
