package world;

import org.junit.Test;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import screen.WorldScreen;
import world.World;

import static org.junit.Assert.*;

import java.io.IOException;


public class WorldTest {
    @Test
    public void testWorld() throws IOException {
        WorldScreen screenx= new WorldScreen();
        screenx.build_game_screen();
        Map map = screenx.get_map();
        for(int i=17;i>7;i--){ //calashbro to monster
            map.moveAction(i, 10, i-1, 10);
        }
        assertEquals(map.checkmap(17, 10), 3);
    }
}

