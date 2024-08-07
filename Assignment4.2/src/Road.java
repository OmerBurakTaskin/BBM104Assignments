public class Road {
    String endPoint1;
    String endPoint2;
    int length;
    int id;

    public Road(String endPoint1, String endPoint2, int length, int id) {
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;
        this.length = length;
        this.id = id;
    }

    @Override
    public String toString() {
        return endPoint1+" "+endPoint2;
    }

    String getOtherEndPoint(String endPoint){
        return endPoint.equals(endPoint1)? endPoint2:endPoint1;
    }
}
