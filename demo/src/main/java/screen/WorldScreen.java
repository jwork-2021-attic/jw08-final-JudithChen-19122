package screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import world.Map;
import world.Monster;
import world.Wall;
import world.Beans;

import asciiPanel.AsciiPanel;
import network.Client;
import network.Server;
import world.Calabashbros;
import world.Creature;
import world.Floor;
import world.Heart;
import world.Magic_prop;
import world.World;

public class WorldScreen implements Screen {

    private Map map;
    private World world;
    private Calabashbros player;
    ArrayList<Creature> creatures;
    private int[][] maze;
    private int[][] map_for_props;
    String[] moveSteps; //take record of moveSteps so we can recover the game
    String log; //log for recover the game
    private Wall wall;
    private Floor floor;
    private ExecutorService exec;
    private int state;
    private Color white;
    private Color red;
    private int goal;
    String saved_game;
    int id;

    private Client client;
    private Server server;
    final String t1 = "move";
    final String t2 = "magic";
    final String t3 = "map";
    final String t4 = "id";


    private Calabashbros[] players;
    
    Monster monster1;
    Monster monster2;
    Monster monster3;
    Monster monster4;
    Monster monster5;
    Monster monster6;

    Replayer replayer;


    public int get_state(){
        return state;
    }

    public void set_id(int i){
        id = i;
    }

    public Map get_map(){
        return map;
    }

    public void sent_Server_info(String s){
        if(state != 9 && state != 11 ) return;
        server.send_info(s);
    }

    public String server_receive_info(String s){
        String[] x = s.split(" ");
        int id = Integer.parseInt(x[0]);
        int dir = Integer.parseInt(x[1]);
        if(players[id]!=null && !players[id].checkifdead())
            players[id].moveAction(dir);
        return s;
    }

    public String client_receive_info(String s) throws IOException{
            String[]temp = s.split("\n");
            for(int i=0;i<temp.length;i++){
                String[] stmp = null;
                stmp = temp[i].split(" ");
                if(stmp[0].equals(t3)){ // receive prop map info
                    map_for_props=new int[30][30];
                    for(int j=0;j<30;j++){
                        i++;
                        stmp = temp[i].split(" ");
                        for (int k = 0; k < 30; k++) {
                            map_for_props[j][k] = Integer.parseInt(stmp[k]);
                        } 
                    }
                    build_client_online();
                }
                else if (stmp[0].equals(t1)) {
                    map.moveAction(Integer.parseInt(stmp[1]), Integer.parseInt(stmp[2]), Integer.parseInt(stmp[3]),
                    Integer.parseInt(stmp[4]));
                }
                else if(stmp[0].equals(t2))
                    map.set_inmagic();
                else if(stmp[0].equals(t4))
                    id = Integer.parseInt(stmp[1]);
            }
            return s;
    }

    public void set_online_monster_player(){
         // set players
         players[0] = new Calabashbros(new Color(204, 0, 0), (char) 2, world, map , 1);
         world.put(players[0], 17, 10);
         creatures.add(players[0]);
         map.add_creature_to_map(3, 17, 10);

         players[1] = new Calabashbros(new Color(244, 165, 0), (char) 2, world, map , 1);
         world.put(players[1], 16, 10);
         creatures.add(players[1]);
         map.add_creature_to_map(3, 16, 10);

        players[2] = new Calabashbros(new Color(252, 233, 79), (char) 2, world, map, 1);
        world.put(players[2], 15, 10);
        creatures.add(players[2]);
        map.add_creature_to_map(3, 15, 10);
 
        players[0].setgoal(goal);
        players[1].setgoal(goal);
        players[2].setgoal(goal);
 
         // set monsters
         monster1 = new Monster(new Color(0, 0, 139), (char) 15, world, map);
         monster2 = new Monster(new Color(139, 28, 98), (char) 15, world, map);
         monster3 = new Monster(new Color(139, 125, 123), (char) 15, world, map);
         monster4 = new Monster(new Color(0, 0, 139), (char) 15, world, map);
         monster5 = new Monster(new Color(139, 28, 98), (char) 15, world, map);
         monster6 = new Monster(new Color(139, 125, 123), (char) 15, world, map);
         world.put(monster1, 22, 19);
         world.put(monster2, 7, 10);
         world.put(monster3, 10, 25);
         world.put(monster4, 2, 9);
         world.put(monster5, 13, 4);
         world.put(monster6, 20, 19);
         creatures.add(monster1);
         creatures.add(monster2);
         creatures.add(monster3);
         creatures.add(monster4);
         creatures.add(monster5);
         creatures.add(monster6);
         map.add_creature_to_map(2, 22, 19);
         map.add_creature_to_map(2, 7, 10);
         map.add_creature_to_map(2, 10, 25);
         map.add_creature_to_map(2, 2, 9);
         map.add_creature_to_map(2, 13, 4);
         map.add_creature_to_map(2, 20, 19);

    }

