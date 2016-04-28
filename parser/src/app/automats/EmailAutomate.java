package app.automats;

import app.structures.Actions;
import app.structures.State;
import app.structures.SymbolType;
import app.structures.TableRecord;

import java.util.ArrayList;

public class EmailAutomate extends Automate {

    public enum EmailState implements State {
        BEGIN,
        NAME,
        DOMAIN_START,
        DOMAIN,
        AFTER_MINUS,
        FULL_DOMAIN,
        FULL_MINUS
    }

    public EmailAutomate(){
        table = new ArrayList<TableRecord>();
        table.add(new TableRecord(EmailState.BEGIN, SymbolType.CHARACTER, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.BEGIN, SymbolType.T, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.BEGIN, SymbolType.N, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.BEGIN, SymbolType.NUMBER, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.BEGIN, SymbolType.UNDERLINE, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.BEGIN, SymbolType.SPACE, null, EmailState.BEGIN));
        table.add(new TableRecord(EmailState.BEGIN, SymbolType.EOL, null, EmailState.BEGIN));

        table.add(new TableRecord(EmailState.NAME, SymbolType.CHARACTER, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.NAME, SymbolType.T, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.NAME, SymbolType.N, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.NAME, SymbolType.NUMBER, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.NAME, SymbolType.UNDERLINE, Actions.ADD, EmailState.NAME));
        table.add(new TableRecord(EmailState.NAME, SymbolType.AT, Actions.ADD, EmailState.DOMAIN_START));

        table.add(new TableRecord(EmailState.DOMAIN_START, SymbolType.CHARACTER, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.DOMAIN_START, SymbolType.N, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.DOMAIN_START, SymbolType.T, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.DOMAIN_START, SymbolType.NUMBER, Actions.ADD, EmailState.DOMAIN));

        table.add(new TableRecord(EmailState.DOMAIN, SymbolType.CHARACTER, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.DOMAIN, SymbolType.T, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.DOMAIN, SymbolType.N, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.DOMAIN, SymbolType.NUMBER, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.DOMAIN, SymbolType.POINT, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.DOMAIN, SymbolType.MINUS, Actions.ADD, EmailState.AFTER_MINUS));

        table.add(new TableRecord(EmailState.AFTER_MINUS, SymbolType.CHARACTER, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.AFTER_MINUS, SymbolType.T, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.AFTER_MINUS, SymbolType.N, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.AFTER_MINUS, SymbolType.NUMBER, Actions.ADD, EmailState.DOMAIN));
        table.add(new TableRecord(EmailState.AFTER_MINUS, SymbolType.MINUS, Actions.ADD, EmailState.AFTER_MINUS));

        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.CHARACTER, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.T, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.N, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.NUMBER, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.POINT, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.MINUS, Actions.ADD, EmailState.FULL_MINUS));
        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.SPACE, Actions.COMPLETE, null));
        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.EOL, Actions.COMPLETE, null));
        table.add(new TableRecord(EmailState.FULL_DOMAIN, SymbolType.EOF, Actions.COMPLETE, null));

        table.add(new TableRecord(EmailState.FULL_MINUS, SymbolType.CHARACTER, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_MINUS, SymbolType.T, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_MINUS, SymbolType.N, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_MINUS, SymbolType.NUMBER, Actions.ADD, EmailState.FULL_DOMAIN));
        table.add(new TableRecord(EmailState.FULL_MINUS, SymbolType.MINUS, Actions.ADD, EmailState.FULL_MINUS));

    }

}
