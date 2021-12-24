package world;

import java.awt.Color;

import javax.lang.model.util.ElementScanner6;

public class Calabashbros extends Creature{

     // everyone owns 3 life at first
    static int score =0; //eat beans to get score
    boolean ifmagic; //check if it is in magic time, when using magic it can kill the monster
     
    final int magictime = 10; //the magic lasts
    int magic_count;

    int goal;

    public Calabashbros(Color color, char glyph, World world,Map tmap) {
        super(color, glyph, world, tmap, 3);
        //TODO Auto-generated constructor stub
        life = 3;
        magic_count = 10;
        ifmagic = false;
        type=3;
    }

    public Calabashbros(Color color, char glyph, World world,Map tmap, int li) {
        super(color, glyph, world, tmap, 3);
        //TODO Auto-generated constructor stub
        life = li;
        magic_count = 10;
        ifmagic = false;
        type=3;
    }

    @Override
    public String saving_state(){
        String state="";
        state += getX()+" ";
        state += getY()+" ";
        state += ifmagic+" ";
        state += isdead+" ";
        state += life+" ";
        state += score+" ";
        state += magic_count+" ";
        state += goal + "\n";
        return state;
    }

    public void set_calabashbros(boolean ifm, boolean ifd, int lie, int sco, int magic_c, int go){
        ifmagic=ifm;
        isdead=ifd;
        life=lie;
        score = sco;
        magic_count = magic_c;
        goal = go;
    }

    public void setgoal(int g){
        goal=g;
    }

    @Override
    public void setmagic(){
        glyph = 15;
        magic_count = 0;
        ifmagic = true;
    }

    @Override
    public void set_glyph(char g){
        glyph = g;
    }
    
    public void magic_counter(){
        if(magic_count == magictime-1) {
            set_inmagic();
            
        }

        else if(magic_count<magictime)
            magic_count++;

        if(magic_count<magictime)
            ifmagic=true;
        else
            ifmagic=false;  
            
        if(ifmagic){
            glyph = 15;
        }
    }

    @Override
    public void eatBeans(int type){
        if(type==4)
            score++;
        else if(type==5)
            setmagic();
        else if(type==6)
            life++;
    }

    public boolean checkifmagic(){
        return ifmagic;
    }
    //for replay
    @Override
    public void set_inmagic(){
        magic_count = magictime;
        ifmagic=false;
        glyph=2;
    }

    @Override
    public boolean attack(Creature creature){
        if(ifmagic) return true;
        else return false;
    }

    public boolean checkifwin(){
        return score >= goal;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(!isdead){
            if(world.get_state()!=2 && world.get_state()!=11)break;
                magic_counter();
            //System.out.println("counter++");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
