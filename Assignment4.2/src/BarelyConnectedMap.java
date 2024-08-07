import java.util.*;

public class BarelyConnectedMap {
    String startPoint;
    HashMap<String,List<Road>> wholeMap = new HashMap<>();
    HashMap<String,Road> shortestWayToPoint = new HashMap<>();
    List<CenterInfo> distanceSortedCentres = new ArrayList<>();
    List<Road> roads = new ArrayList<>();

    /**
     * Constructs a BarelyConnectedMap with the given starting point, whole map, and roads.
     *
     * @param startPoint The starting point for creating the barely connected map.
     * @param wholeMap The entire map containing all the points and their connected roads.
     * @param roads A list of all roads in the map.
     */
    public BarelyConnectedMap(String startPoint, HashMap<String, List<Road>> wholeMap,List<Road> roads) {
        this.startPoint = startPoint;
        this.wholeMap = wholeMap;
        this.roads=roads;
    }

    void createBarelyConnectedMap() {
        distanceSortedCentres.add(new CenterInfo(startPoint, null, 0, null));
        List<String> added=new ArrayList<>();
        while (!distanceSortedCentres.isEmpty()) {
            CenterInfo currentCenterInfo= distanceSortedCentres.get(0);
            distanceSortedCentres.remove(currentCenterInfo);
            String currentCityName= currentCenterInfo.pointName;

            if (shortestWayToPoint.containsKey(currentCityName)) {
                continue;
            }

            added.add(currentCityName);
            if (currentCenterInfo.road != null) {
                shortestWayToPoint.put(currentCityName, currentCenterInfo.road);
            }

            for (Road road : roads) {
                if (road.endPoint1.equals(currentCityName) && !added.contains(road.endPoint2)) {
                    distanceSortedCentres.add(new CenterInfo(road.endPoint2,currentCityName, road.length, road));
                } else if (road.endPoint2.equals(currentCityName) && !added.contains(road.endPoint1)) {
                    distanceSortedCentres.add(new CenterInfo(road.endPoint1,currentCityName, road.length, road));
                }
            }
            sortCenterInfoList();
        }
        System.out.println(shortestWayToPoint);

    }

    void sortCenterInfoList() {
        distanceSortedCentres.sort(Comparator.comparingInt((CenterInfo ci) -> ci.distance).thenComparingInt((ci) -> ci.road.id));
    }
}
