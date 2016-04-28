package app.structures;

public class TableRecord {
    State inputSate;
    SymbolType symbol;
    Actions action;
    State outputState;

    public TableRecord(State inputState, SymbolType inputSymbol, Actions action, State outputState){
        this.symbol = inputSymbol;
        this.inputSate = inputState;
        this.action = action;
        this.outputState = outputState;
    }

    public SymbolType getSymbol(){
        return symbol;
    }

    public Actions getAction(){
        return action;
    }

    public State getOutputState(){
        return outputState;
    }

    public State getInputSate(){
        return inputSate;
    }

    public boolean equals(Object record){
        return this.symbol == ((TableRecord) record).getSymbol()
                && this.inputSate == ((TableRecord) record).getInputSate();
    }
}
