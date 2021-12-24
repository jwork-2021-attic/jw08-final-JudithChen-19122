package world;

import java.awt.Color;

public class Monster extends Creature {

    public Monster(Color color, char glyph, World world,Map tmap) {
        super(color, glyph, world, tmap, 2);
        //TODO Auto-generated constructor stub
    }

    @Override
    public String saving_state(){
        String state="";
        state += getX()+" ";
        state += getY()+" ";
        state += ifmagic+" ";
        state += isdead+" ";
        state += life+"\n";
        return state;
    }

    public void set_monster(boolean ifm, boolean ifd, int lie){
        ifmagic=ifm;
        isdead=ifd;
        life=lie;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(!isdead){
            if(world.get_state()!=2 && world.get_state()!=11) break;
            int dir = ((int)(Math.random()*100)) % 4 + 1;
            //System.out.println("here");
            moveAction(dir);
        
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean attack(Creature creature){
        if(creature.checkifmagic()) return false ;
        else return true;
    }


    
}
