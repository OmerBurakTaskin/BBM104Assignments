import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Output {
    String outputPath;
    int shortestPathLength=0;
    int shortestPathLengthBCM=0;

    int totalLengthBCM;

    public Output(String outputPath) {
        this.outputPath = outputPath;
    }
    public void shortestPathOutput(HashMap<String,Road> shortestPathMap,String startPoint,String endpoint){
        int totalKM=0;
        List<Road> paces= new ArrayList<>();
        String current=endpoint;
        while (!current.equals(startPoint)){
            Road road=shortestPathMap.get(current);
            totalKM+= road.length;
            paces.add(0,road);
            current=road.getOtherEndPoint(current);
        }
        standartOutput(String.format("Fastest Route from %s to %s (%d KM):",startPoint,endpoint,totalKM));
        for (Road road:paces){
            String msg= String.format("%s\t%s\t%d\t%d",road.endPoint1,road.endPoint2,road.length,road.id);
           standartOutput(msg);
        }
        shortestPathLength=totalKM;
    }

    public void barelyConnectedMapOutput(List<Road> mapRoads){
        standartOutput("Roads of Barely Connected Map is:");
        mapRoads.sort(Comparator.comparingInt((Road r)->r.length).thenComparingInt(r->r.id));
        for (Road road:mapRoads){
            String msg= String.format("%s\t%s\t%d\t%d",road.endPoint1,road.endPoint2,road.length,road.id);
            standartOutput(msg);
        }
    }

    public void fastestRouteBCM(HashMap<String,Road> shortestPathMap,String startPoint,String endPoint){
        int totalKM=0;
        List<Road> paces= new ArrayList<>();
        String current=endPoint;
        while (!current.equals(startPoint)){
            Road road=shortestPathMap.get(current);
            totalKM+= road.length;
            paces.add(0,road);
            current=road.getOtherEndPoint(current);
        }
        standartOutput(String.format("Fastest Route from %s to %s on Barely Connected Map (%d KM):",startPoint,endPoint,totalKM));
        for (Road road:paces){
            String msg= String.format("%s\t%s\t%d\t%d",road.endPoint1,road.endPoint2,road.length,road.id);
            standartOutput(msg);
        }
        for (Road road:shortestPathMap.values()){
            totalLengthBCM+=road.length;
        }
        shortestPathLengthBCM= totalKM;

    }
    void analysisOutput(){
        standartOutput("Analysis:");
        float ratio= (float) totalLengthBCM /(float)ShortestWay.totalLength;
        standartOutput(String.format("Ratio of Construction Material Usage Between Barely Connected and Original Map: %.2f",(float) totalLengthBCM /(float)ShortestWay.totalLength));
        FileOutput.writeToFile(outputPath,String.format("Ratio of Fastest Route Between Barely Connected and Original Map: %.2f",(float) shortestPathLengthBCM /(float)shortestPathLength),true,false);
    }
    private void standartOutput(String msg){
        FileOutput.writeToFile(outputPath,msg,true,true);
    }
}
