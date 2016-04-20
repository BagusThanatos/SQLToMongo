/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLParser;

import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public class Parser {
    private ArrayList<TokenLexic> tokens;
    private final Stack<String> stack=new Stack();
    protected final static ArrayList<String> LEXICALNAME= new ArrayList();
   // protected final static ArrayList<Integer> LEXICALCODE= new ArrayList();
    static {
        LEXICALNAME.add("SELECT");  //1
        LEXICALNAME.add("*");       //2
        LEXICALNAME.add("WHERE");   //3
        LEXICALNAME.add("FROM");    //4
        LEXICALNAME.add("(");       //5
        LEXICALNAME.add(")");       //6
        LEXICALNAME.add(".");       //7
        LEXICALNAME.add(",");       //8
        LEXICALNAME.add(";");       //9
        LEXICALNAME.add("IN");      //10
        LEXICALNAME.add("AND");     //11
        LEXICALNAME.add("OR");      //12
        LEXICALNAME.add("NOT");     //13
        LEXICALNAME.add(">=");      //14
        LEXICALNAME.add("=");       //15
        LEXICALNAME.add("<=");      //16
        LEXICALNAME.add("<");       //17
        LEXICALNAME.add(">");       //18
        LEXICALNAME.add("LIKE");    //19
        LEXICALNAME.add("UNION");   //20
        LEXICALNAME.add("JOIN");    //21
        LEXICALNAME.add("UPDATE");  //
        LEXICALNAME.add("DELETE");
        LEXICALNAME.add("INSERT");
        LEXICALNAME.add("INTO");
        LEXICALNAME.add("VALUES");
        LEXICALNAME.add("ALL");
        LEXICALNAME.add("SET");
        LEXICALNAME.add("LIKE");
        LEXICALNAME.add("BETWEEN");
        LEXICALNAME.add("ORDER");
        LEXICALNAME.add("BY");
        LEXICALNAME.add("ASC");
        LEXICALNAME.add("DESC");
        LEXICALNAME.add("DISTINCT");
        LEXICALNAME.add("LEFT");
        LEXICALNAME.add("OUTER");
        LEXICALNAME.add("JOIN");
        LEXICALNAME.add("ON");
        LEXICALNAME.add("USING");
        //for (int i=1;i<=21;i++) LEXICALCODE.add(i);
    }
//    public final static int START=0;
//    public final static int SELECT=1;
//    public final static int STAR=2;
//    public final static int WHERE=3;
//    public final static int FROM=4;
//    public final static int KURKA=5;
//    public final static int KURTUP=6;
//    public final static int PERIOD=7;
//    public final static int COMMA=8;
//    public final static int SEMICOLON=9;
//    public final static int IN=10;
//    public final static int AND=11;
//    public final static int OR=12;
//    public final static int NOT=13;
//    public final static int GREATER_EQUAL=14;
//    public final static int EQUAL=15;
//    public final static int LESS_EQUAL=16;
//    public final static int LESS=17;
//    public final static int GREATER=18;
//    public final static int LIKE=19;
//    public final static int UNION=20;
//    public final static int JOIN=21;
      public final static int VARIABLE=96;
      public final static int CONSTANT_STRING=97;
      public final static int CONSTANT_NUMBER=98;
//    public final static int INSERT = 26;
//    public final static int DELETE = 27;
//    public final static int UPDATE = 28;
//    public final static int SET = 29;
//    public final static int VALUES = 30;
//    public final static int ALL = 31;
//    public final static int INTO = 30;
//    
      public final static int UNIDENTIFIED=99;
//    public final static int KEYWORDS=10;
//    public final static int BOOLEANS=12;
//    public final static int LOGIC_OPERATORS=18;
//    public final static int SET_OPERATOR=20;
//    
    
    private static final Parser SQLPARSER = makeSQLParser();
    ArrayList<Node> nodes ;
    Node currentNode ;
    JunkNode jN = new JunkNode(this);
    Node initialNode;
    private Parser(){
        
    }
    void setNode(Node n){
        this.currentNode = n;
    }
    private void setInitialNode(Node initial){
        this.initialNode=initial;
    }
    private void init(){
        this.currentNode=initialNode;
        this.stack.clear();
    }
    Node getJunkNode(){
        return jN;
    }
    void addNode(Node n){
        this.nodes.add(n);
    }
    
    public static Parser getSQLParser(){
        return SQLPARSER;
    }
    public String stackPeek(){
      return stack.empty()? null : this.stack.peek();
    }
    public void stackPush(String push){
      this.stack.push(push);
    }
    public void popStack(){
      this.stack.pop();
    }
    private static Parser makeSQLParser(){
        Parser p= new Parser();
        
        NormalNode initial = new NormalNode(p,false);
        NormalNode select = new NormalNode(p, false);
        NormalNode insert = new NormalNode(p,false);
        NormalNode delete = new NormalNode(p,false);
        NormalNode update = new NormalNode(p,false);
        NormalNode all = new NormalNode(p, false);
        NormalNode select_Expr = new NormalNode(p,false);
        NormalNode from = new NormalNode(p,false);
        NormalNode table = new NormalNode(p,false);
        NormalNode semicolon = new NormalNode(p,true);
        NormalNode where = new NormalNode(p,false);
        NormalNode into = new NormalNode(p,false);
        NormalNode commaSelect = new NormalNode(p,false);
        NormalNode fromDelete = new NormalNode(p,false);
        NormalNode tableDelete = new NormalNode(p,false);
        NormalNode tableUpdate = new NormalNode(p,false);
        NormalNode set = new NormalNode(p,false);
        NormalNode kolomUpdate = new NormalNode(p,false);
        NormalNode eqUpdate = new NormalNode(p,false);
        NormalNode valuesUpdate = new NormalNode(p,false);
        NormalNode tableInsert = new NormalNode(p,false);
        NormalNode kurkaInsert = new NormalNode(p,false);
        NormalNode kolomInsert = new NormalNode(p,false);
        NormalNode kurtubInsert = new NormalNode(p,false);
        NormalNode valuesInsert = new NormalNode(p,false);
        NormalNode kurka2Insert = new NormalNode(p,false);
        NormalNode kurtub2Insert= new NormalNode(p,false);
        NormalNode stringAngkaInsert = new NormalNode(p,false);
        NormalNode angkaWhere = new NormalNode(p,false);
        NormalNode operator = new NormalNode(p,false);
        NormalNode operatorKolom = new NormalNode(p,false);
        NormalNode angkaKolom = new NormalNode(p,false);
        NormalNode angkaKolomWhere = new NormalNode(p,false);
        NormalNode stringWhere = new NormalNode(p,false);
        NormalNode eqWhere = new NormalNode(p,false);
        NormalNode eq2Where = new NormalNode(p,false);
        NormalNode string2Where = new NormalNode(p,false);
        NormalNode stringKolom = new NormalNode(p,false);
        NormalNode kolom = new NormalNode(p,false);
        NormalNode like = new NormalNode(p,false);
        NormalNode stringLike = new NormalNode(p,false);
        NormalNode between = new NormalNode(p,false);
        NormalNode num1Between = new NormalNode(p,false);
        NormalNode andBetween = new NormalNode(p,false);
        NormalNode num2Between = new NormalNode(p,false);
        NormalNode order = new NormalNode(p,false);
        NormalNode by = new NormalNode(p,false);
        NormalNode kolomOrder = new NormalNode(p,false);
        NormalNode asc = new NormalNode(p,false);
        NormalNode desc = new NormalNode(p,false);
        NormalNode distinct = new NormalNode(p,false);
        NormalNode kolomDistinct = new NormalNode(p,false);
        NormalNode left = new NormalNode(p,false);
        NormalNode outer = new NormalNode(p, false);
        NormalNode join = new NormalNode(p, false);
        NormalNode onUsing = new NormalNode(p, false);
        NormalNode kolomTableJoin = new NormalNode(p, false);
        NormalNode tableJoin = new NormalNode(p, false);
        NormalNode dot1Join = new NormalNode(p,false);
        NormalNode kolom1join = new NormalNode(p,false);
        NormalNode eqJoin = new NormalNode(p,false);
        NormalNode table2Join = new NormalNode(p,false);
        NormalNode dot2Join = new NormalNode(p,false);
        NormalNode kolom2Join = new NormalNode(p, false);
        
        
        initial.addNext(LEXICALNAME.indexOf("SELECT"), select, SQLKeywords.SELECT, null);
        initial.addNext(LEXICALNAME.indexOf("INSERT"), insert, null, null);
        initial.addNext(LEXICALNAME.indexOf("DELETE"), delete, null, null);
        initial.addNext(LEXICALNAME.indexOf("UPDATE"), update, null, null);
        p.setInitialNode(initial);
        
        select.addNext(LEXICALNAME.indexOf("ALL"), all, null, null);
        select.addNext(LEXICALNAME.indexOf("*"), all, null, null);
        select.addNext(VARIABLE, select_Expr, null, null);
        select.addNext(LEXICALNAME.indexOf("DISTINCT"),distinct, null, SQLKeywords.SELECT);
        
        distinct.addNext(VARIABLE, kolomDistinct, null,null);
        
        kolomDistinct.addNext(LEXICALNAME.indexOf(SQLKeywords.FROM), from, null,null);
        
        all.addNext(LEXICALNAME.indexOf(SQLKeywords.FROM), from, null, null);
        
        from.addNext(VARIABLE, table, null, null);

        select_Expr.addNext(LEXICALNAME.indexOf(SQLKeywords.COMMA), commaSelect, null, null);
        select_Expr.addNext(LEXICALNAME.indexOf(SQLKeywords.FROM), from, null, null);
        
        commaSelect.addNext(VARIABLE, select_Expr, null, null);
        
        table.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        table.addNext(LEXICALNAME.indexOf(SQLKeywords.WHERE), where, null, null);
        table.addNext(LEXICALNAME.indexOf("LEFT"),left, null,null);
        
        left.addNext(LEXICALNAME.indexOf("OUTER"),outer, null, null);
        
        outer.addNext(LEXICALNAME.indexOf("JOIN"), join, null,null);
        
        join.addNext(VARIABLE, tableJoin, null, null);
        
        tableJoin.addNext(LEXICALNAME.indexOf("USING"), onUsing, null,null);
        tableJoin.addNext(LEXICALNAME.indexOf("ON"), onUsing, null,null);
        
        onUsing.addNext(VARIABLE, kolomTableJoin, null,null);
        
        kolomTableJoin.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        kolomTableJoin.addNext(LEXICALNAME.indexOf(SQLKeywords.WHERE), where, null, null);
        kolomTableJoin.addNext(LEXICALNAME.indexOf("."), dot1Join, null, null);
        
        dot1Join.addNext(VARIABLE, kolom1join, null, null);
        
        kolom1join.addNext(LEXICALNAME.indexOf("="),eqJoin,null , null);
        
        eqJoin.addNext(VARIABLE, table2Join, null, null);
        
        table2Join.addNext(LEXICALNAME.indexOf("."), dot2Join, null , null);
        
        dot2Join.addNext(VARIABLE, kolom2Join, null, null);
        
        kolom2Join.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        kolom2Join.addNext(LEXICALNAME.indexOf(SQLKeywords.WHERE), where, null, null);
        
        where.addNext(CONSTANT_NUMBER, angkaWhere, null, null);
        where.addNext(CONSTANT_STRING, stringWhere, null, null);
        where.addNext(VARIABLE, kolom, null, null);
        
        angkaWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.EQ), operator, null, null);
        angkaWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.GT), operator, null, null);
        angkaWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.GTE), operator, null, null);
        angkaWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.LT), operator, null, null);
        angkaWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.LTE), operator, null, null);
        
