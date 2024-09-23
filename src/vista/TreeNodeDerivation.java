/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import java.util.ArrayList;

public class TreeNodeDerivation {
    private String value;
    private ArrayList<TreeNodeDerivation> children = new ArrayList<>();
    private static int nextId = 0;
    private int id;
    private TreeNodeDerivation parent;

    public ArrayList<TreeNodeDerivation> getChildren() {
        return children;
    }

    public TreeNodeDerivation(){
        
    }
    
    public TreeNodeDerivation getParent() {
        return parent;
    }

    public void setParent(TreeNodeDerivation parent) {
        this.parent = parent;
    }

    public String getValue() {
        return value;
    }

    public int getDepth() {
        int depth = 0;
        TreeNodeDerivation current = this;
        while (current.getParent() != null) {
            depth++;
            current = current.getParent();
        }
        return depth;
    }


    public TreeNodeDerivation(String value) {
        this.value = value;
        this.id = nextId++;
    }

    public void addChild(TreeNodeDerivation child) {
        children.add(child);
    }

    public TreeNodeDerivation getLastChild() {
        if (children.isEmpty()) {
            return null;
        }
        return children.get(children.size() - 1);
    }

    public int getId() {
        return id;
    }

    public void printTree(int level) {
        
        System.out.println(value + " (ID: " + id + ")");
        for (TreeNodeDerivation child : children) {
            child.printTree(level + 1);
        
        }
    }
}
