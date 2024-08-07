import java.util.List;

public abstract class Bus {
    int rows;
    Voyage voyage;
    double revenue;
    static String outputPath="";
    abstract void printBus();    // writes bus seat order as how it wanted to be portrayed in instructions.To execute easy for every bus subclasses, it is abstract.

    abstract void fillSeat(List<Integer> seats);//it will be executed when "SELL_TICKET" is read from input file
    abstract void cancelBus();
    void discardSeat(List<Integer> seatNumbers){} // because minibuses are not refundable, this function is not abstract and will be overriden according to instructions


}
