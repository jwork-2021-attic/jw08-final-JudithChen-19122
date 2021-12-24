package world;

public class World {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private Tile<Thing>[][] tiles;
    private Tile<Thing>[][] tiles_props;// the game item includes beans and magic props

    private int state;

    public int get_state(){
        return state;
    }

    public void set_state(int s){
        state = s;
    }

    public World() {
        state = 1;

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
            }
        }

        if (tiles_props == null) {
            tiles_props = new Tile[WIDTH][HEIGHT];
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles_props[i][j] = new Tile<>(i, j);
                tiles_props[i][j].setThing(new Floor(this));
            }
        }
    }

    public Thing get_props(int x, int y) {
        return this.tiles_props[x][y].getThing();
    }

    public void put_props(Thing t, int x, int y) {
        this.tiles_props[x][y].setThing(t);
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

    public void swap(int x, int y ,int nx, int ny){
        Thing t1=get(x,y);
        Thing t2=get(nx,ny);
        put(t1,nx,ny);
        put(t2,x,y);
    }

    
}
