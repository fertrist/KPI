package app;

public class Key implements Cloneable, Comparable<Key>{
    private String str;
    int mod;

    public Key(){ }

    public Key(int value){
        this.setStr("name_" + (int) value);
        this.setMod((int) Math.abs((int) System.currentTimeMillis()) + this.hashCode());
    }

    public String getStr(){
        return str;
    }

    public void setStr(String str){
        this.str = str;
    }

    public void setMod(int mod){
        this.mod = mod;
    }

    public int getMod(){
        return mod;
    }

    @Override
    public Key clone(){
        Key key = new Key();
        key.setStr(this.str);
        key.setMod(this.mod);
        return key;
    }

    @Override
    public int compareTo(Key k){
        if(k == null) return 1;
        int result = this.str.compareTo(k.getStr());
        if(result == 0){ result = this.mod - k.getMod(); }
        return result;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Key){
            if(this.compareTo((Key)o) == 0) return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "str=" + this.str + "; " + "mod=" + mod;
    }

    public boolean isSimilar(Key key){
        //count equal symbols
        return SimilarityUtil.isSimilar(this.str.toCharArray(),
                key.getStr().toCharArray());
    }
}
