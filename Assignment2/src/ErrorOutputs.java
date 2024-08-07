public class ErrorOutputs {
    static String outputPath;

    private static void standartPrint(String msg){
        FileOutput.writeToFile(outputPath,msg,true,false);
    }
    static void lineCouldntParse(){
        standartPrint("ERROR: Line couldn't be parsed.");
    }

    static void cantBuySameTicket(){
        standartPrint("ERROR: Same ticket can't be bought more then once.");
    }
    static void invalidInputType(){
        standartPrint("ERROR: Wanted and given input types aren't match.");
    }

    static void insufficientInputLength(){
        standartPrint("ERROR: Info's arent sufficient.");
    }
    static void invalidBusType(String input){
        standartPrint(String.format("ERROR: Bus type %s is invalid.",input));
    }
    static void erroneousUsageError(String commandType){
        String msg=String.format("ERROR: Erroneous usage of \"%s\" command!" ,commandType);
        standartPrint(msg);
    }

    static void invalidCommandError(String commandType){
        String msg= String.format("ERROR: There is no command namely %s!",commandType);
        standartPrint(msg);
    }
    static void negativeSeatRowError(int number){
        String msg= String.format("ERROR: %d is not a positive integer, number of seat rows of a voyage must be a positive integer!",number);
        standartPrint(msg);
    }
    static void negativePriceError(String number){
        String msg= String.format("ERROR: %s is not a positive number, price must be a positive number!",number);
        standartPrint(msg);
    }
    static void negativeRefundError(int number){
        String msg=String.format("ERROR: %d is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!",number);
        standartPrint(msg);
    }

    static void negativePremiumFeeError(int number){
        String msg= String.format("ERROR: %d is not a non-negative integer, premium fee must be a non-negative integer!",number);
        standartPrint(msg);
    }

    static void voyageAlreadyExistsError(int id){
        String msg= String.format("ERROR: There is already a voyage with ID of %d!",id);
        standartPrint(msg);
    }

    static void IDMustBePositive(){
        standartPrint("ERROR: ID of a voyage must be a positive integer!");
    }
    static void invalidVoyageError(int id){
        String msg=String.format("ERROR: There is no voyage with ID of %d!",id);
        standartPrint(msg);
    }

    static void invalidSeatError(){
        String msg="ERROR: There is no such a seat!";
        standartPrint(msg);
    }
    static void minibusNotRefundable(){
        String msg= "ERROR: Minibus tickets are not refundable!";
        standartPrint(msg);
    }
    static void negativeSeatBuyError(int boughtSeatNumber){
        String msg=String.format("ERROR: %s is not a positive integer, seat number must be a positive integer!",boughtSeatNumber+"");
        standartPrint(msg);
    }

    static void inputFileDoesNotExist(String inputFile){
        String msg= String.format("ERROR: This program cannot read from the \"%s\", either this program does not have read permission to read that file or file does not exist. Program is going to terminate!",inputFile);
        System.out.println(msg);
    }
    static void outputFileDoesNotExist(String outputPath){
        String msg=String.format("ERROR: This program cannot write to the \"%s\", please check the permissions to write that directory. Program is going to terminate!",outputPath);
        System.out.println(msg);
    }
    static void insufficientProgramArgs(){
        String msg="ERROR: This program works exactly with two command line arguments, the first one is the path to the input file whereas the second one is the path to the output file. Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!";
        System.out.println(msg);
    }
    static void seatAlreadySoldError(){
        String msg= "ERROR: One or more seats already sold!";
        standartPrint(msg);
    }

    static void emptySeatRefundError(){
        String msg= "ERROR: One or more seats are already empty!";
        standartPrint(msg);
    }

    static void negativeVoyageIDError(int voyageID){
        String msg= String.format("ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!",voyageID);
        standartPrint(msg);
    }
    static void noVoyageAvailable(){
        standartPrint("No Voyages Available!");
    }


}
