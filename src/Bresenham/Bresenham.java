package Bresenham;

import java.awt.Graphics;

public class Bresenham {
    public static void AlgorithmBresenham(Graphics g,int x0, int y0, int x1, int y1) {
        int deltaX = Math.abs(x1 - x0);
        int deltaY = Math.abs(y1 - y0);
        int incrementX = x0 < x1 ? 1 : -1; 
        int incrementY = y0 < y1 ? 1 : -1; 
        int erro = deltaX - deltaY; 

        while (true) {
            g.fillRect(x0, y0, 1, 1);
            if (x0 == x1 && y0 == y1) break; 
            int erro2 = 2 * erro; 
            if (erro2 > -deltaY) {
                erro -= deltaY;
                x0 += incrementX;
            }
            if (erro2 < deltaX) {
                erro += deltaX;
                y0 += incrementY;
            }
        }
    }
}
