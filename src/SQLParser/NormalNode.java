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
public class NormalNode extends Node{

    public NormalNode(Parser p, boolean finalState){
        super(p,finalState);
    }
    @Override
    public void next(TokenLexic input) {
        int n = -1;
        int code = input.getTokenCode();System.out.println(input.getValue());System.out.println(code);
        for(int i = 0; i < inputs.size();i++){System.out.println(inputs.get(i));
            if (inputs.get(i)==code) {
                n = i;
                break;
            }
        }
        parser.setNode(n==-1 ? parser.getJunkNode() : nextNodes.get(n));
    }
    
}
