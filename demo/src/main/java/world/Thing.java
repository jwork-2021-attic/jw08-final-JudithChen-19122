package world;

import java.awt.Color;

public class Thing {

    int type; //0: wall 1:floor 2:monster 3:calabash 4:beans 5:magic 6:heart

    protected World world;

    public Tile<? extends Thing> tile; 

    public int getX() {
        return this.tile.getxPos();
    }

    public int getY() {
        return this.tile.getyPos();
    }

    public void setTile(Tile<? extends Thing> tile) {
        this.tile = tile;
    }

    Thing(Color color, char glyph, World world, int type) {
        this.color = color;
        this.glyph = glyph;
        this.world = world;
        this.type = type;
    }

    protected Color color;

    public int get_type(){
        return type;
    }

    public Color getColor() {
        return this.color;
    }

    protected char glyph;

    public char getGlyph() {
        return this.glyph;
    }

}
