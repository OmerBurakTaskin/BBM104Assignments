import java.util.*;

public class ShortestWay {
    String startPoint;
    String endPoint;
    HashMap<String,List<Road>> wholeMap = new HashMap<>();
    HashMap<String,Road> shortestWayToPoint = new HashMap<>();
    List<CenterInfo> distanceSortedCentres = new ArrayList<>();
    List<Road> roads = new ArrayList<>();
    static int totalLength=1;

    /**
     * Constructs a ShortestWay object from the given file data.
     *
     * @param file The input data representing the map and roads.
     */
    public ShortestWay(String[] file) {
        String[] firstLineInfo = file[0].split("\t");
        startPoint = firstLineInfo[0];
        endPoint = firstLineInfo[1];
        createCities(file);
        addRoadsToMap();
        findShortesWay();

    }

    void findShortesWay() {
        distanceSortedCentres.add(new CenterInfo(startPoint,null,0,null));
        while (!distanceSortedCentres.isEmpty()) {
            CenterInfo currentCenterInfo= distanceSortedCentres.get(0);
            distanceSortedCentres.remove(currentCenterInfo);
            String currentCityName= currentCenterInfo.pointName;

            if (shortestWayToPoint.containsKey(currentCityName))
                continue;

            if (currentCenterInfo.road!=null)
                shortestWayToPoint.put(currentCityName,currentCenterInfo.road);

            List<Road> linkedRoads= wholeMap.get(currentCityName);
            if (roads!=null){
                for (Road road:linkedRoads){
                    String target= road.getOtherEndPoint(currentCityName);
                    if (!shortestWayToPoint.containsKey(target)){
                        distanceSortedCentres.add(new CenterInfo(target,currentCityName,currentCenterInfo.distance+road.length,road));

                    }
                }
            }
            sortCenterInfoList();
        }
    }

    void sortCenterInfoList() {
        distanceSortedCentres.sort(Comparator.comparingInt((CenterInfo ci) -> ci.distance).thenComparingInt((ci) -> ci.road.id));
    }

    /**
     * Creates the cities and roads from the input file data.
     *
     * @param file The input data representing the map and roads.
     */
    private void createCities(String[] file) {
        for (int i = 1; i < file.length; i++) {
            String line = file[i];
            String[] infos = line.split("\t");
            String city1 = infos[0];
            String city2 = infos[1];
            int length = Integer.parseInt(infos[2]);
            int id = Integer.parseInt(infos[3]);
            totalLength+=length;
            roads.add(new Road(city1, city2, length, id));
        }
    }
    void addRoadsToMap(){
        for (Road road:roads){
            String endPoint1=road.endPoint1;
            String endPoint2=road.endPoint2;
            if (!wholeMap.containsKey(endPoint1))
                wholeMap.put(endPoint1,new ArrayList<>(Arrays.asList(road)));
            else
                wholeMap.get(endPoint1).add(road);

            if (!wholeMap.containsKey(endPoint2))
                wholeMap.put(endPoint2,new ArrayList<>(Arrays.asList(road)));
            else
                wholeMap.get(endPoint2).add(road);
        }
    }
}
