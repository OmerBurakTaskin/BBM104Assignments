public class CenterInfo {


    String pointName;
    String previousPointName;
    int distance;
    Road road;
    public CenterInfo(String pointName, String previousPointName, int distance, Road road) {
        this.pointName = pointName;
        this.previousPointName = previousPointName;
        this.distance = distance;
        this.road = road;
    }

    void changeInfos(String newPreviousPoint,Road newRoad,int distance){

    }

}
