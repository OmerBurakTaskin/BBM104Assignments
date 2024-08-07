import java.util.ArrayList;
import java.util.List;

public class PremiumBus extends Bus {
    double standartSeatPrice;
    int premiumFee;
    int refundCut;
    double premiumSeatPrice;

    List<List<String>> seatList;
    List<Integer> bookedSeatList = new ArrayList<>();

    public PremiumBus(double standartSeatPrice, int premiumFee, int refundCut, int rows) {
        this.rows = rows;
        this.standartSeatPrice = standartSeatPrice;
        this.premiumFee = premiumFee;
        this.refundCut = refundCut;
        seatList = new ArrayList<List<String>>(rows);
        premiumSeatPrice = standartSeatPrice + standartSeatPrice * premiumFee / 100;

    }

    @Override
    void printBus() {

        String header = "Voyage " + voyage.ID;
        FileOutput.writeToFile(outputPath, header, true, true);
        String direction = voyage.fromWhere + "-" + voyage.toWhere;
        FileOutput.writeToFile(outputPath, direction, true, true);
        String row = "";
        for (int i = 0; i < rows * 3; i++) {
            boolean endOfRow = i % 3 == 2;
            boolean endOfSemiRow = i % 3 == 0;
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
            if (seatNumber>rows*3){
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
        for (int seatNumber : indexedSeatNumbers) {
            boolean isPremium = seatNumber % 3 == 0;
            income += isPremium ? premiumSeatPrice : standartSeatPrice;
        }
        revenue += income;

        String msg = String.format("Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.",
                bookedSeats, voyage.ID, voyage.fromWhere, voyage.toWhere, income);
        FileOutput.writeToFile(outputPath, msg, true, false);


    }

    @Override
    void cancelBus() {
        FileOutput.writeToFile(outputPath, String.format("Voyage %d was successfully cancelled!\nVoyage details can be found below:", voyage.ID), true, true);
        for (int seatNumber : bookedSeatList) {
            boolean isPremium = seatNumber % 3 == 0;
            revenue -= isPremium ? premiumSeatPrice : standartSeatPrice;
        }
        this.printBus();
        Voyage.getVoyageMap().remove(voyage.ID);
    }

    @Override
    void discardSeat(List<Integer> seatNumbers) {
        List<Integer> indexedSeatNumbers=new ArrayList<>();
        for (int seatNumber : seatNumbers) {
            if (seatNumber>rows*3){
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
        for (int seatNumber : indexedSeatNumbers) {
            boolean isPremium = seatNumber % 3 == 0;
            refundedAmount += isPremium ? premiumSeatPrice * (100 - refundCut) / 100 : standartSeatPrice * (100 - refundCut) / 100;
        }
        revenue -= refundedAmount;
        bookedSeatList.removeAll(indexedSeatNumbers);

        String refundedSeats = seatNumbers.get(0).toString();
        for (int i = 1; i < seatNumbers.size(); i++) {
            refundedSeats += "-" + (seatNumbers.get(i));
        }
        String msg = String.format("Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.",
                refundedSeats, voyage.ID, voyage.fromWhere, voyage.toWhere, refundedAmount);
        FileOutput.writeToFile(outputPath,msg,true,false);
    }
}
