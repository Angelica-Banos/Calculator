/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.History;
import core.models.Operation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author edangulo
 */
public class Calculator {

    //Suma
    public static Response add(String a, String b) {

//        Las variable deben incializarse antes de cualquien trycatch para que sus cambios se guarden, 
//        Las variables declaradas dentro de un try catch solo exiten dentro de ese bloque
        Double number1;
        Double number2;
        Double result;
        try {
            //verificar que sean números
            try {
                number1 = Double.parseDouble(a);
                number2 = Double.parseDouble(b);
            } catch (NumberFormatException ex) {
                return new Response("All inputs must be numbers ", Status.BAD_REQUEST);
            }

            //verificar que si sea posible sumarlos
            if ((number2 > 0 && number1 > Integer.MAX_VALUE - number2) || (number2 < 0 && number1 < Integer.MIN_VALUE - number2)) {
                return new Response("The numbers are way too big", Status.BAD_REQUEST);
            }
            //El problema con las siguientes verificaciones es que si están al límite de tamaño de los dobles (1.7x10^300 aprox) no se pueden realizar las operaciones pq pasan el límite
            // Verificar decimales
            if ((number1 * 100) % 1 != 0) {
                return new Response("The numbers can't have more than 3 decimals", Status.BAD_REQUEST);
            }
            if ((number2 * 100) % 1 != 0) {
                return new Response("The numbers can't have more than 3 decimals", Status.BAD_REQUEST);
            }

            //Respuesta con 3 decimales, tmb podría contrarestar que la cosa esta no pueda manejar el tamaño de los números
            result = number1 + number2;
            result = result * 100;
            result = result - result % 1;
            result = result / 100;

            //Sending to the history
            History history = History.getInstance();
            if (!history.addOperation(new Operation(number1, number2, "*", result))) {
                return new Response("Error", Status.INTERNAL_SERVER_ERROR);
            }
            //yeyy
            return new Response("Succesful calculation", Status.OK);
        } catch (Exception ex) {
            return new Response("Internal Error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    //Resta, Suma 2.0
    public static Response subtract(String a, String b) {
        double number1;
        double number2;
        double result;
        //Error interno
        try {

            //Verificar que sean números
            try {
                number1 = Double.parseDouble(a);
                number2 = Double.parseDouble(b);
            } catch (NumberFormatException ex) {
                return new Response("All inputs must be numbers ", Status.BAD_REQUEST);
            }
            //Verificar que si sea posible restarlos
            if ((number2 > 0 && number1 > Integer.MAX_VALUE - number2) || (number2 < 0 && number1 < Integer.MIN_VALUE - number2)) {
                return new Response("The numbers are way too big", Status.BAD_REQUEST);
            }

            //Verificar los decimales
            double decimales;
            if ((decimales = number1 * 1000) % 1 != 0) {
                return new Response("The numbers can't have more than 3 decimals", Status.BAD_REQUEST);
            }
            if ((decimales = number2 * 1000) % 1 != 0) {
                return new Response("The numbers can't have more than 3 decimals", Status.BAD_REQUEST);
            }

            //Hacer la resta
            result = number1 - number2;
            
           result = ((result*1000) - (result*1000%1)) /1000;

            //Enviar al historial
            History history = History.getInstance();
            if (!history.addOperation(new Operation(number1, number2, "-", result))) {
                return new Response("Error", Status.INTERNAL_SERVER_ERROR);
            }
            return new Response("Succesful calculation", Status.OK);
        } catch (Exception ex) {
            return new Response("Internal Error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response multiply(String a, String b) {
        double number1;
        double number2;
        double result;
        //nternal error
        try {
            //Check if they are numbers
            try {
                number1 = Double.parseDouble(a);
                number2 = Double.parseDouble(b);
            } catch (NumberFormatException e) {
                return new Response("Both inputs must be numers", Status.BAD_REQUEST);
            }

            //Check decimals
            //(number1<0 & number1%1>0.999) ||(number1>0 & number1%1<-0.999) Es otra forma de verificar
            if ((number1) * 1000 % 1 != 0) {
                return new Response("The maximum number of decimals is 3", Status.BAD_REQUEST);
            }
            if ((number2) * 1000 % 1 != 0) {
                return new Response("The maximum number of decimals is 3", Status.BAD_REQUEST);
            }

            //checking the size
            if (number1 != 0 && number2 > Integer.MAX_VALUE / number1) {
                return new Response("The numbers are way too big", Status.BAD_REQUEST);
            }

            //Multiplying
            result = number1 * number2;
            //Removing extra decimals
            result = Math.round(result * 1000) / 1000;
            //Sending to the history
            History history = History.getInstance();
            if (!history.addOperation(new Operation(number1, number2, "*", result))) {
                return new Response("Error", Status.INTERNAL_SERVER_ERROR);
            }
            //yeyy
            return new Response("Succesful calculation", Status.OK);
        } catch (Exception ex) {
            return new Response("Internal Error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response divide(String a, String b) {
        // Declaring the variables
        double number1;
        double number2;
        double result;
        // Internal Error
        try {
            // Check if they can be transformed into numbers
            try {
                number1 = Double.parseDouble(a);
                number2 = Double.parseDouble(b);
            } catch (NumberFormatException e) {
                return new Response("All inputs must be numbers", Status.BAD_REQUEST);
            }
            // Check that b isn't 0
            if(number2==0){
                return new Response("The second number can't be a 0", Status.BAD_REQUEST);
            }
            // Check size, man, it's 12:06, let's just assume the outer internal error handles it okay?
            
            //Divide
            result = number1/number2;
            //Get last 3 decimals
            result = ((result*1000) - (result*1000%1)) /1000;
            //Send to history
            History history = History.getInstance();
            if(!history.addOperation(new Operation(number1, number2, "/", result))){
                return new Response("I don't know how this happened", Status.INTERNAL_SERVER_ERROR);
            }
            return new Response("Succesful calculation", Status.OK);
        } catch (Exception ex) {
            return new Response("Internar Error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static String getResult() {
        History history = History.getInstance();
        LinkedList<Operation> operations = history.getOperations();
        double result = operations.getFirst().getResult();
        return String.valueOf(result);
    }

    public static ListModel<String> getFullHistory() {
        DefaultListModel<String> model = new DefaultListModel<>();
        History history = History.getInstance();
        LinkedList<Operation> operations = history.getOperations();
        for (Operation op : operations) {
            model.addElement(op.toString());
        }
        return model;
    }
}
