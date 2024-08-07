import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class Attributes {
    static HashMap<String, Image> imageHashMap= new HashMap<>();
    static HashMap<String, Mineral> mineralHashMap= new HashMap<>();
    static void setAttributes(){// reads attributes and puts in hashmaps
        String[] lines = FileInput.readFile("src/assets/atributes_of_valuables.txt", true, true);
        for (int i = 1; i < lines.length; i++) {
            String[] infos = lines[i].split("\t");
            String mineralType = infos[0].toLowerCase(Locale.ENGLISH);
            int worth = Integer.parseInt(infos[1]);
            int weight = Integer.parseInt(infos[2]);
            String path = String.format("assets/underground/valuable_%s.png", mineralType);
            imageHashMap.put(mineralType,new Image(path));
            mineralHashMap.put(mineralType,new Mineral(worth,weight,mineralType));
        }
        String[] customBlockNames= new String[]{"obstacle","soil","top_soil","lava"};
        String[] blockPaths= new String[]{"obstacle_01","soil_01","top_01","lava_03"};
        for (int i = 0; i < customBlockNames.length; i++) {
            String path = String.format("assets/underground/%s.png", blockPaths[i]);
            imageHashMap.put(customBlockNames[i],new Image(path));
        }
    }


    static Image selectRandomMineralImage(){ // returns image of mineral randomly which also has different possibility weights
        Random random= new Random();
        int randomInt= random.nextInt(1000);
        if (randomInt> 990) return imageHashMap.get("diamond");
        if (randomInt> 800) return imageHashMap.get("ruby");
        if (randomInt> 750) return imageHashMap.get("emerald");
        if (randomInt> 600) return imageHashMap.get("einsteinium");
        if (randomInt> 450) return imageHashMap.get("platinum");
        if (randomInt> 300) return imageHashMap.get("silverium");
        if (randomInt> 150) return imageHashMap.get("bronzium");
        return imageHashMap.get("ironium");
    }
}