    public int build_client_online() throws IOException{
        //state = 10;
        world = new World();
        //world.set_state(10);
        creatures = new ArrayList<Creature>();
        wall = new Wall(world);

        // build map world
        map = new Map(world);
        map.load_map();
        map.init_log();

        maze = map.get_init_map();
        map_for_props[20][25] = 1;
        map.load_saved_map(maze, map_for_props);
        put_wall_beans();
        goal=map.get_total_beans();
        //System.out.println("client goal: " + goal);
        //set monster and player
        set_online_monster_player();

        state = 12;
        world.set_state(12);

        map.init_creatures(creatures);

        return state;

    }

    public int build_online_game() throws IOException{
        //state = 9;
        world = new World();
        //world.set_state(9);
        creatures = new ArrayList<Creature>();
        wall = new Wall(world);

        // build map world
        map = new Map(world, server);
        map.load_map();
        map.init_log();

        maze = map.get_init_map();
        map_for_props = map.get_prop_map();
        map_for_props[20][25] = 1;
        map.load_saved_map(maze, map_for_props);
        put_wall_beans();
        goal=map.get_total_beans();
        //System.out.println("server goal: " + goal);

        //set monster and player
        set_online_monster_player();

        int[][] now_prop_map = map.get_prop_map();
        String prop_init="map"+"\n";
        for(int i=0; i<30;i++){
            for(int j=0; j<30;j++){
                prop_init += now_prop_map[i][j] + " ";
            }
            prop_init += "\n";
        }
        server.send_info(prop_init);
 

        state = 11;
        world.set_state(11);

        map.init_creatures(creatures);
  
        exec = Executors.newCachedThreadPool();
        //System.out.println("creatures:" + creatures.size());
        for(int i=0;i<creatures.size();i++){
            exec.execute(new Thread(creatures.get(i)));
        }

        return state;

    }

    public void excute_log() throws IOException{
        BufferedReader bf = new BufferedReader(new FileReader("mylog.txt"));
        String textLine = new String();
        String str = new String();
        while ((textLine = bf.readLine()) != null) {
            str += textLine + ",";
        }
        bf.close();

        String[] numbers = str.split(",");
        replayer = new Replayer(map,numbers); 
        Thread replayer_thr = new Thread(replayer);
        replayer_thr.start();
    }

    public void save_game() throws IOException{
        //saving log
        log=map.get_log();
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter("mylog.txt",true));
        writer.write(log);
        writer.flush();
        writer.close();

        //saving map
        saved_game="";
        int[][] now_map = map.get_map();
        for(int i=0; i<30;i++){
            for(int j=0; j<30;j++){
                saved_game += now_map[i][j] + " ";
            }
            saved_game += "\n";
        }
        //saving map prop
        int[][] now_prop_map = map.get_prop_map();
        for(int i=0; i<30;i++){
            for(int j=0; j<30;j++){
                saved_game += now_prop_map[i][j] + " ";
            }
            saved_game += "\n";
        }
        //saving monster
        if(!monster1.checkifdead()) saved_game += monster1.saving_state(); else saved_game += "d\n";
        if(!monster2.checkifdead()) saved_game += monster2.saving_state(); else saved_game += "d\n";
        if(!monster3.checkifdead()) saved_game += monster3.saving_state(); else saved_game += "d\n";
        if(!monster4.checkifdead()) saved_game += monster4.saving_state(); else saved_game += "d\n";
        if(!monster5.checkifdead()) saved_game += monster5.saving_state(); else saved_game += "d\n";
        if(!monster6.checkifdead()) saved_game += monster6.saving_state(); else saved_game += "d\n";
        //saving player
        if(!player.checkifdead()) saved_game += player.saving_state(); else saved_game += "d\n";

