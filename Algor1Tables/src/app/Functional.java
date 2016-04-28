package app;

public class Functional implements Cloneable{
    double value;

    public Functional(){
    }

    public void setValue(double value){
        this.value = value;
    }

    public double getValue(){
        return value;
    }

    @Override
    public Functional clone(){
        Functional functional = new Functional();
        functional.setValue(this.value);
        return functional;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Functional){
            if(this.value == ((Functional)o).getValue()){
                return true;
            }
        }
        return false;
    }
}
