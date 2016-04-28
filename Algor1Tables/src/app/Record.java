package app;

public class Record implements Cloneable, Comparable<Record>{

    private Key key;
    private Functional functional;
    private boolean isRemoved = false;

    public Record(){
    }

    public Record(double value){
        key = new Key();
        key.setStr("name_" + (int) value);
        key.setMod((int) Math.abs((int)System.currentTimeMillis()) + this.hashCode());
        functional = new Functional();
        functional.setValue(value);
    }

    public void delete(){
        this.isRemoved = true;
    }

    public boolean isRemoved(){
        return isRemoved;
    }

    @Override
    public Record clone(){
        Record instance = new Record();
        instance.setKey(this.key);
        instance.setFunctional(this.functional);
        return instance;
    }

    @Override
    public int compareTo(Record o) {
        return this.key.compareTo(o.getKey());
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Record){
            if(this.compareTo((Record)o) == 0){
                return this.functional.equals(((Record) o).getFunctional());
            }
        }else{
            return false;
        }
        return false;
    }

    public void setKey(Key key){
        this.key = key.clone();
    }

    public Key getKey(){
        return key;
    }

    public void setFunctional(Functional functional){
        this.functional = functional.clone();
    }

    public Functional getFunctional(){
        return functional;
    }

    @Override
    public String toString(){
        return String.format("[Key: {%s}, Value: {%s}]", key, functional.getValue());
    }

    public boolean isSimilar(Record record){
        return this.key.isSimilar(record.getKey());
    }
}

