package app;

import app.automats.Automate;
import app.automats.EmailAutomate;
import app.structures.*;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ParserEmail extends Parser{

    protected Automate automate;

    public ParserEmail(String fileName) throws FileNotFoundException {
        super(fileName);
        automate = new EmailAutomate();
        inputState = EmailAutomate.EmailState.BEGIN;
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
        String answer;
        boolean success = true;
        while (success) {
            System.out.println("Do you want to continue?(y/n)");
            //point 2
            answer = in.readLine();
            if (answer.equals("y")) {
                System.out.println(getNextConstruction());
                success = true;
            } else success = false;
        }
        i = 0;
        in.close();
    }

    public String getNextConstruction() throws ParseException {
        StringBuilder str = new StringBuilder("");

        /** input state which will be used in automate */
        State inputState = this.inputState;
        for(; i < encrypted.size(); i++) {
            SymbolType symbol = encrypted.get(i);

            /** get next symbol and current state */
            TableRecord input = new TableRecord(inputState, symbol, null, null);
            Actions action;

            /** look through all automate to find similar input data */
            if(!automate.table.contains(input)){
                throw new ParseException(String.format("Parse Exception symbol#%d", i), 0);
            }
            for (TableRecord tableRecord : automate.table) {
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
                    }
                }
            }
        }
        return str.toString();
    }
}
