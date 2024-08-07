import java.io.File;

public class BookingSystem {
    public static void main(String[] args) {
        if (args.length!=2){
            ErrorOutputs.insufficientProgramArgs();
            return;
        }
        File inputFile =new File(args[0]);
        if (!inputFile.exists()||!inputFile.canRead()||!args[0].equals("input.txt")){
            ErrorOutputs.inputFileDoesNotExist(args[0]);
            return;
        }
        File outputFile= new File(args[1]);
        if (outputFile.exists()&&!outputFile.canWrite()){
            ErrorOutputs.outputFileDoesNotExist(args[0]);
            return;
        }
        ErrorOutputs.outputPath=args[1];
        Bus.outputPath=args[1];
        ManageVoyages manageVoyages= new ManageVoyages(args);
        manageVoyages.startOperations();

    }
}