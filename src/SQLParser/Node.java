/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SQLParser;

import java.util.ArrayList;

/**
 *
 * @author BagusThanatos (github.com/BagusThanatos)
 */
public abstract class Node {
    Parser parser;
    ArrayList<String> pushList, popList;
    ArrayList<Integer> inputs;
    ArrayList<Node> nextNodes;
    boolean isFinalState;
    public Node(Parser p, boolean finalState){
        this.parser = p;
        this.inputs = new ArrayList();
        this.nextNodes = new ArrayList();
        this.pushList= new ArrayList();
        this.popList = new ArrayList();
        this.isFinalState= finalState;
    }
    public void addNext(int input, Node nextNode, String push, String pop){
        inputs.add(input);
        nextNodes.add(nextNode);
        pushList.add(push);
        popList.add(pop);
    }
    public boolean isFinalState(){
        return isFinalState;
    }
    public abstract void next(TokenLexic input);
    
}
