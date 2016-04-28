package app.automats;

import app.structures.Actions;
import app.structures.State;
import app.structures.SymbolType;
import app.structures.TableRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MarkupAutomate extends Automate {

    public List<List<TableRecord>> tables;

    public List<List<TableRecord>> getTables() {
        return tables;
    }

    public enum MarkupState implements State {
        BEGIN,
        FILE,
        BEGIN_COMMENT2,
        COMMENT1,
        COMMENT2,
        COMMENT2_STAR,
        NAME,
        SPACE,
        BRACKET,
        QUOTED,
        BACK_SLASHED,
        VALUE,
        VALUE_END
    }

    public enum Directions {
        MAIN,
        COMMENT1,
        COMMENT2,
        VALUE,
        NAME,
    }

    public MarkupAutomate(){
        tables = new ArrayList<List<TableRecord>>(5);

        List<TableRecord> main = new ArrayList<TableRecord>();
        List<TableRecord> comment1 = new ArrayList<TableRecord>();
        List<TableRecord> comment2 = new ArrayList<TableRecord>();
        List<TableRecord> value = new ArrayList<TableRecord>();
        List<TableRecord> name = new ArrayList<TableRecord>();

        /** MAIN automate */
        //at the start of the file
        main.add(new TableRecord(MarkupState.BEGIN, SymbolType.EOL, null, MarkupState.BEGIN));
        main.add(new TableRecord(MarkupState.BEGIN, SymbolType.SPACE, null, MarkupState.BEGIN));
        //if EOF found in the start then tis is an error
        //if EOF found within a file
        main.add(new TableRecord(MarkupState.FILE, SymbolType.EOF, Actions.COMPLETE, null));

        /** COMMENT with simple grid form */
        //if # was found
        main.add(new TableRecord(MarkupState.BEGIN, SymbolType.GRID, Actions.ADD, MarkupState.COMMENT1));
        main.add(new TableRecord(MarkupState.FILE, SymbolType.GRID, Actions.ADD, MarkupState.COMMENT1));
        for(SymbolType symbolType : SymbolType.values()){
            //any symbol within all symbols
            if(symbolType != SymbolType.EOL && symbolType != SymbolType.EOF){
                comment1.add(new TableRecord(MarkupState.COMMENT1, symbolType, Actions.ADD, MarkupState.COMMENT1));
            }else {
                comment1.add(new TableRecord(MarkupState.COMMENT1, SymbolType.EOL, Actions.COMPLETE, MarkupState.FILE));
                comment1.add(new TableRecord(MarkupState.COMMENT1, SymbolType.EOF, Actions.COMPLETE, MarkupState.FILE));
            }
        }

        /** COMMENT with complicated form */
        //if / and * were found
        main.add(new TableRecord(MarkupState.BEGIN, SymbolType.SLASH, Actions.ADD, MarkupState.BEGIN_COMMENT2));
        main.add(new TableRecord(MarkupState.FILE, SymbolType.SLASH, Actions.ADD, MarkupState.BEGIN_COMMENT2));
        //after / was found go to inner comment2 state
        comment2.add(new TableRecord(MarkupState.BEGIN_COMMENT2, SymbolType.STAR, Actions.ADD, MarkupState.COMMENT2));
        for(SymbolType symbolType : SymbolType.values()){
            if(symbolType == SymbolType.SLASH){
                //when slash is found within comment, then it is recognised as a simple symbol
                comment2.add(new TableRecord(MarkupState.COMMENT2, symbolType, Actions.ADD, MarkupState.COMMENT2));
                //end of the comment block
                comment2.add(new TableRecord(MarkupState.COMMENT2_STAR, symbolType, Actions.VALUE_END, MarkupState.FILE));
            }else if(symbolType != SymbolType.STAR){
                //every symbol within a comment
                comment2.add(new TableRecord(MarkupState.COMMENT2, symbolType, Actions.ADD, MarkupState.COMMENT2));
                //if * was found but after it there is no slash
                comment2.add(new TableRecord(MarkupState.COMMENT2_STAR, symbolType, Actions.ADD, MarkupState.COMMENT2));
            }else {
                comment2.add(new TableRecord(MarkupState.COMMENT2_STAR, symbolType, Actions.ADD, MarkupState.COMMENT2_STAR));
                comment2.add(new TableRecord(MarkupState.COMMENT2, symbolType, Actions.ADD, MarkupState.COMMENT2_STAR));
            }
        }

        /** NAME of section or parameter*/
        //begin of parameter or section name
        main.add(new TableRecord(MarkupState.FILE, SymbolType.CHARACTER, Actions.ADD, MarkupState.NAME));
        main.add(new TableRecord(MarkupState.FILE, SymbolType.T, Actions.ADD, MarkupState.NAME));
        main.add(new TableRecord(MarkupState.FILE, SymbolType.N, Actions.ADD, MarkupState.NAME));
        main.add(new TableRecord(MarkupState.BEGIN, SymbolType.CHARACTER, Actions.ADD, MarkupState.NAME));
        main.add(new TableRecord(MarkupState.BEGIN, SymbolType.T, Actions.ADD, MarkupState.NAME));
        main.add(new TableRecord(MarkupState.BEGIN, SymbolType.N, Actions.ADD, MarkupState.NAME));
        //spaces
        main.add(new TableRecord(MarkupState.FILE, SymbolType.SPACE, Actions.ADD, MarkupState.FILE));
        main.add(new TableRecord(MarkupState.FILE, SymbolType.OTHER_SPACES, Actions.ADD, MarkupState.FILE));
        main.add(new TableRecord(MarkupState.FILE, SymbolType.EOL, Actions.ADD, MarkupState.FILE));
        //name consists from characters, numbers and underlines
        name.add(new TableRecord(MarkupState.NAME, SymbolType.CHARACTER, Actions.ADD, MarkupState.NAME));
        name.add(new TableRecord(MarkupState.NAME, SymbolType.T, Actions.ADD, MarkupState.NAME));
        name.add(new TableRecord(MarkupState.NAME, SymbolType.N, Actions.ADD, MarkupState.NAME));
        name.add(new TableRecord(MarkupState.NAME, SymbolType.NUMBER, Actions.ADD, MarkupState.NAME));
        name.add(new TableRecord(MarkupState.NAME, SymbolType.UNDERLINE, Actions.ADD, MarkupState.NAME));

        //if name is followed by number of spaces
        name.add(new TableRecord(MarkupState.NAME, SymbolType.SPACE, Actions.ADD, MarkupState.SPACE));
        name.add(new TableRecord(MarkupState.NAME, SymbolType.EOL, Actions.ADD, MarkupState.SPACE));
        name.add(new TableRecord(MarkupState.SPACE, SymbolType.SPACE, Actions.ADD, MarkupState.SPACE));
        name.add(new TableRecord(MarkupState.SPACE, SymbolType.EOL, Actions.ADD, MarkupState.SPACE));

        //name can be followed by optional or parameter value
        for(SymbolType symbolType : SymbolType.values()){
            //if name is followed by '=' then it is parameter value, else its optional value
            if(symbolType != SymbolType.ASSIGN && symbolType != SymbolType.EOF
                    && symbolType != SymbolType.OPENED && symbolType != SymbolType.CLOSED
                    && symbolType != SymbolType.QUOTE){
                name.add(new TableRecord(MarkupState.SPACE, symbolType, Actions.ADD, MarkupState.VALUE));
            }else if(symbolType == SymbolType.ASSIGN){
                name.add(new TableRecord(MarkupState.SPACE, symbolType, Actions.ADD, MarkupState.VALUE));
                name.add(new TableRecord(MarkupState.NAME, symbolType, Actions.ADD, MarkupState.VALUE));
            }else if(symbolType == SymbolType.QUOTE){
                name.add(new TableRecord(MarkupState.SPACE, symbolType, Actions.ADD, MarkupState.QUOTED));
            }
        }

        /** Optional value */
        /** Parameter value */
        /** Quoted string*/
        for(SymbolType symbolType : SymbolType.values()){
            if(symbolType != SymbolType.QUOTE && symbolType != SymbolType.SEMICOLON
                    && symbolType != SymbolType.OPENED && symbolType != SymbolType.CLOSED
                    && symbolType != SymbolType.BACK_SLASH){
                value.add(new TableRecord(MarkupState.VALUE, symbolType, Actions.ADD, MarkupState.VALUE));
                value.add(new TableRecord(MarkupState.QUOTED, symbolType, Actions.ADD, MarkupState.QUOTED));
            }else if(symbolType == SymbolType.QUOTE) {
                value.add(new TableRecord(MarkupState.VALUE, symbolType, Actions.ADD, MarkupState.QUOTED));
            }
        }
        //quoted string ends when it gets quote
        value.add(new TableRecord(MarkupState.QUOTED, SymbolType.QUOTE, Actions.ADD, MarkupState.VALUE));

        //some escape-sequencies
        value.add(new TableRecord(MarkupState.QUOTED, SymbolType.BACK_SLASH, Actions.ADD, MarkupState.BACK_SLASHED));
        value.add(new TableRecord(MarkupState.BACK_SLASHED, SymbolType.QUOTE, Actions.ADD, MarkupState.QUOTED));
        value.add(new TableRecord(MarkupState.BACK_SLASHED, SymbolType.BACK_SLASH, Actions.ADD, MarkupState.QUOTED));
        value.add(new TableRecord(MarkupState.BACK_SLASHED, SymbolType.T, Actions.ADD, MarkupState.QUOTED));
        value.add(new TableRecord(MarkupState.BACK_SLASHED, SymbolType.N, Actions.ADD, MarkupState.QUOTED));

        //when semicolon found
        value.add(new TableRecord(MarkupState.VALUE, SymbolType.SEMICOLON, Actions.VALUE_END, MarkupState.FILE));

        /** Opened bracket { */
        name.add(new TableRecord(MarkupState.SPACE, SymbolType.OPENED, Actions.COMPLETE, MarkupState.BRACKET));
        name.add(new TableRecord(MarkupState.NAME, SymbolType.OPENED, Actions.COMPLETE, MarkupState.BRACKET));
        value.add(new TableRecord(MarkupState.VALUE, SymbolType.OPENED, Actions.COMPLETE, MarkupState.BRACKET));

        main.add(new TableRecord(MarkupState.BRACKET, SymbolType.OPENED, Actions.VALUE_END, MarkupState.FILE));
        main.add(new TableRecord(MarkupState.BRACKET, SymbolType.CLOSED, Actions.VALUE_END, MarkupState.FILE));
        main.add(new TableRecord(MarkupState.FILE, SymbolType.OPENED, Actions.VALUE_END, MarkupState.FILE));
        main.add(new TableRecord(MarkupState.FILE, SymbolType.CLOSED, Actions.COMPLETE, MarkupState.BRACKET));
        /** Section  */


        //add all this tables on their places
        tables.add(main);
        tables.add(comment1);
        tables.add(comment2);
        tables.add(value);
        tables.add(name);
    }


}