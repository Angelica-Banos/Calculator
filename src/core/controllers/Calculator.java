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

    //Suma, es mejor estudiar a partir de la resta tbh, es lo mismo
    public static Response add(String a, String b) {

//        Las variable deben incializarse antes de cualquien trycatch para que sus cambios se guarden, 
//        Las variables declaradas dentro de un try catch solo exiten dentro de ese bloque
        Double doubleA;
        Double doubleB;
        Double res;
        try {
            //verificar que sean números
            try {
                doubleA = Double.parseDouble(a);
                doubleB = Double.parseDouble(b);
            } catch (NumberFormatException ex) {
                return new Response("All inputs must be numbers ", Status.BAD_REQUEST);
            }

            //verificar que si sea posible sumarlos
            if ((doubleB > 0 && doubleA > Integer.MAX_VALUE - doubleB) || (doubleB < 0 && doubleA < Integer.MIN_VALUE - doubleB)) {
                return new Response("The numbers are way too big", Status.BAD_REQUEST);
            }
            //El problema con las siguientes verificaciones es que si están al límite de tamaño de los dobles (1.7x10^300 aprox) no se pueden realizar las operaciones pq pasan el límite
            // Verificar decimales
            if ((doubleA * 100) % 1 != 0) {
                return new Response("The numbers can't have more than 3 decimals", Status.BAD_REQUEST);
            }
            if ((doubleB * 100) % 1 != 0) {
                return new Response("The numbers can't have more than 3 decimals", Status.BAD_REQUEST);
            }

            //Respuesta con 3 decimales, tmb podría contrarestar que la cosa esta no pueda manejar el tamaño de los números
            res = doubleA + doubleB;
            res = res * 100;
            res = res - res % 1;
            res = res / 100;
            
            //Sending to the history
            History history = History.getInstance();
            if (!history.addOperation(new Operation(doubleA, doubleB, "*", res))) {
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
        double doubleA;
        double doubleB;
        double res;
        //Error interno
        try {

            //Verificar que sean números
            try {
                doubleA = Double.parseDouble(a);
                doubleB = Double.parseDouble(b);
            } catch (NumberFormatException ex) {
                return new Response("All inputs must be numbers ", Status.BAD_REQUEST);
            }
            //Verificar que si sea posible restarlos
            if ((doubleB > 0 && doubleA > Integer.MAX_VALUE - doubleB) || (doubleB < 0 && doubleA < Integer.MIN_VALUE - doubleB)) {
                return new Response("The numbers are way too big", Status.BAD_REQUEST);
            }

            //Verificar los decimales
            double decimales;
            if ((decimales = doubleA * 1000) % 1 != 0) {
                return new Response("The numbers can't have more than 3 decimals", Status.BAD_REQUEST);
            }
            if ((decimales = doubleB * 1000) % 1 != 0) {
                return new Response("The numbers can't have more than 3 decimals", Status.BAD_REQUEST);
            }

            //Hacer la resta
            res = doubleA - doubleB;
            //Quitar los decimales extra usando Math.round
            Math.round(res * 1000);
            res = res / 1000;
            // Otra forma: res = res * 1000; res = res - res % 1; res = res/1000

            //Enviar al historial
            History history = History.getInstance();
            if (!history.addOperation(new Operation(doubleA, doubleB, "*", res))) {
                return new Response("Error", Status.INTERNAL_SERVER_ERROR);
            }
            return new Response("Succesful calculation", Status.OK);
        } catch (Exception ex) {
            return new Response("Internal Error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response multiply(String a, String b) {
        double doubleA;
        double doubleB;
        double res;
        //nternal error
        try {
            //Check if they are numbers
            try {
                doubleA = Double.parseDouble(a);
                doubleB = Double.parseDouble(b);
            } catch (NumberFormatException e) {
                return new Response("Both inputs must be numers", Status.BAD_REQUEST);
            }

            //Check decimals
            //(doubleA<0 & doubleA%1>0.999) ||(doubleA>0 & doubleA%1<-0.999) Es otra forma de verificar
            if ((doubleA) * 1000 % 1 != 0) {
                return new Response("The maximum number of decimals is 3", Status.BAD_REQUEST);
            }
            if ((doubleB) * 1000 % 1 != 0) {
                return new Response("The maximum number of decimals is 3", Status.BAD_REQUEST);
            }

            //checking the size
            if (doubleA != 0 && doubleB > Integer.MAX_VALUE / doubleA) {
                return new Response("The numbers are way too big", Status.BAD_REQUEST);
            }

            //Multiplying
            res = doubleA * doubleB;
            //Removing extra decimals
            res = Math.round(res * 1000) / 1000;
            //Sending to the history
            History history = History.getInstance();
            if (!history.addOperation(new Operation(doubleA, doubleB, "*", res))) {
                return new Response("Error", Status.INTERNAL_SERVER_ERROR);
            }
            //yeyy
            return new Response("Succesful calculation", Status.OK);
        } catch (Exception ex) {
            return new Response("Internal Error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public double divide(double a, double b) {
        return a / b;
    }

    public static String getResult() {
        History history = History.getInstance();
        LinkedList<Operation> operations = history.getOperations();
        double res = operations.getFirst().getResult();
        return String.valueOf(res);
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
