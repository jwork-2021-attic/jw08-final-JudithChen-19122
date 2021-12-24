package screen;
import org.junit.Test;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import world.World;

import static org.junit.Assert.*;

import java.io.IOException;


public class ScreenTest {
    @Test
    public void testScreen() throws IOException, InterruptedException {
        AsciiPanel terminal = new AsciiPanel(World.WIDTH, World.HEIGHT+1, AsciiFont.NCP437_16x16);
        WorldScreen screenx= new WorldScreen();
        screenx.build_fail_screen();
        assertEquals(4, screenx.get_state());
        screenx.displayOutput(terminal);
       
        screenx.build_won_screen();
        assertEquals(3, screenx.get_state());
        screenx.displayOutput(terminal);

        screenx.build_game_screen();
        assertEquals(2, screenx.get_state());
        screenx.displayOutput(terminal);
        
        screenx.build_start_screen();
        assertEquals(1, screenx.get_state());
        screenx.displayOutput(terminal);
        
        screenx.rebuild_saved_game();
        assertEquals(2, screenx.get_state());
        screenx.displayOutput(terminal);
       
        screenx.replay_game_screen();
        assertEquals(7, screenx.get_state());
        screenx.displayOutput(terminal);
        
        assertEquals(1, 1);
    }
}
