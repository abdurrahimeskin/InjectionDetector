public class InjectionDetector {
    // 0 = initial / normal
    // 1 = in single quote
    // 2 = in double quote
    // 3 = already saw a semicolon
    // 4 = saw a semicolon + more (possible sql injection)
    private int tokenizerState = 0;
    private String sqlStatement;

    public InjectionDetector(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public boolean probablyHasInjection() {
        for(int i=0; i<sqlStatement.length(); i++)
            feed(sqlStatement.charAt(i));

        return (tokenizerState == 4);
    }

    private void feed(char ch) {
        switch(tokenizerState) {
            case 0:
                if(ch == '\'') tokenizerState = 1;
                else if(ch == '"') tokenizerState = 2;
                else if(ch == ';') tokenizerState = 3;
                break;

            case 1:
                if(ch == '\'') tokenizerState = 0;
                break;

            case 2:
                if(ch == '"') tokenizerState = 0;
                break;

            case 3:
                if(!Character.isWhitespace(ch)) tokenizerState = 4;
                break;

            case 4:
                break;
        }
    }

}
