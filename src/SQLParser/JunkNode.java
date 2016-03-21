/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLParser;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public class JunkNode extends Node{

    public JunkNode(Parser p){
        super(p,false);
    }
    @Override
    public void next(TokenLexic input) {
        
    }
    
}
