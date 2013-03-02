package object;

public abstract class CharacterClass {
    protected int level;
    protected byte sexe;
    protected byte type;
    protected Stats _stats = new Stats();

    public abstract String className();

    public CharacterClass(int level, byte sexe){
        this.level=level;
        this.sexe=sexe;
        _stats.add(Stats.Type.PA, 6);
        _stats.add(Stats.Type.PM, 3);
        _stats.add(Stats.Type.VITALITE, 50);
        _stats.add(Stats.Type.PROSPECTION, 100);
        updateStatsByLevel(level);
    }

    protected void updateStatsByLevel(int dlvl){
        //ajoute 10pdv par lvl
        _stats.add(Stats.Type.VITALITE, dlvl*10);

        if(level>=100)
            _stats.add(Stats.Type.PA, 1);
    }

    public void update(int level){
        int dlvl=level-this.level;
        this.level=level;
        updateStatsByLevel(dlvl);
    }
    
    public Stats getBaseStats(){
        return _stats;
    }

    public int getDefaultGfxid(){
        return Integer.parseInt(type+""+sexe);
    }

    /**
     * Crée la classe en fonction de son id
     * @param type
     * @param level
     * @return
     */
    public static CharacterClass getClass(byte type, int level, byte sexe){
        switch(type){
            //féca
            case 1:
                return new Feca(level, sexe);
            //osamodas
            case 2:
                return new Osamodas(level, sexe);
            //énutrof
            case 3:
                return new Enutrof(level, sexe);
            //sram
            case 4:
                return new Sram(level, sexe);
            //xelor
            case 5:
                return new Xelor(level, sexe);
            //ecaflip
            case 6:
                return new Ecaflip(level, sexe);
            //enirispa
            case 7:
                return new Eniripsa(level, sexe);
            //iop
            case 8:
                return new Iop(level, sexe);
            //crâ
            case 9:
                return new Cra(level, sexe);
            //sadida
            case 10:
                return new Sadida(level, sexe);
            //sacrieur
            case 11:
                return new Sacrieur(level, sexe);
            //pandawa
            case 12:
                return new Pandawa(level, sexe);
        }

        return null;
    }

    protected static class Feca extends CharacterClass{
        public Feca(int level, byte sexe){
            super(level, sexe);
            type=1;      
        }

        @Override
        public String className(){
            return "Féca";
        }
    }

    protected static class Osamodas extends CharacterClass{
        public Osamodas(int level, byte sexe){
            super(level, sexe);
            type=2;
        }

        @Override
        public String className(){
            return "Osamodas";
        }
    }

    protected static class Enutrof extends CharacterClass{
        public Enutrof(int level, byte sexe){
            super(level, sexe);
            type=3;

            _stats.add(Stats.Type.PROSPECTION, 20);
        }

        @Override
        public String className(){
            return "Enutrof";
        }
    }

    protected static class Sram extends CharacterClass{
        public Sram(int level, byte sexe){
            super(level, sexe);
            type=4;
        }

        @Override
        public String className(){
            return "Sram";
        }
    }

    protected static class Xelor extends CharacterClass{
        public Xelor(int level, byte sexe){
            super(level, sexe);
            type=5;
        }

        @Override
        public String className(){
            return "Xelor";
        }
    }

    protected static class Ecaflip extends CharacterClass{
        public Ecaflip(int level, byte sexe){
            super(level, sexe);
            type=6;
        }

        @Override
        public String className(){
            return "Ecaflip";
        }
    }

    protected static class Eniripsa extends CharacterClass{
        public Eniripsa(int level, byte sexe){
            super(level, sexe);
            type=7;
        }

        @Override
        public String className(){
            return "Eniripsa";
        }
    }

    protected static class Iop extends CharacterClass{
        public Iop(int level, byte sexe){
            super(level, sexe);
            type=8;
        }

        @Override
        public String className(){
            return "Iop";
        }
    }

    protected static class Cra extends CharacterClass{
        public Cra(int level, byte sexe){
            super(level, sexe);
            type=9;
        }

        @Override
        public String className(){
            return "Crâ";
        }
    }

    protected static class Sadida extends CharacterClass{
        public Sadida(int level, byte sexe){
            super(level, sexe);
            type=10;
        }

        @Override
        public String className(){
            return "Sadida";
        }
    }

    protected static class Sacrieur extends CharacterClass{
        public Sacrieur(int level, byte sexe){
            super(level, sexe);
            type=11;
        }

        @Override
        public String className(){
            return "Sacrieur";
        }

        @Override
        protected void updateStatsByLevel(int dlvl){
            _stats.add(Stats.Type.VITALITE, dlvl*20);

            if(level>=100)
                _stats.add(Stats.Type.PA, 1);
        }
    }

    protected static class Pandawa extends CharacterClass{
        public Pandawa(int level, byte sexe){
            super(level, sexe);
            type=12;
        }

        @Override
        public String className(){
            return "Pandawa";
        }
    }
}