        BufferedWriter writer2;
        writer2 = new BufferedWriter(new FileWriter("saved_game.txt"));
        writer2.write(saved_game);
        writer2.flush();
        writer2.close();

    }

    public void save_init_prop() throws IOException{
        int[][] now_prop_map = map.get_prop_map();
        String prop_init="";
        for(int i=0; i<30;i++){
            for(int j=0; j<30;j++){
                prop_init += now_prop_map[i][j] + " ";
            }
            prop_init += "\n";
        }
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter("init_prop_map.txt"));
        writer.write(prop_init);
        writer.flush();
        writer.close();
    }

    public void set_monster(String[] stmp, Monster m){
        m.set_monster(Boolean.valueOf(stmp[2]).booleanValue(), Boolean.valueOf(stmp[3]).booleanValue(), Integer.parseInt(stmp[4]));
    }

    public int rebuild_saved_game() throws IOException{
        state = 6;
        world = new World();
        world.set_state(6);
        creatures = new ArrayList<Creature>();
        wall = new Wall(world);
        map = new Map(world);
        map_for_props=new int[30][30];

        //get info from file
        BufferedReader bf = new BufferedReader(new FileReader("saved_game.txt"));
        String textLine = new String();
        String str = new String();
        while ((textLine = bf.readLine()) != null) {
            str += textLine + ",";
        }

        bf.close();

        String[] numbers = str.split(",");
        String[] stmp = null;
        int[][]old_map=new int[30][30];
        //setmap
        for (int i = 0; i < 30; i++) {
            stmp = numbers[i].split(" ");
            for (int j = 0; j < 30; j++) {
                old_map[i][j] = Integer.parseInt(stmp[j]);
            }
        }
        for (int i = 30; i < 60; i++) {
            stmp = numbers[i].split(" ");
            for (int j = 0; j < 30; j++) {
                map_for_props[i-30][j] = Integer.parseInt(stmp[j]);
            }
        }
        map.load_saved_map(old_map, map_for_props);
        //put wall
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                if (old_map[i][j] == 0)
                    world.put(wall, i, j);
            }
        }
        //put beans
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                if (map_for_props[i][j] == 4)
                    world.put_props(new Beans(world), i, j);
                else if (map_for_props[i][j] == 5)
                    world.put_props(new Magic_prop(world), i, j);
                else if (map_for_props[i][j] == 6)
                    world.put_props(new Heart(world), i, j);
            }
        }


        //set monster
        String t="d";
        stmp=numbers[60].split(" ");
        if(!stmp[0].equals(t)){
            monster1 = new Monster(new Color(0, 0, 139), (char) 15, world, map);
            set_monster(stmp,monster1);
            world.put(monster1, Integer.parseInt(stmp[0]), Integer.parseInt(stmp[1]));
            creatures.add(monster1);
        }
        stmp=numbers[61].split(" ");
        if(!stmp[0].equals(t)){
            monster2 = new Monster(new Color(139, 28, 98), (char) 15, world, map);
            set_monster(stmp,monster2);
            world.put(monster2, Integer.parseInt(stmp[0]), Integer.parseInt(stmp[1]));
            creatures.add(monster2);
        }
        stmp=numbers[62].split(" ");
        
        if(!stmp[0].equals(t)){ 
            monster3 = new Monster(new Color(139, 125, 123), (char) 15, world, map);
            set_monster(stmp,monster3);
            world.put(monster3, Integer.parseInt(stmp[0]), Integer.parseInt(stmp[1]));
            creatures.add(monster3);
        }
        stmp=numbers[63].split(" ");
        if(!stmp[0].equals(t)){ 
            monster4 = new Monster(new Color(0, 0, 139), (char) 15, world, map);
            set_monster(stmp,monster4);
            world.put(monster4, Integer.parseInt(stmp[0]), Integer.parseInt(stmp[1]));
            creatures.add(monster4);
        }
        stmp=numbers[64].split(" ");
        if(!stmp[0].equals(t)){ 
            monster5 = new Monster(new Color(139, 28, 98), (char) 15, world, map);
            set_monster(stmp,monster5);
            world.put(monster5, Integer.parseInt(stmp[0]), Integer.parseInt(stmp[1]));
            creatures.add(monster5);
        }
        stmp=numbers[65].split(" ");
        if(!stmp[0].equals(t)){ 
            monster6 = new Monster(new Color(139, 125, 123), (char) 15, world, map);
            set_monster(stmp,monster6);
            world.put(monster6, Integer.parseInt(stmp[0]), Integer.parseInt(stmp[1]));
            creatures.add(monster6);
        }

        //set player
        stmp=numbers[66].split(" ");
        if(!stmp[0].equals(t)){
            player = new Calabashbros(new Color(204, 0, 0), (char) 2, world, map);
            player.set_calabashbros(Boolean.valueOf(stmp[2]).booleanValue(), 
            Boolean.valueOf(stmp[3]).booleanValue(), 
            Integer.parseInt(stmp[4]),
            Integer.parseInt(stmp[5]),
            Integer.parseInt(stmp[6]),
            Integer.parseInt(stmp[7]));
            world.put(player, Integer.parseInt(stmp[0]), Integer.parseInt(stmp[1]));
            creatures.add(player);
        }

        if(Integer.parseInt(stmp[6])<10) map.set_has_magic();

        map.init_creatures(creatures);

        state=2;
        world.set_state(2);
        exec = Executors.newCachedThreadPool();
        for(int i=0;i<creatures.size();i++){
            exec.execute(new Thread(creatures.get(i)));
        }

        return state;
    }

    public int replay_game_screen() throws IOException{
        //back to the start


        state = 7;
        world = new World();
        world.set_state(7);
        creatures = new ArrayList<Creature>();
        wall = new Wall(world);

        map = new Map(world);
        map.load_map();
        map.load_old_prop_map();
        map_for_props=map.get_prop_map();
        maze = map.get_init_map();
        put_wall_beans();
        goal=map.get_total_beans();
        set_monster_player();
        map.init_creatures(creatures);
        

        //replay
        excute_log();

        return state;



    }

    public void set_monster_player(){
        // set player
        player = new Calabashbros(new Color(204, 0, 0), (char) 2, world, map);
        world.put(player, 17, 10);
        creatures.add(player);
        map.add_creature_to_map(3, 17, 10);

        player.setgoal(goal);

        // set monsters
        monster1 = new Monster(new Color(0, 0, 139), (char) 15, world, map);
        monster2 = new Monster(new Color(139, 28, 98), (char) 15, world, map);
        monster3 = new Monster(new Color(139, 125, 123), (char) 15, world, map);
        monster4 = new Monster(new Color(0, 0, 139), (char) 15, world, map);
        monster5 = new Monster(new Color(139, 28, 98), (char) 15, world, map);
        monster6 = new Monster(new Color(139, 125, 123), (char) 15, world, map);
        world.put(monster1, 22, 19);
        world.put(monster2, 7, 10);
        world.put(monster3, 10, 25);
        world.put(monster4, 2, 9);
        world.put(monster5, 13, 4);
        world.put(monster6, 20, 19);
        creatures.add(monster1);
        creatures.add(monster2);
        creatures.add(monster3);
        creatures.add(monster4);
        creatures.add(monster5);
        creatures.add(monster6);
        map.add_creature_to_map(2, 22, 19);
        map.add_creature_to_map(2, 7, 10);
        map.add_creature_to_map(2, 10, 25);
        map.add_creature_to_map(2, 2, 9);
        map.add_creature_to_map(2, 13, 4);
        map.add_creature_to_map(2, 20, 19);

    }

    public void put_wall_beans(){
        //putwall
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                if (maze[i][j] == 0)
                    world.put(wall, i, j);
            }
        }

        // put props
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                if (map_for_props[i][j] == 4)
                    world.put_props(new Beans(world), i, j);
                else if (map_for_props[i][j] == 5)
                    world.put_props(new Magic_prop(world), i, j);
                else if (map_for_props[i][j] == 6)
                    world.put_props(new Heart(world), i, j);
            }
        }

    }

    public int build_game_screen() throws IOException {
        //clear log file
        log="";
        File file =new File("mylog.txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //state = 2;
        world = new World();
        //world.set_state(2);
        creatures = new ArrayList<Creature>();
        wall = new Wall(world);


        // build map world
        map = new Map(world);
        map.load_map();
        map.init_log();

        // map.change();
        maze = map.get_init_map();
        map_for_props = map.get_prop_map();
        //ready for play back from the start
        save_init_prop();
        // put wall and beans;
        put_wall_beans();

        goal=map.get_total_beans();

        //set monster and player
        set_monster_player();

        //set the world before print so we can avoid null pointer
        state = 2;
        world.set_state(2);

        map.init_creatures(creatures);

        exec = Executors.newCachedThreadPool();
        for(int i=0;i<creatures.size();i++){
            exec.execute(new Thread(creatures.get(i)));
        }

        return state;
    }

    public int build_start_screen() {
        state = 1;
        world = new World();
        world.set_state(1);
        return state;
    }

    public void show_start(AsciiPanel terminal) {

        String a="PRESS \"A\" TO START";
        String b="PRESS \"B\" TO CONTINUE";
        String c="PRESS \"C\" TO PLAYBACK";
        String d="PRESS \"H\" TO BE HOST";
        String e="PRESS \"J\" TO JION";
        String f="Online game only support";
        String g="one server two client";

        for(int i=0;i<a.length();i++){
            terminal.write((char) a.charAt(i), 4+i, 5, white);
        }
        for(int i=0;i<b.length();i++){
            terminal.write((char) b.charAt(i), 4+i, 10, white);
        }
        for(int i=0;i<c.length();i++){
            terminal.write((char) c.charAt(i), 4+i, 15, white);
        }
        for(int i=0;i<d.length();i++){
            terminal.write((char) d.charAt(i), 4+i, 20, white);
        }
        for(int i=0;i<e.length();i++){
            terminal.write((char) e.charAt(i), 4+i, 24, white);
        }
        for(int i=0;i<f.length();i++){
            terminal.write((char) f.charAt(i), 2+i, 27, white);
        }
        for(int i=0;i<g.length();i++){
            terminal.write((char) g.charAt(i), 4+i, 28, white);
        }



    }

    public void show_fail(AsciiPanel terminal) {
        String f="YOU FAILED!";
        for(int i=0;i<f.length();i++){
            terminal.write((char) f.charAt(i), 8+i, 15, white);
        }

    }

    public void show_win(AsciiPanel terminal) {
        String w="YOU WIN!";
        for(int i=0;i<w.length();i++){
            terminal.write((char) w.charAt(i), 11+i, 15, white);
        }        
    }

    public void show_wait(AsciiPanel terminal){
        String w="WAITING...";
        for(int i=0;i<w.length();i++){
            terminal.write((char) w.charAt(i), 9+i, 15, white);
        }        
    }

    public void show_game_info(AsciiPanel terminal) {
        String l="LIFE:";

        for(int i=0;i<l.length();i++){
            terminal.write((char) l.charAt(i), i, 30, white);
        } 
   
        for (int i = 0; i < player.getlife(); i++) {
            terminal.write((char) 3, 6 + i, 30, red);
        }

        String q="Press Q to save&quit";
        for(int i=0;i<q.length();i++){
            terminal.write((char) q.charAt(i), i+10, 30, white);
        } 
    }

    public void show_game_info(AsciiPanel terminal, int id) {
        String l="LIFE:";

        for(int i=0;i<l.length();i++){
            terminal.write((char) l.charAt(i), i, 30, white);
        } 
   
        for (int i = 0; i < players[id].getlife(); i++) {
            terminal.write((char) 3, 6 + i, 30, red);
        }
    }

    public int build_fail_screen() {
        state = 4;
        map=null;
        creatures=null;
        player=null;
        
        world = new World();
        world.set_state(4);
        return state;
    }

    public int build_won_screen() {
        state = 3;
        map=null;
        creatures=null;
        player=null;
        world = new World();
        world.set_state(3);
        return state;
    }

    public int build_wait_screen_server(){
        state = 9;
        world = new World();
        world.set_state(9);
        server = new Server(this); 
        Thread server_thr = new Thread(server);
        server_thr.start();
        return state;
    }

    public int build_wait_screen_client() throws UnknownHostException, IOException{
        state = 10;
        world = new World();
        world.set_state(10);
        client = new Client(this);
        client.connect(12345);
        return state;
    }

    public WorldScreen() throws IOException {
        players = new Calabashbros[4];
        id = 0;
        white = new Color(255, 255, 255);
        red = new Color(255, 0, 0);
        build_start_screen();
        //build_game_screen();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {
                if (state == 2 || state == 7) {
                    terminal.write(world.get_props(x, y).getGlyph(), x, y, world.get_props(x, y).getColor());
                    if (world.get(x, y).get_type() != 1)
                        terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());
                    show_game_info(terminal);

                    if(player.checkifdead()) build_fail_screen();
                    else if(player.checkifwin()) build_won_screen();

                } else if (state == 1) {
                    show_start(terminal);
                } else if (state == 3) {
                    show_win(terminal);
                } else if (state == 4) {
                    show_fail(terminal);
                } else if(state == 9||state == 10){
                    show_wait(terminal);
                }
                if(state == 11 || state == 12){
                    terminal.write(world.get_props(x, y).getGlyph(), x, y, world.get_props(x, y).getColor());
                    if (world.get(x, y).get_type() != 1)
                        terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());
                    show_game_info(terminal, id);
                    if(players[0].checkifdead()&&players[1].checkifdead()&&players[2].checkifdead()) build_fail_screen();
                    else if(players[id].checkifwin()) build_won_screen();
                }
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) throws IOException {
        if(state==1)
        {
            if(key.getKeyCode()==KeyEvent.VK_A){
                build_game_screen();
            }
            else if(key.getKeyCode()==KeyEvent.VK_B){
                rebuild_saved_game();
            }
            else if(key.getKeyCode()==KeyEvent.VK_C){
                replay_game_screen();
            }
            else if(key.getKeyCode()==KeyEvent.VK_H){
                build_wait_screen_server();
            }
            else if(key.getKeyCode()==KeyEvent.VK_J){
                build_wait_screen_client();
            }
        }
        else if (state == 2) {
            switch (key.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.moveAction(1);
                    break;
                case KeyEvent.VK_S:
                    player.moveAction(2);
                    break;
                case KeyEvent.VK_A:
                    player.moveAction(3);
                    break;
                case KeyEvent.VK_D:
                    player.moveAction(4);
                    break;
                case KeyEvent.VK_Q:
                    state = 1;
                    save_game();
                    build_start_screen();
                    break;
                default:
                    ;
            }
        }
        else if(state == 7){
            if(key.getKeyCode()==KeyEvent.VK_Q){
                replayer.finish_play();
                state = 1;
                build_start_screen();
            }
        }
        else if(state==12){
            switch (key.getKeyCode()) {
                case KeyEvent.VK_W:
                    client.send( id +" 1");
                    break;
                case KeyEvent.VK_S:
                    client.send( id +" 2");
                    break;
                case KeyEvent.VK_A:
                    client.send( id +" 3");
                    break;
                case KeyEvent.VK_D:
                    client.send( id +" 4");
                    break;
                default:
                    ;
            }   
        }
        else if(state==11){
            switch (key.getKeyCode()) {
                case KeyEvent.VK_W:
                    players[0].moveAction(1);
                    break;
                case KeyEvent.VK_S:
                    players[0].moveAction(2);
                    break;
                case KeyEvent.VK_A:
                    players[0].moveAction(3);
                    break;
                case KeyEvent.VK_D:
                    players[0].moveAction(4);
                    break;
                default:
                    ;
            }
        }
        return this;
    }

}
