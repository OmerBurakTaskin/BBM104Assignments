public class Driller {
    private static int money=0;
    public static float fuel=10000f;
    public static int haul=0;
    static boolean isAlive=true;
    static void increaseMoney(int amount){
        money+=amount;
    }
    static int getMoney(){
        return money;
    }

}
