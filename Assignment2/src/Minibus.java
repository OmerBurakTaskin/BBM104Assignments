import java.util.ArrayList;
import java.util.List;

public class Minibus extends Bus {
    double seatPrice;
    List<List<String>> seatList;
    List<Integer> bookedSeatList = new ArrayList<>();

    /**
     * Constructs a new Minibus object with the specified seat price and number of rows.
     * @param seatPrice The price per seat in the minibus.
     * @param rows The number of rows in the minibus.
     */
    public Minibus(double seatPrice, int rows) {
        this.seatPrice = seatPrice;
        this.rows = rows;
        seatList = new ArrayList<List<String>>(rows);
    }


    @Override
    void printBus() {
        String header = "Voyage " + voyage.ID;
        FileOutput.writeToFile(outputPath, header, true, true);
        String direction = voyage.fromWhere + "-" + voyage.toWhere;
        FileOutput.writeToFile(outputPath, direction, true, true);
        String row = "";
        for (int i = 0; i < rows * 2; i++) {
            boolean rowCondition = i % 2 == 0;
            String seatDisplay = bookedSeatList.contains(i) ? "X" : "*";
            row += rowCondition ? seatDisplay : " " + seatDisplay;
            if (!rowCondition) {
                FileOutput.writeToFile(outputPath, row, true, true);
                row = "";
            }
        }
        String msg = String.format("Revenue: %.2f", revenue);
        FileOutput.writeToFile(outputPath, msg, true, false);

    }

    @Override
    void fillSeat(List<Integer> seatNumbers) { // executes when "SELL_TICKET" is read from input file.
        double income = 0;
        List<Integer> indexedSeatNumbers=new ArrayList<>();
        for (int seatNumber : seatNumbers) {
            if (seatNumber>rows*2){
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
            bookedSeats += "-" + seatNumbers.get(i);
        }
        income = seatPrice * seatNumbers.size();
        revenue += income;

        String msg = String.format("Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.",
                bookedSeats, voyage.ID, voyage.fromWhere, voyage.toWhere, income);
        FileOutput.writeToFile(outputPath, msg, true, false);
    }

    @Override
    void cancelBus() {
        FileOutput.writeToFile(outputPath, String.format("Voyage %d was successfully cancelled!\nVoyage details can be found below:", voyage.ID), true, true);
        revenue -= bookedSeatList.size() * seatPrice;
        this.printBus();
        Voyage.getVoyageMap().remove(voyage.ID);
    }

    @Override
    void discardSeat(List<Integer> seatNumbers) {//Minibuses cant refund.
        ErrorOutputs.minibusNotRefundable();
    }
}
