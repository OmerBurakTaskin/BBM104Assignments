import java.util.List;

public class MachineOutputs {
    String outputPath;
    public MachineOutputs(String path) {
        outputPath=path;
    }

    void notEnoughMoney(int insertedMoney){
        String msg1="INFO: Insufficient money, try again with more money.";
        String msg2=String.format("RETURN: Returning your change: %d TL",insertedMoney);
        FileOutput.writeToFile(outputPath,msg1,true,true);
        FileOutput.writeToFile(outputPath,msg2,true,true);
    }

    void invalidNumber(int insertedMoney){
        String msg=String.format("RETURN: Returning your change: %d TL",insertedMoney);
        FileOutput.writeToFile(outputPath,"INFO: Number cannot be accepted. Please try again with another number.",true,true);
        FileOutput.writeToFile(outputPath,msg,true,true);
    }

    void successfulPurchase(String productName, int returnedMoney){
        String msg1=String.format("PURCHASE: You have bought one %s",productName);
        String msg2=String.format("RETURN: Returning your change: %d TL",returnedMoney);
        FileOutput.writeToFile(outputPath,msg1,true,true);
        FileOutput.writeToFile(outputPath,msg2,true,true);
    }

    void emptySlot(int insertedMoney){
        String msg1="INFO: This slot is empty, your money will be returned.";
        String msg2=String.format("RETURN: Returning your change: %d TL",insertedMoney);
        FileOutput.writeToFile(outputPath,msg1,true,true);
        FileOutput.writeToFile(outputPath,msg2,true,true);
    }

    void productNotFound(int insertedMoney){
        String msg1="INFO: Product not found, your money will be returned.";
        String msg2=String.format("RETURN: Returning your change: %d TL",insertedMoney);
        FileOutput.writeToFile(outputPath,msg1,true,true);
        FileOutput.writeToFile(outputPath,msg2,true,true);

    }


}
