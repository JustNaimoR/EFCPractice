package demo.enums;

/**
 * Возможные разделители данных в csv файле
 */
public enum Delimiter {
    COMMA(','), SEMICOLON(';'), SPACE(' '), TAB('\t');

    private char delimiter;

    Delimiter(char delimiter) {
        this.delimiter = delimiter;
    }
    
    @Override
    public String toString() {
        return Character.toString(delimiter);
    }
}