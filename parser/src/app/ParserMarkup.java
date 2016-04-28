package app;

import app.automats.MarkupAutomate;
import app.structures.Actions;
import app.structures.SymbolType;
import app.structures.TableRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ParserMarkup extends Parser{

    private MarkupAutomate automate;
    List<String> parsed;
    int opened = 0;
    int closed = 0;

    public ParserMarkup(String fileName) throws FileNotFoundException {
        super(fileName);
        automate = new MarkupAutomate();
        inputState = MarkupAutomate.MarkupState.BEGIN;
        parsed = new ArrayList<String>();
    }

    int i = 0;

    public List<String> parse() throws ParseException {
        List<String> parsed = new ArrayList<String>();
        while(i < encrypted.size()){
            String construction = getNextConstruction();
            parsed.add(construction);
        }
        return parsed;
    }

    public void parseOneByOne() throws IOException, ParseException {
        BufferedReader in;
        in = new BufferedReader(new InputStreamReader(System.in));
        String answer = "y";
        boolean ask = true;
        String construction;
        while (true) {
            if (ask) {
                System.out.println("Do you want to continue?(y/n)");
                //answer = in.readLine();
                if (answer.equals("y")) {
                    construction = getNextConstruction();
                    if (construction != null) {
                        parsed.add(construction);
                        System.out.println(construction);
                    } else {
                        break;
                    }
                } else {
                    ask = false;
                }
            } else {
                construction = getNextConstruction();
                parsed.add(construction);
                System.out.println(construction);
            }
        }
        if(opened != closed) throw new ParseException(
                String.format("Not odd brackets: opened=%d closed=%d", opened, closed), 0);
        i = 0;
        printWholeFile();
        in.close();
    }

    private void printWholeFile(){
        for(String construction : parsed){
            System.out.print(construction);
        }
    }

    public String getNextConstruction() throws ParseException {
        StringBuilder str = new StringBuilder("");

        /** input state which will be used in automate */
        List<TableRecord> table = null;
        for(int s = 1; i < encrypted.size(); s++, i++) {
            if(inputState == MarkupAutomate.MarkupState.BRACKET){
                i--;
            }
            SymbolType symbol = encrypted.get(i);

            /** get next symbol and current state */
            //when */ sequence is found
            TableRecord input = new TableRecord(inputState, symbol, null, null);
            Actions action;

            /** count number of opened/closed brackets*/
            if(inputState == MarkupAutomate.MarkupState.BRACKET){
                if(symbol == SymbolType.OPENED){
                    opened++;
                }else if(symbol == SymbolType.CLOSED){
                    closed++;
                }
            }

            /**retrieve automate according to current state */
            if(inputState == MarkupAutomate.MarkupState.BEGIN ||
                    inputState == MarkupAutomate.MarkupState.FILE ||
                    inputState == MarkupAutomate.MarkupState.BRACKET){
                table = automate.getTables().get(MarkupAutomate.Directions.MAIN.ordinal());
            }else if(inputState == MarkupAutomate.MarkupState.COMMENT1) {
                table = automate.getTables().get(MarkupAutomate.Directions.COMMENT1.ordinal());
            }else if(inputState == MarkupAutomate.MarkupState.COMMENT2 ||
                    inputState == MarkupAutomate.MarkupState.BEGIN_COMMENT2 ||
                    inputState == MarkupAutomate.MarkupState.COMMENT2_STAR) {
                table = automate.getTables().get(MarkupAutomate.Directions.COMMENT2.ordinal());
            }else if(inputState == MarkupAutomate.MarkupState.NAME ){
                table = automate.getTables().get(MarkupAutomate.Directions.NAME.ordinal());
            }else if(inputState == MarkupAutomate.MarkupState.VALUE ||
                    inputState == MarkupAutomate.MarkupState.QUOTED){
                table = automate.getTables().get(MarkupAutomate.Directions.VALUE.ordinal());
            }
            /** look through all automate to find similar input data */
            assert table != null;
            if(!table.contains(input)){
                throw new ParseException(String.format("Parse Exception symbol#%d", s), 0);
            }
            for (TableRecord tableRecord : table) {
                if (tableRecord.equals(input)) {
                    inputState = tableRecord.getOutputState();
                    action = tableRecord.getAction();
                    if(action == Actions.ADD){
                        str.append(characters.get(i));
                        break;
                    }else if(action == Actions.ERROR){
                        throw new ParseException("Something is wrong in file", i);
                    }else if(action == Actions.COMPLETE){
                        i++;
                        return str.toString();
                    }else if(action == Actions.VALUE_END){
                        str.append(characters.get(i));
                        i++;
                        return str.toString();
                    }
                }
            }
        }
        return null;
    }

}
