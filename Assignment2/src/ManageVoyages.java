
import java.util.*;

public class ManageVoyages {
    String inputPath;
    String outputPath;
    String[] lines;

    ManageVoyages(String[] args) {
        this.inputPath = args[0];
        this.outputPath = args[1];
        this.lines = FileInput.readFile(inputPath, true, true);
    }

    // read the input file
    void startOperations() {
        for (int i =0;i<lines.length;i++) {
            String line=lines[i];
            String[] infos = line.split("\t");
            String operation = infos[0];
            String msg= i==0?"COMMAND: " + line:"\nCOMMAND: " + line;
            standardOutput(msg);
            if (operation.equals("INIT_VOYAGE")) {
                initializeVoyage(infos);
            } else if (operation.equals("SELL_TICKET")) {
                sellTicket(infos);
            } else if (operation.equals("REFUND_TICKET")) {
                refundTicket(infos);
            } else if (operation.equals("PRINT_VOYAGE")) {
                printVoyage(infos);
            } else if (operation.equals("CANCEL_VOYAGE")) {
                cancelVoyage(infos);
            } else if (operation.equals("Z_REPORT")) {
                if (infos.length!=1){
                    ErrorOutputs.erroneousUsageError(infos[0]);
                }else{
                    printZReport();
                }
            }else{
                ErrorOutputs.invalidCommandError(infos[0]);
            }
        }
        //last z report
        if (lines.length==0||!lines[lines.length-1].equals("Z_REPORT")){
            standardOutput("");
            printZReport();
        }
    }

    private void initializeVoyage(String[] infos) {
        List<String> busTypes = new ArrayList<String>() {
        };
        if (infos.length < 7 || infos.length > 9) {
            ErrorOutputs.erroneousUsageError(infos[0]);
            return;
        };
        busTypes.add("Standard");
        busTypes.add("Premium");
        busTypes.add("Minibus");
        String busType = infos[1];
        if (!busTypes.contains(busType)){
            ErrorOutputs.erroneousUsageError(infos[0]);
            return;
        }
        int id = 0;
        try {//see if id is negative
            id = Integer.parseInt(infos[2]);
            if (id < 0) {
                ErrorOutputs.negativeVoyageIDError(id);
                return;
            }else if (id==0){
                ErrorOutputs.IDMustBePositive();
                return;
            }
        } catch (Exception e) {
            ErrorOutputs.invalidInputType();
            return;
        }
        if(Voyage.getVoyageMap().containsKey(id)){
            ErrorOutputs.voyageAlreadyExistsError(id);
            return;
        }
        String fromWhere = infos[3];
        String toWhere = infos[4];
        int rowCount = 0;
        try {// is rowCount positive
            rowCount = Integer.parseInt(infos[5]);
            if (rowCount < 0) {
                ErrorOutputs.negativeSeatRowError(rowCount);
                return;
            }
        } catch (Exception e) {
            ErrorOutputs.invalidInputType();
            return;
        }
        double price = 0;
        try {// is price positive
            price = Double.parseDouble(infos[6]);
            if (price < 0) {
                ErrorOutputs.negativePriceError(infos[6]);
                return;
            }
        } catch (Exception e) {
            ErrorOutputs.invalidInputType();
            return;
        }
        int refundCut = 0;
        int premiumFee = 0;
        if (infos.length > 7) {
            try {// is refundCut positive
                refundCut = Integer.parseInt(infos[7]);
                if (refundCut < 0) {
                    ErrorOutputs.negativeRefundError(refundCut);
                    return;
                }
            } catch (Exception e) {
                ErrorOutputs.invalidInputType();
                return;
            }
        }
        if (infos.length > 8) {
            try {// is premiumFee positive
                premiumFee = Integer.parseInt(infos[8]);
                if (premiumFee < 0) {
                    ErrorOutputs.negativePremiumFeeError(premiumFee);
                    return;
                }
            } catch (Exception e) {
                ErrorOutputs.invalidInputType();
                return;
            }
        }
        Voyage voyage;
        Bus bus;
        if (infos.length == 7) {
            bus = new Minibus(price, rowCount);
            voyage = new Voyage(bus, id, fromWhere, toWhere, price);
            bus.voyage = voyage;
            Voyage.putVoyageMap(id, voyage);
            String msg = String.format("Voyage %d was initialized as a minibus (2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that minibus tickets are not refundable.",
                    id, fromWhere, toWhere, price, rowCount * 2);
            outputWithoutNewLine(msg);
        } else if (infos.length == 8) {
            bus = new StandartBus(rowCount, price, refundCut);
            voyage = new Voyage(bus, id, fromWhere, toWhere, price);
            bus.voyage = voyage;
            Voyage.putVoyageMap(id, voyage);
            String msg = String.format("Voyage %d was initialized as a standard (2+2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that refunds will be %d%% less than the paid amount.",
                    id, fromWhere, toWhere, price, rowCount * 4, refundCut);
            outputWithoutNewLine(msg);

        } else {
            bus = new PremiumBus(price, premiumFee, refundCut, rowCount);
            voyage = new Voyage(bus, id, fromWhere, toWhere, price);
            bus.voyage = voyage;
            Voyage.putVoyageMap(id, voyage);
            String msg = String.format("Voyage %d was initialized as a premium (1+2) voyage from %s to %s with %.2f TL priced %d regular seats and %.2f TL priced %d premium seats. Note that refunds will be %d%% less than the paid amount.",
                    id, fromWhere, toWhere, price, rowCount * 2, ((PremiumBus) bus).premiumSeatPrice, rowCount, refundCut);
            outputWithoutNewLine(msg);
        }


    }

