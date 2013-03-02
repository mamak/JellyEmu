package object;

import java.util.Map;
import java.util.TreeMap;

public class Stats {
    protected Map<Type, Integer> _stats = new TreeMap<>();
    
    public static enum Type{
        //stats inconnues (inutiles)
        NULL(-1, -1),

        //types de bases
        VITALITE(125, 153),
        PA(111, 101),
        PM(128, 127),

        //stats de base (éléments)
        FORCE(118, 157),
        INTELLIGENCE(126, 155),
        SAGESSE(124, 156),
        CHANCE(123, 152),
        AGILITE(119, 154),

        //stats secondaires (dépendent des autres)
        PODS(158, 159),
        CC(115, 171),
        PROSPECTION(176, 177),
        SOIN(178, 179),
        INITIATIVE(174, 175),

        ;

        protected int addId;
        protected int rmId;
        protected static Map<Integer, Type> _typesId = new TreeMap<>();

        Type(int addId, int rmId){
            this.addId=addId;
            this.rmId=rmId;
        }

        public int addId(){
            return addId;
        }

        public int rmId(){
            return rmId;
        }

        public boolean isAdd(int id){
            return id==addId;
        }

        public static Type getTypeById(int id){
            if(_typesId.isEmpty()){
                for(Type _type : Type.values()){
                    _typesId.put(_type.addId(), _type);
                    _typesId.put(_type.rmId(), _type);
                }
            }

            if(_typesId.containsKey(id))
                return _typesId.get(id);

            return Type.NULL;
        }
    }

    public void add(Type _type, int qu){
        int total=0;

        if(_stats.containsKey(_type)){
            total=_stats.get(_type);
        }

        total+=qu;
    }

    public void add(int typeId, int qu){
        Type _type = Type.getTypeById(typeId);
        if(_type.isAdd(typeId))
            add(_type, qu);
        else
            add(_type, -qu);
    }

    public int get(Type _type){
        if(!_stats.containsKey(_type))
            return 0;

        return _stats.get(_type);
    }

    public int getInitiative(){
        int init = get(Type.INITIATIVE);

        //ancien calcul d'initiative (un peu modifié)
        init += (get(Type.FORCE) + get(Type.CHANCE) + get(Type.INTELLIGENCE) + get(Type.AGILITE)) / 4;
        init += get(Type.VITALITE) / 4; //permet aux sacri d'avoir de l'initiative, sans être cheaté

        init = init < 0 ? 0 : init;

        return init;
    }
}
