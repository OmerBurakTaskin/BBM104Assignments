import java.util.ArrayList;
import java.util.Locale;

public class MapAnalyzer {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        String[] file = FileInput.readFile(args[0], true, true);
        ShortestWay shortestWay = new ShortestWay(file);
        Output output= new Output(args[1]);
        output.shortestPathOutput(shortestWay.shortestWayToPoint, shortestWay.startPoint, shortestWay.endPoint);
        BarelyConnectedMap barelyConnectedMap= new BarelyConnectedMap(shortestWay.startPoint, shortestWay.wholeMap,shortestWay.roads);
        barelyConnectedMap.createBarelyConnectedMap();
        output.barelyConnectedMapOutput(new ArrayList<Road>(barelyConnectedMap.shortestWayToPoint.values()));
        output.fastestRouteBCM(barelyConnectedMap.shortestWayToPoint, barelyConnectedMap.startPoint, shortestWay.endPoint);
        output.analysisOutput();
    }
}