/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Random;

/**
 *
 * @author vincent
 */
public class Player implements core.database.Model {
    public int id;
    public String name;
    public int accountId;
    public int level=1;

    public byte color1;
    public byte color2;
    public byte color3;
    public int gfxid;
    public byte sexe;

    public byte classId;

    /**
     * Génération d'un nom aléatoire (béta, non totalement satisfaisant)
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

    public void clear(){
        id = 0;
    }

    public int getPk(){
        return id;
    }
}
