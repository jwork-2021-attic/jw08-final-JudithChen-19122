package world;

import java.awt.Color;


public class Creature extends Thing  implements Runnable {

    Map map;
    boolean ifmagic;
    boolean isdead;
    int life;

    Creature(Color color, char glyph, World world,Map tmap, int type) {
        super(color, glyph, world,type);
        ifmagic=false;
        isdead=false;
        life=1;
        map=tmap;
        type=2;
    }


    public String saving_state(){
        return "";
    }

    public void set_inmagic(){
        ifmagic=false;
    }

    public void setmagic(){
        ;
    }
    
    public void set_glyph(char g){
        ;
    }
    

    public int getlife(){
        return life;
    }

    public boolean checkifmagic(){
        return ifmagic;
    }

    public boolean checkifdead(){
        if(life<1) isdead=true;
        else isdead=false;
        return isdead;
    }

    public void setdead(){
        isdead=true;
    }

    public void eatBeans(int type){
        return;
    }

    public boolean beenkill(){
        life--;
        return checkifdead();
    }

    public void moveAction(int direction){
        int x = getX();
        int y = getY();
        int nx = x;
        int ny = y;
        switch(direction){
            case 1: ny=y-1;  break;
            case 2: ny=y+1;  break;
            case 3: nx=x-1;  break;
            case 4: nx=x+1;  break;
            default:
            System.out.println("illegal movement.");
        }
        if( 0<nx && nx<30 && ny<30 && ny>0)
            map.moveAction(x,y,nx,ny);
    }

    public  boolean attack(Creature creature){
        return true;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        ;
    }
}

