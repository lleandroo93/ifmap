/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifmap.util;

/**
 *
 * @author leandro
 */
public class Main {

    public static void main(String[] args) {
        Float x1 = -2.25f;
        Float y1 = 3f;
        
        Float x2 = 1.5f;
        Float y2 = -2f;
        
        Float pX = 0f;
        Float pY = 3f;
        
        Float A;
        if (x2 == 0 && x1 == 0){
            A = 0f;
        } else {
            A = -((y2 - y1) / (x2 - x1));
        }
        Float B = 1f;
        Float C = - y1 - (A * x1);

        Float distancia = Math.abs(A * pX + B * pY + C) / ((float) Math.sqrt(A * A + B * B));
        System.out.println("Coeficiente angular = " + ((y2 - y1) / (x2 - x1)));
        System.out.println("Equação = " + A + "X + " + B + "Y + " +C);
        System.out.println("DISTANCIA = " + distancia);
    }

}
