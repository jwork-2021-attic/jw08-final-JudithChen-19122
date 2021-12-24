package world;

import java.awt.Color;

public class Heart extends Thing {

    public Heart(World world) {
        super(new Color(255,0,0), (char) 3, world, 6);
    }

}