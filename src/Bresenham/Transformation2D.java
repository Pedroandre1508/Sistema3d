package Bresenham;

import java.util.List;

public class Transformation2D {

    public static void applyTranslate(List<int[]> lines, float dx, float dy) {
        for (int[] line : lines) {
            line[0] += dx;
            line[1] += dy;
            line[2] += dx;
            line[3] += dy;
        }
    }

    public static void applyScale(List<int[]> lines, float sx, float sy, int cx, int cy) {
        for (int[] line : lines) {
            line[0] = (int) ((line[0] - cx) * sx + cx);
            line[1] = (int) ((line[1] - cy) * sy + cy);
            line[2] = (int) ((line[2] - cx) * sx + cx);
            line[3] = (int) ((line[3] - cy) * sy + cy);
        }
    }

    public static void applyRotation(List<int[]> lines, double angulo, int cx, int cy) {
        double rad = Math.toRadians(angulo);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        for (int[] line : lines) {
            int x0 = line[0] - cx;
            int y0 = line[1] - cy;
            int x1 = line[2] - cx;
            int y1 = line[3] - cy;

            line[0] = (int) (x0 * cos - y0 * sin + cx);
            line[1] = (int) (x0 * sin + y0 * cos + cy);
            line[2] = (int) (x1 * cos - y1 * sin + cx);
            line[3] = (int) (x1 * sin + y1 * cos + cy);
        }
    }

    public static void applyReflect(List<int[]> lines, boolean eixoX, boolean eixoY, int cx, int cy) {
        for (int[] line : lines) {
            if (eixoX) {
                line[1] = 2 * cy - line[1];
                line[3] = 2 * cy - line[3];
            }
            if (eixoY) {
                line[0] = 2 * cx - line[0];
                line[2] = 2 * cx - line[2];
            }
        }
    }

    public static void applyShear(List<int[]> lines, float shx, float shy, int cx, int cy) {
        for (int[] line : lines) {
            int x0 = line[0] - cx;
            int y0 = line[1] - cy;
            int x1 = line[2] - cx;
            int y1 = line[3] - cy;

            line[0] = (int) (x0 + shx * y0 + cx);
            line[1] = (int) (y0 + shy * x0 + cy);
            line[2] = (int) (x1 + shx * y1 + cx);
            line[3] = (int) (y1 + shy * x1 + cy);
        }
    }
}