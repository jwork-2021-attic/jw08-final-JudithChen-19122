import org.junit.Test;


import static org.junit.Assert.*;

import java.io.IOException;

import javax.swing.JFrame;

public class MainTest {
    @Test
    public void testMain() throws IOException  {
        Main main= new Main();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        main.setVisible(true); 
        
        assertEquals(1, 1);
    }
}
