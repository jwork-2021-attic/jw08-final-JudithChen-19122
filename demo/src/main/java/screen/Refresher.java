package screen;

import javax.swing.JFrame;

public class Refresher implements Runnable{

    private JFrame mainT;
    private Screen screen;

    public Refresher(JFrame maintt,Screen screen) {
        this.mainT = maintt;
        this.screen = screen;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true){
            mainT.repaint();
            //System.out.println("refresher repaint");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
}
