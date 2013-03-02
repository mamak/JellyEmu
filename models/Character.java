package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.TreeMap;
import jelly.Jelly;
import system.*;

public class Character {
    protected int id;
    protected String name;
    protected int accountId;
    protected int level=1;

    protected int color1;
    protected int color2;
    protected int color3;
    protected int gfxid;
    protected byte sexe;

    protected byte charClass;
    protected object.CharacterClass _class;

    public Character(int id, String name, byte charClass, int level, int account, int color1, int color2, int color3, int gfxid, byte sexe){
        this.id=id;
        this.name=name;
        this.level=level;
        accountId=account;

        this.color1=color1;
        this.color2=color2;
        this.color3=color3;
        this.gfxid=gfxid;
        this.sexe=sexe;

        this.charClass=charClass;

        _class=object.CharacterClass.getClass(charClass, level, sexe);

        if(this.gfxid==-1) {
            this.gfxid=_class.getDefaultGfxid();
        }
    }

    /**
     * Crée un personnage
     * Valeurs de retour :
     * 0 : succès
     * 1 : nom existant
     * 2 : compte plein
     * @param name
     * @param CharClass
     * @param color1
     * @param color2
     * @param color3
     * @param account
     * @return
     */
    public static int create(String name, int CharClass, int color1, int color2, int color3, int account){
        if(nameExists(name))
            return 1;
        
        try{
            PreparedStatement stmt = Jelly._database.prepare("INSERT INTO characters(name, account) VALUES(?,?)");
            stmt.setString(1, name);
            stmt.setInt(2, account);            
            stmt.execute();

            return 0;
        }catch(Exception e){
            return 1;
        }
    }

    /*
     * Fonctions de chargement
     */

    /**
     * Crée un personnage, via un ResultSet
     * @param RS
     * @return
     */
    protected static Character parseResultSet(ResultSet RS){
        try{
            return new Character(
                    RS.getInt("id"),
                    RS.getString("name"),
                    RS.getByte("class"),
                    RS.getInt("level"),
                    RS.getInt("account"),
                    RS.getInt("color1"),
                    RS.getInt("color2"),
                    RS.getInt("color3"),
                    RS.getInt("gfxid"),
                    RS.getByte("sexe")
            );
        }catch(Exception e){
            Debug.databaseError(e, "Character - analyse du ResultSet impossible");
        }
        return null;
    }

    /**
     * Charge les personnage d'un compte
     * @param id
     * @return
     */
    public static java.util.Map<Integer, Character> loadByAccount(int id){
        Debug.info("Character : chargement des personnages du compte n°"+id);
        java.util.Map<Integer, Character> characters = new TreeMap<>();

        try{
            PreparedStatement stmt = Jelly._database.prepare("SELECT * FROM characters WHERE account = ?");
            stmt.setInt(1, id);
            ResultSet RS = stmt.executeQuery();

            while(RS.next()){
                characters.put(RS.getInt("id"),
                        parseResultSet(RS)
                );
            }
        }catch(Exception e){
            Debug.databaseError(e, "Character - impossible de charger les personnages du compte n°"+id);
        }

        return characters;
    }

    /**
     * Charge le personnage en fonction de son nom
     * @param name
     * @return
     */
    public static Character load(String name){
        try{
            PreparedStatement stmt = Jelly._database.prepare("SELECT * FROM characters WHERE name = ?");
            stmt.setString(1, name);
            ResultSet RS = stmt.executeQuery();
            
            if(!RS.next()){
                Debug.print("Character : personnage inexistant '"+name+"'");
                return null;
            }

            return parseResultSet(RS);
        }catch(Exception e){
            Debug.databaseError(e, "Character - Impossible de charger le personnage '"+name+"'");
            return null;
        }
    }

    /**
     * Test si le pseudo existe déjà
     * @param name
     * @return
     */
    public static boolean nameExists(String name){
        try{
            PreparedStatement stmt = Jelly._database.prepare("SELECT COUNT(*) AS num FROM characters WHERE name = ?");
            stmt.setString(1, name);
            ResultSet RS = stmt.executeQuery();
            RS.next();
            return RS.getByte("num") > 0;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Génération d'un nom aléatoire (béta, nom totalement satisfaisant)
     * @return
     */
    public static String generateName(){
        String name="";
        Random rand = new Random();

        int size = rand.nextInt(6) + 4;

        String[] prefix = {
            "Rex", "Xer", "Oy", "Mel", "Weir", "Kor", "Swi", "Tco", "Ret",
            "Kit", "Rom", "Bir", "Nor", "Your", "Yor", "Kra", "Ken", "Tar",
            "Heit", "Thre", "Cys", "Jil", "Fire", "As", "Flow", "Rhi", "Luc",
            "Hug", "Aim", "Bug", "Cris", "Del", "Ety", "Fal", "Gli", "Inn",
            "Jet", "Lin", "Mop", "Nai", "Otis", "Psy", "Quel", "Rav", "Stri",
            "Try", "Ug", "Vis", "Wes", "Xult", "Yoh", "Zyr"
        };

        String cons = "zrtpqsdfghjklmwxcvbn";
        String voy = "aeiouy";

        String[] s1 = {
            "si", "ma", "li", "wei", "po", "se", "bo", "wo", "ka", "moa", "la",
            "bro", "fu", "sur", "you", "jo", "plo", "gor", "stu", "wel", "lis",
            "cu"
        };

        String[] s2 = {
            "elle", "el", "il", "al", "en", "ut", "olin", "ed", "er", "ije",
            "era", "owei", "edi", "arc", "up", "ufo", "ier", "ead", "ing", "ana"
        };

        name = prefix[rand.nextInt(prefix.length-1)];

        while(name.length()<size){
            if(cons.contains(name.substring(name.length()-1))){
                name+=voy.charAt(rand.nextInt(voy.length()));
                if(name.length()>size)
                    break;
                name+=s1[rand.nextInt(s1.length)];
            }else{
                name+=cons.charAt(rand.nextInt(cons.length()));
                if(name.length()>size)
                    break;
                name+=s2[rand.nextInt(s2.length)];
            }
        }

        //resize le nom à la bonne taille (de 4 à 10)
        name=name.substring(0, size);

        return name;
    }

    /*
     * getters / setters
     */

    public int getId(){
        return id;
    }

    public int getGfxId(){
        return gfxid;
    }

    @Override
    /**
     * linéarise le personnage pour le passer en packet
     */
    public String toString(){
        String str = "";

        //id;nom;level;gfxid;couleur1;couleur2;couleur3;stuff;marchand;1;deathcount;levelmax;
        str+=id+";"+name+";"+level+";"+gfxid+";"+color1+";"+color2+";"+color3+";;0;"+level+";;;";

        return str;
    }

    public String getAsPacket(){
        StringBuilder str = new StringBuilder();

        

        return str.toString();
    }
}
