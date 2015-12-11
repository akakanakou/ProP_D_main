package jp.project_p.d.prop_d;

/**
 * Created by c01131653a on 2015/12/11.
 */
public class CastleMarker {
    private double hp;
    private final int FLAG = 1;
    private final int BLUE_TEAM = 2;
    private final int RED_TEAM = 3;
    private final int YELLOW_TEAM = 4;
    private int castle_status = FLAG;

    public CastleMarker(){
        hp = 100;
    }

    public double getHp() {
        return hp;
    }

    public void damage(double dam){
        hp -= dam;
        if(hp == 0){
            castle_status = FLAG;
        }
    }

    public void set_status(String color){
        if("BLUE".)
    }






}
