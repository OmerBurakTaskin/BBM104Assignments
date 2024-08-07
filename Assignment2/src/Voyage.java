import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Voyage {
    private static Map<Integer,Voyage> voyageMap= new HashMap<Integer,Voyage>();// we will store our voyages in a static HashMap which our voyages are bounded to their id
    Bus bus;
    int ID;


    String fromWhere;
    String toWhere;
    double price;

    /**
     * Constructs a new Voyage object with the specified parameters.
     * @param bus The bus assigned to the voyage.
     * @param ID The ID of the voyage.
     * @param fromWhere The departure point of the voyage.
     * @param toWhere The destination point of the voyage.
     * @param price The price of the voyage.
     */
    public Voyage(Bus bus, int ID, String fromWhere, String toWhere, double price) {
        this.bus = bus;
        this.ID = ID;
        this.fromWhere = fromWhere;
        this.toWhere = toWhere;
        this.price = price;
    }
    public int getID() {
        return ID;
    }

    public static Map<Integer, Voyage> getVoyageMap() {
        return voyageMap;
    }

    public static void putVoyageMap(int id,Voyage voyage) {
        if (voyageMap.containsKey(id)){
            ErrorOutputs.voyageAlreadyExistsError(id);
        }else{
            voyageMap.put(id,voyage);
        }
    }
}
