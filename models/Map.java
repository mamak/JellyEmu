package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import jelly.Jelly;
import system.*;

public class Map implements java.io.Serializable {
    protected int id;
    protected int X;
    protected int Y;
    protected ArrayList _cells = new ArrayList();

    public Map(int id, String pos, String mapdata){
        this.id=id;

        X = Integer.parseInt(pos.split(",")[0]);
        Y = Integer.parseInt(pos.split(",")[1]);
        loadCells(mapdata);
    }

    private void loadCells(String mapdata){
        int length = mapdata.length();
        for(int i=0;i<length;i+=10)
        {
            _cells.add(new Cell(id, mapdata.substring(i, i+10)));
        }
    }

    public static int parseHashChar(char c){
        char[] HASH = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
	        't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
	        'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
        };

        int count = HASH.length;

        for(int i=0;i<count;i++)
        {
            if(HASH[i]==c)
            {
                return i;
            }
        }

        return -1;
    }

    protected static class Cell implements java.io.Serializable
    {
        protected int mapId;
        protected boolean walkable;
        protected int obj;
        
        public Cell(int map_id, String cellData){
            mapId=map_id;
            walkable=((Map.parseHashChar(cellData.charAt(2)) & 56) >> 3) != 0;
        }
    }

    public static java.util.Map<Integer, Map> getAll(){
        java.util.Map<Integer, Map> maps = new java.util.TreeMap<>();
        try{
            ResultSet RS = Jelly._database.query("SELECT * FROM maps");
            int id;
            while(RS.next()){
                id = RS.getInt("id");
                maps.put(id, new Map(
                        id,
                        RS.getString("mappos"),
                        RS.getString("mapData")
                ));
            }
            return maps;
        }catch(Exception e)
        {
            Debug.databaseError(e, "Chargement des maps impossible.");
            return null;
        }
    }
}
