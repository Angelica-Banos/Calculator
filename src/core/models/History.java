/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author edangulo
 */
public class History {
    
    private static History instance;
    private static LinkedList<Operation> operations;

    private History() {
        this.operations = new LinkedList<>();
    }
    
    public static History getInstance(){
        if(instance == null){
            instance = new History();
        }
        return instance;
    }
    
    public boolean addOperation(Operation operation) {
        try {
            operations.addFirst(operation);
        return true;
        } catch (Exception e) {
            return false;
        }

    }

    public LinkedList<Operation> getOperations() {
        return operations;
    }
    
}