    private void sellTicket(String[] infos) {
        if (infos.length!=3){
            ErrorOutputs.erroneousUsageError(infos[0]);
            return;
        }
        int id = 0;
        try {//see if id is negative
            id = Integer.parseInt(infos[1]);
            if (id < 0) {
                ErrorOutputs.negativeVoyageIDError(id);
                return;
            }else if (id==0){
                ErrorOutputs.IDMustBePositive();
                return;
            }
        } catch (Exception e) {
            ErrorOutputs.invalidInputType();
            return;
        }

        if (!Voyage.getVoyageMap().containsKey(id)) {
            ErrorOutputs.invalidVoyageError(id);
            return;
        }
        String[] inputTicketNumbers = infos[2].split("_");
        ArrayList<Integer> ticketNumbers = new ArrayList<>();
        for (String ticketNumber : inputTicketNumbers) {
            int number = 0;
            try {// see if number is negative
                number = Integer.parseInt(ticketNumber);
                if (number < 1) {
                    ErrorOutputs.negativeSeatBuyError(number);
                    return;
                }
            } catch (Exception e) {
                ErrorOutputs.invalidInputType();
                return;
            }
            ticketNumbers.add(number);

        }
        // check if list has same numbers
        HashSet<Integer> ticketSet = new HashSet<>(ticketNumbers);
        if (ticketSet.size() != ticketNumbers.size()) {
            ErrorOutputs.cantBuySameTicket();
            return;
        }
        Voyage voyage = Voyage.getVoyageMap().get(id);
        voyage.bus.fillSeat(ticketNumbers);

    }

    private void refundTicket(String[] infos) {
        if (infos.length!=3){
            ErrorOutputs.erroneousUsageError(infos[0]);
            return;
        }
        int id = 0;
        try {//see if id is negative
            id = Integer.parseInt(infos[1]);
            if (id < 0) {
                ErrorOutputs.negativeVoyageIDError(id);
                return;
            }else if (id==0){
                ErrorOutputs.IDMustBePositive();
                return;
            }
        } catch (Exception e) {
            ErrorOutputs.invalidInputType();
            return;
        }
        if (!Voyage.getVoyageMap().containsKey(id)) {
            ErrorOutputs.invalidVoyageError(id);
            return;
        }


        String[] inputSeatNumbers = infos[2].split("_");
        ArrayList<Integer> seatNumbers = new ArrayList<>();
        for (String seatNumber : inputSeatNumbers) {
            int number = 0;
            try {// see if number is negative
                number = Integer.parseInt(seatNumber);
                if (number < 1) {
                    ErrorOutputs.negativeSeatBuyError(number);
                    return;
                }
            } catch (Exception e) {
                ErrorOutputs.invalidInputType();
                return;
            }

            seatNumbers.add(number);
        }
        // check if list has same numbers
        HashSet<Integer> seatSet = new HashSet<>(seatNumbers);
        if (seatSet.size() != seatNumbers.size()) {
            ErrorOutputs.cantBuySameTicket();
            return;
        }
        Voyage voyage = Voyage.getVoyageMap().get(id);
        voyage.bus.discardSeat(seatNumbers);


    }

    private void printVoyage(String[] infos) {
        if (infos.length!=2){
            ErrorOutputs.erroneousUsageError(infos[0]);
            return;
        }

        int id = 0;
        try {//see if id is negative
            id = Integer.parseInt(infos[1]);
            if (id < 0) {
                ErrorOutputs.negativeVoyageIDError(id);
                return;
            }else if (id==0){
                ErrorOutputs.IDMustBePositive();
                return;
            }
        } catch (Exception e) {
            ErrorOutputs.invalidInputType();
            return;
        }
        if (!Voyage.getVoyageMap().containsKey(id)) {
            ErrorOutputs.invalidVoyageError(id);
            return;
        }
        Voyage voyage = Voyage.getVoyageMap().get(id);
        voyage.bus.printBus();

    }

    private void cancelVoyage(String[] infos) {
        if (infos.length!=2){
            ErrorOutputs.erroneousUsageError(infos[0]);
            return;
        }
        int id = 0;
        try {//see if id is negative
            id = Integer.parseInt(infos[1]);
            if (id < 0) {
                ErrorOutputs.negativeVoyageIDError(id);
                return;
            }else if (id==0){
                ErrorOutputs.IDMustBePositive();
                return;
            }
        } catch (Exception e) {
            ErrorOutputs.invalidInputType();
            return;
        }
        if (!Voyage.getVoyageMap().containsKey(id)) {
            ErrorOutputs.invalidVoyageError(id);
            return;
        }
        Voyage voyage = Voyage.getVoyageMap().get(id);
        voyage.bus.cancelBus();

    }

    private void printZReport() {

        outputWithoutNewLine("Z Report:");
        if (Voyage.getVoyageMap().isEmpty()){
            standardOutput("\n----------------");
            ErrorOutputs.noVoyageAvailable();
            outputWithoutNewLine("\n----------------");
            return;
        }
        List<Voyage> voyages = new ArrayList<Voyage>(Voyage.getVoyageMap().values());
        Comparator<Voyage> idComparator = Comparator.comparingInt(Voyage::getID);
        voyages.sort(idComparator);
        for (Voyage voyage : voyages) {
            standardOutput("\n----------------");
            voyage.bus.printBus();
        }
        FileOutput.writeToFile(outputPath, "\n----------------", true, false);
    }

    private void standardOutput(String msg) {
        FileOutput.writeToFile(outputPath, msg, true, true);
    }
    private void outputWithoutNewLine(String msg){
        FileOutput.writeToFile(outputPath, msg, true, false);
    }

}
