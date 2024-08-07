import java.util.ArrayList;
import java.util.List;

public class StandartBus extends Bus {

    List<List<String>> seatList;
    int refundCut;
    double seatPrice;
    List<Integer> bookedSeatList = new ArrayList<>();

    public StandartBus(int rows, double seatPrice, int refundCut) {

        this.rows = rows;
        this.refundCut = refundCut;
        this.seatPrice = seatPrice;
        seatList = new ArrayList<List<String>>(rows);
    }

    @Override
    void printBus() {

        String header = "Voyage " + voyage.ID;
        FileOutput.writeToFile(outputPath, header, true, true);
        String direction = voyage.fromWhere + "-" + voyage.toWhere;
        FileOutput.writeToFile(outputPath, direction, true, true);
        String row = "";
        for (int i = 0; i < rows * 4; i++) {
            boolean endOfRow = i % 4 == 3;
            boolean endOfSemiRow = i % 4 == 1;
            String seatDisplay = bookedSeatList.contains(i) ? "X" : "*";
            if (endOfRow) {
                row += seatDisplay;
                FileOutput.writeToFile(outputPath, row, true, true);
                row = "";
            } else if (endOfSemiRow) {
                row += seatDisplay + " | ";
            } else {
                row += seatDisplay + " ";
            }
        }
        String msg = String.format("Revenue: %.2f", revenue);
        FileOutput.writeToFile(outputPath, msg, true, false);
    }

    @Override
    void fillSeat(List<Integer> seatNumbers) {
        double income = 0;
        List<Integer> indexedSeatNumbers=new ArrayList<>();
        for (int seatNumber : seatNumbers) {
            if (seatNumber>rows*4){
                ErrorOutputs.invalidSeatError();
                return;
            }
            indexedSeatNumbers.add(seatNumber-1);
        }
        for (int indexedSeatNumber : indexedSeatNumbers) {
            if (bookedSeatList.contains(indexedSeatNumber)) {
                ErrorOutputs.seatAlreadySoldError();
                return;
            }
        }
        bookedSeatList.addAll(indexedSeatNumbers);
        String bookedSeats = seatNumbers.get(0).toString();
        for (int i = 1; i < seatNumbers.size(); i++) {
            bookedSeats += "-" + (seatNumbers.get(i));
        }
        income = seatPrice * seatNumbers.size();
        revenue += income;

        String msg = String.format("Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.",
                bookedSeats, voyage.ID, voyage.fromWhere, voyage.toWhere, income);
        FileOutput.writeToFile(outputPath, msg, true, false);
    }

    @Override
    void cancelBus() {
        revenue -= bookedSeatList.size() * seatPrice;
        this.printBus();
        Voyage.getVoyageMap().remove(voyage.ID);
    }

    @Override
    void discardSeat(List<Integer> seatNumbers) {
        List<Integer> indexedSeatNumbers=new ArrayList<>();
        for (int seatNumber : seatNumbers) {
            if (seatNumber>rows*4){
                ErrorOutputs.invalidSeatError();
                return;
            }
            indexedSeatNumbers.add(seatNumber-1);
        }
        for (int indexedSeatNumber : indexedSeatNumbers) {
            if (!bookedSeatList.contains(indexedSeatNumber)) {
                ErrorOutputs.emptySeatRefundError();
                return;
            }
        }
        double refundedAmount = 0;
        for (int seatNumber : seatNumbers) {
            refundedAmount += seatPrice * (100 - refundCut) / 100;
        }
        revenue -= refundedAmount;
        bookedSeatList.removeAll(indexedSeatNumbers);

        String bookedSeats =seatNumbers.get(0).toString();
        for (int i = 1; i < seatNumbers.size(); i++) {
            bookedSeats += "-" + (seatNumbers.get(i));
        }
        String msg = String.format("Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.",
                bookedSeats, voyage.ID, voyage.fromWhere, voyage.toWhere, refundedAmount);
        FileOutput.writeToFile(outputPath,msg,true,false);
    }
}
