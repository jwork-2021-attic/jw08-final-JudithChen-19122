
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;

import world.World;
import screen.Refresher;
import screen.Screen;
import screen.WorldScreen;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

public class Main extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    private Refresher refresher;

    public Main() throws IOException {
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT+1, AsciiFont.NCP437_16x16);
        add(terminal);
        pack();
        screen = new WorldScreen();
        addKeyListener(this);
        repaint();
        refresher = new Refresher(this,screen); 
        Thread refresher_thr = new Thread(refresher);
        refresher_thr.start();
    }

    @Override
    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            screen = screen.respondToUserInput(e);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) throws IOException {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        app.setVisible(true); 

    }

}
 