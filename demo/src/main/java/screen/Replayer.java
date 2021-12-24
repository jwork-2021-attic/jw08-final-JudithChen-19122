package screen;

import world.Map;

public class Replayer implements Runnable {

    Map map;
    boolean li;
    String[] numbers;
    int temp;
    final String t1 = "move";
    final String t2 = "magic";

    public Replayer(Map tmap, String[] x) {
        map = tmap;
        li = true;
        numbers = x;
        temp = 0;
    }

    public void finish_play() {
        li = false;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (li) {
            if (temp >= numbers.length)
                break;
            String[] stmp = null;
            stmp = numbers[temp].split(" ");
            if (stmp[0].equals(t1)) {
                map.moveAction(Integer.parseInt(stmp[1]), Integer.parseInt(stmp[2]), Integer.parseInt(stmp[3]),
                        Integer.parseInt(stmp[4]));
            }
            else if(stmp[0].equals(t2))
                map.set_inmagic();
            temp++;

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