//        operator.addNext(CONSTANT_NUMBER, angkaKolomWhere, null, null);
        operator.addNext(VARIABLE, angkaKolomWhere, null, null);
        
        angkaKolomWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        angkaKolomWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, SQLKeywords.SELECT);
        angkaKolomWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.AND), where, null, null);
        angkaKolomWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.OR), where, null, null);
        angkaKolomWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.ORDER), order, null, SQLKeywords.SELECT);
        
        stringWhere.addNext(LEXICALNAME.indexOf(SQLKeywords.EQ), eqWhere, null, null);
        
        eqWhere.addNext(VARIABLE, stringKolom, null, null);
//        eqWhere.addNext(CONSTANT_STRING, stringKolom, null, null);
        
        stringKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        stringKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, SQLKeywords.SELECT);
        stringKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.ORDER), order, null, SQLKeywords.SELECT);
        stringKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.AND), where, null, null);
        stringKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.OR), where, null, null);
        
        kolom.addNext(LEXICALNAME.indexOf(SQLKeywords.EQ), eq2Where, null, null);
        kolom.addNext(LEXICALNAME.indexOf(SQLKeywords.LIKE), like, null, null);
        kolom.addNext(LEXICALNAME.indexOf(SQLKeywords.BETWEEN), between, null, null);
        kolom.addNext(LEXICALNAME.indexOf(SQLKeywords.GT), operatorKolom, null, null);
        kolom.addNext(LEXICALNAME.indexOf(SQLKeywords.GTE), operatorKolom, null, null);
        kolom.addNext(LEXICALNAME.indexOf(SQLKeywords.LT), operatorKolom, null, null);
        kolom.addNext(LEXICALNAME.indexOf(SQLKeywords.LTE), operatorKolom, null, null);
        
        operatorKolom.addNext(CONSTANT_NUMBER, angkaKolom, null, null);
        
        angkaKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        angkaKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, SQLKeywords.SELECT);
        angkaKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.ORDER), order, null, SQLKeywords.SELECT);
        angkaKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.AND), where, null, null);
        angkaKolom.addNext(LEXICALNAME.indexOf(SQLKeywords.OR), where, null, null);
        
        
        eq2Where.addNext(CONSTANT_STRING, string2Where, null, null);
        eq2Where.addNext(CONSTANT_NUMBER, string2Where, null, null);
		
        string2Where.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        string2Where.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, SQLKeywords.SELECT);
        string2Where.addNext(LEXICALNAME.indexOf(SQLKeywords.ORDER), order, null, SQLKeywords.SELECT);
        string2Where.addNext(LEXICALNAME.indexOf(SQLKeywords.AND), where, null, null);
        string2Where.addNext(LEXICALNAME.indexOf(SQLKeywords.OR), where, null, null);
        
        like.addNext(CONSTANT_STRING, stringLike, null,null);
        
        stringLike.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        stringLike.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, SQLKeywords.SELECT);
        stringLike.addNext(LEXICALNAME.indexOf(SQLKeywords.ORDER), order, null, SQLKeywords.SELECT);
        stringLike.addNext(LEXICALNAME.indexOf(SQLKeywords.AND), where, null, null);
        stringLike.addNext(LEXICALNAME.indexOf(SQLKeywords.OR), where, null, null);
        
        between.addNext(CONSTANT_NUMBER, num1Between, null, null);
        
        num1Between.addNext(LEXICALNAME.indexOf(SQLKeywords.AND), andBetween, null, null);
        
        andBetween.addNext(CONSTANT_NUMBER, num2Between, null, null);
        
        num2Between.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        num2Between.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, SQLKeywords.SELECT);
        num2Between.addNext(LEXICALNAME.indexOf(SQLKeywords.ORDER), order, null, SQLKeywords.SELECT);
        num2Between.addNext(LEXICALNAME.indexOf(SQLKeywords.AND), where, null, null);
        num2Between.addNext(LEXICALNAME.indexOf(SQLKeywords.OR), where, null, null);
        
        insert.addNext(LEXICALNAME.indexOf(SQLKeywords.INTO), into, null, null);
        
        into.addNext(VARIABLE, tableInsert, null, null);
        
        tableInsert.addNext(LEXICALNAME.indexOf(SQLKeywords.KURKA), kurkaInsert, null, null);
        
        kurkaInsert.addNext(VARIABLE, kolomInsert, null, null);
        
        kolomInsert.addNext(LEXICALNAME.indexOf(SQLKeywords.COMMA), kurkaInsert, null, null);
        kolomInsert.addNext(LEXICALNAME.indexOf(SQLKeywords.KURTUP), kurtubInsert, null, null);
        
        kurtubInsert.addNext(LEXICALNAME.indexOf(SQLKeywords.VALUES), valuesInsert, null, null);
        
        valuesInsert.addNext(LEXICALNAME.indexOf(SQLKeywords.KURKA), kurka2Insert, null, null);
        
        kurka2Insert.addNext(CONSTANT_NUMBER, stringAngkaInsert, null, null);
        kurka2Insert.addNext(CONSTANT_STRING, stringAngkaInsert, null, null);
        
        stringAngkaInsert.addNext(LEXICALNAME.indexOf(SQLKeywords.COMMA), kurka2Insert, null, null);
        stringAngkaInsert.addNext(LEXICALNAME.indexOf(SQLKeywords.KURTUP), kurtub2Insert, null, null);
        
        kurtub2Insert.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        
        delete.addNext(LEXICALNAME.indexOf(SQLKeywords.FROM), from, null, null);
        
        fromDelete.addNext(VARIABLE, tableDelete, null, null);
        
        tableDelete.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        tableDelete.addNext(LEXICALNAME.indexOf(SQLKeywords.WHERE), where, null, null);
        
        update.addNext(VARIABLE, tableUpdate, null, null);
        
        tableUpdate.addNext(LEXICALNAME.indexOf(SQLKeywords.SET), set, null, null);
        
        set.addNext(VARIABLE, kolomUpdate, null, null);
        
        kolomUpdate.addNext(LEXICALNAME.indexOf(SQLKeywords.EQ), eqUpdate, null, null);
        
        eqUpdate.addNext(CONSTANT_NUMBER, valuesUpdate, null, null);
        eqUpdate.addNext(CONSTANT_STRING, valuesUpdate, null, null);
        
        valuesUpdate.addNext(LEXICALNAME.indexOf(SQLKeywords.WHERE), where, null, null);
        valuesUpdate.addNext(LEXICALNAME.indexOf(SQLKeywords.COMMA), set, null, null);
        valuesUpdate.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null, null);
        
        order.addNext(LEXICALNAME.indexOf(SQLKeywords.BY), by, null, null);
        
        by.addNext(VARIABLE, kolomOrder, null, null);
        
        kolomOrder.addNext(LEXICALNAME.indexOf(SQLKeywords.ASC), asc, null,null);
        kolomOrder.addNext(LEXICALNAME.indexOf(SQLKeywords.DESC), desc, null,null);
        
        asc.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null,null);
        
        desc.addNext(LEXICALNAME.indexOf(SQLKeywords.SEMICOLON), semicolon, null,null);
        
        p.setNode(initial);
        return p;
    }
    public boolean parse(String sql){
        init();
        
        tokens = parseSQL2(sql);
        for(TokenLexic t: tokens){
            currentNode.next(t);
        }
        
        return currentNode.isFinalState();
    }
    public ArrayList<TokenLexic> getTokens(){
        return tokens;
    }
    private static ArrayList<TokenLexic> parseSQL2(String sql){
        /*
        memiliki fungsi yang sama dengan fungsi parseSQL, hanya saja menggunakan StringTokenizer
        tidak digunakan, hanya saja tidak dihapus sebagai pembanding saja
        */
        boolean logical=false;
        boolean stringWithSpace=false;
        boolean realNumber=false;
        String stringRealNumber="";
        String stringWithSpaceTemp="";
        String logicalString="";
        ArrayList<TokenLexic> result = new ArrayList();
        String temp;
        sql=sql.replace("\n", " ");
        sql=sql.replace("\t", " ");
        StringTokenizer st= new StringTokenizer(sql,"*,.<>=(); ",true);
        while(st.hasMoreTokens()){
            temp=st.nextToken();
            if (stringWithSpace && !temp.contains("\"")) {
                stringWithSpaceTemp+=temp;
            }
            else if (temp.trim().length()>0){
                if (stringWithSpace) {
                    if (temp.charAt(temp.length()-1)=='\"') 
                        result.add(new TokenLexic(CONSTANT_STRING, "Constant String", stringWithSpaceTemp+temp));
                    else if (temp.contains("\"")) 
                        result.add(new TokenLexic(UNIDENTIFIED, "Unidentified", stringWithSpaceTemp+" "+temp)); 
                    /*
                    else {
                        stringWithSpaceTemp+=" "+temp;
                        continue;
                    }*/
                    stringWithSpace=false;
                    stringWithSpaceTemp="";
                }
                else if (temp.charAt(0)== '\"' && temp.charAt(temp.length()-1) != '\"') {
                    stringWithSpace =true;
                    stringWithSpaceTemp=temp;
                }
                else if (temp.charAt(0)== '\"' && temp.charAt(temp.length()-1) == '\"') {
                    result.add(new TokenLexic(CONSTANT_STRING, "Constant String", temp));
                }
                else if (temp.matches("^[0-9]+$")) {
                    if (realNumber){
                        result.add(new TokenLexic(CONSTANT_NUMBER,"Constant Number",stringRealNumber+temp));
                        realNumber=false;
                        stringRealNumber="";
                    }
                    else {
                        realNumber=true;
                        stringRealNumber=temp;
                    }
                    
                }
                else if (temp.equals(".")) {
                    if (realNumber) stringRealNumber+=".";
                    else result.add(new TokenLexic(LEXICALNAME.indexOf(temp.toUpperCase()),"",temp));
                }
                else if (temp.equals("=")) {
                    if (logical) {
                        logical=false;
                        result.add(new TokenLexic(LEXICALNAME.indexOf(logicalString+temp), "", logicalString+temp));   
                    }
                    else 
                        result.add(new TokenLexic(LEXICALNAME.indexOf("="),"" , "="));
                    logicalString="";
                }
                else if (temp.equals(">")|| temp.equals("<")) {
                    if (logical){
                        logical=false;
                        if (temp.equals(">") || temp.equals("<")){
                            result.add(new TokenLexic(LEXICALNAME.indexOf(logicalString),"",logicalString));
                            result.add(new TokenLexic(LEXICALNAME.indexOf(temp),"",temp));
                        }
                        logicalString="";
                    }
                    else {
                        logical=true;
                        logicalString=temp;
                    }
                }
                else if (LEXICALNAME.contains(temp.toUpperCase())) 
                    result.add(new TokenLexic(LEXICALNAME.indexOf(temp.toUpperCase()),"",temp));
                else result.add(new TokenLexic(VARIABLE, "Variable" , temp));
                if (logical && !temp.equals("=") && !temp.equals(">")&& !temp.equals("<")) {
                    logical= false;
                    result.add(realNumber || stringWithSpace?result.size():result.size()-2, new TokenLexic(LEXICALNAME.indexOf(logicalString), "", logicalString));
                    logicalString="";
                }
                if (realNumber && !temp.matches("^[0-9]+$") && !temp.equals(".")) {
                    realNumber=false;
                    if (stringRealNumber.contains(".")) {
                        result.add(result.size()-2, new TokenLexic(CONSTANT_NUMBER, "Constant Number", stringRealNumber.substring(0, stringRealNumber.length()-1)));
                        result.add(result.size()-2, new TokenLexic(LEXICALNAME.indexOf("."), "", "."));
                    }
                    else {
                        result.add(logical?result.size():result.size()-1,new TokenLexic(CONSTANT_NUMBER, "Constant Number", stringRealNumber));
                    }
                }
            }
        }
        //if (stringWithSpace) result.add(new TokenLexic(CONSTANT_STRING, "Constant String", stringWithSpaceTemp));
        if (logical)
            result.add(new TokenLexic(LEXICALNAME.indexOf(logicalString), "", logicalString));
        if (realNumber) result.add(new  TokenLexic(CONSTANT_NUMBER, "Constant Number", stringRealNumber));
        
        return result;
    }
}
