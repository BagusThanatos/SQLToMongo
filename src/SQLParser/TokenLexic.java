/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLParser;

/**
 *
 * @author BagusThanatos
 */
public class TokenLexic {
    /*
    class yang nantinya mengandung hasil parsing
    */
    private int tokenCode;
    private String string;
    private String value;
    
    public TokenLexic(int tc, String s, String v){
        this.tokenCode=tc;
//        if (!s.equals("")) this.string=s;
//        else if (tc<=Parser.KEYWORDS) this.string="Keyword";
//        else if(tc<=Parser.BOOLEANS) this.string="Boolean Operator";
//        else if(tc<=Parser.LOGIC_OPERATORS) this.string="Logic Operator";
//        else if (tc<=Parser.SET_OPERATOR) this.string="Set Operator";
//        else if (tc==Parser.CONSTANT_NUMBER) this.string="Constant Number";
//        else if (tc== Parser.CONSTANT_STRING) this.string="Constant String";
//        else if (tc==Parser.VARIABLE) this.string="Variable";
//        else if (tc==Parser.UNIDENTIFIED) this.string="Unidentified";
        
        this.value=v.replace("\"", "");
    }

    public int getTokenCode(){
        return this.tokenCode;
    }
    public String getName(){
        return this.string;
    }
    public String getValue(){
        return this.value;
    }
    @Override
    public boolean equals(Object o){
        if (o instanceof String){
            return o.equals(value.toUpperCase());
        }
        return super.equals(o);
    }
    @Override
    public String toString(){
        return value;
    }
}
