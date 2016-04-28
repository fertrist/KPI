package app;

import app.automats.Automate;
import app.structures.State;
import app.structures.SymbolType;
import org.testng.annotations.AfterClass;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Parser {

    protected FileInputStream file;
    protected List<SymbolType> encrypted = null;
    protected List<Character> characters = null;
    protected State inputState;
    public static String BASE_LOCATION = "src/";

    public Parser(String fileName) throws FileNotFoundException {
        file = new FileInputStream(BASE_LOCATION + fileName);
        classify();
    }

    @AfterClass
    public void tearDown() throws IOException {
        if(file!= null) file.close();
    }

    public void classify(){
        encrypted = new ArrayList<SymbolType>();
        characters = new ArrayList<Character>();
        BufferedInputStream in = null;
        try{
            in = new BufferedInputStream(file);
            int next = 0;
            while(next != -1){
                next = in.read();
                if(next >= 'a' && next < 'n' ||
                        next > 'n' && next < 't' ||
                        next > 't' && next <= 'z' ||
                        next >= 'A' && next <= 'Z'){
                    encrypted.add(SymbolType.CHARACTER);
                }else  if(next == 'n'){
                    encrypted.add(SymbolType.N);
                }else if(next == 't'){
                    encrypted.add(SymbolType.T);
                }else if(next == -1){
                    encrypted.add(SymbolType.EOF);
                }else if(next == '/'){
                    encrypted.add(SymbolType.SLASH);
                }else if(next == '*'){
                    encrypted.add(SymbolType.STAR);
                }else if(next == '@'){
                    encrypted.add(SymbolType.AT);
                }else if(next == '\n'){
                    encrypted.add(SymbolType.EOL);
                }else if(next == '\"'){
                    encrypted.add(SymbolType.QUOTE);
                }else if(next == '_'){
                    encrypted.add(SymbolType.UNDERLINE);
                }else if(next == ' '){
                    encrypted.add(SymbolType.SPACE);
                }else if(next == '\t'){
                    encrypted.add(SymbolType.OTHER_SPACES);
                }else if(next == '-'){
                    encrypted.add(SymbolType.MINUS);
                }else if(next >= '0' && next <= '9'){
                    encrypted.add(SymbolType.NUMBER);
                }else if(next == '='){
                    encrypted.add(SymbolType.ASSIGN);
                }else if(next == '.'){
                    encrypted.add(SymbolType.POINT);
                }else if(next == '#'){
                    encrypted.add(SymbolType.GRID);
                }else if(next == '\\'){
                    encrypted.add(SymbolType.BACK_SLASH);
                }else if(next == ';'){
                    encrypted.add(SymbolType.SEMICOLON);
                }else if(next == '{'){
                    encrypted.add(SymbolType.OPENED);
                }else if(next == '}'){
                    encrypted.add(SymbolType.CLOSED);
                }else {
                    encrypted.add(SymbolType.OTHER);
                }
                characters.add((char) next);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
