package jelly;

import system.*;

public class Jelly {
    public static boolean DEBUG=true;
    public static int LOG=2;
    
    public static Config _config;
    public static Database _database;
    public static game.World _world;
    public static realm.RealmServer _realm;
    public static game.GameServer _game;
    
    public static long start;

    public static Commands.Console _console;

    public static boolean running = true;
    
    public static void main(String[] args) {
        start=System.currentTimeMillis();
        System.out.println("======================================================");
        System.out.println("\t\tJelly-emu by v4vx");
        System.out.println("Version 0.1");
        System.out.println("======================================================");
        _config=new Config();
        _database=new Database();
        _world=new game.World();
        _realm=new realm.RealmServer();
        _realm.start();
        _game=new game.GameServer();
        _game.start();

        try{
            Thread.sleep(1000);
        }catch(Exception e){}

        _console = new Commands.Console();
        _console.start();
    }
    
    public static void turnoff(){
        Debug.print("Arrêt en cours...");
        System.exit(1);
    }

    public static void restart(){
        Debug.print("Redémarrage en cours...");
        _console.interrupt();
        _console=null;
        _realm.closeServer();
        _realm.interrupt();
        _realm=null;
        System.gc();
        main(null);
    }
}
