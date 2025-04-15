package Bresenham;

public class CohenSutherland {
    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;    // 1000

    private int xMin, yMin, xMax, yMax;

    public CohenSutherland(int xMin, int yMin, int xMax, int yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public int getXMin() {
        return xMin;
    }

    public int getYMin() {
        return yMin;
    }

    public int getXMax() {
        return xMax;
    }

    public int getYMax() {
        return yMax;
    }

    private int computeCode(int x, int y) {
        int code = INSIDE;

        if (x < xMin) {
            code |= LEFT;
        } else if (x > xMax) {
            code |= RIGHT;
        }
        if (y < yMin) {
            code |= BOTTOM;
        } else if (y > yMax) {
            code |= TOP;
        }

        return code;
    }

    public boolean clipLine(int[] p0, int[] p1) {
        int x0 = p0[0], y0 = p0[1];
        int x1 = p1[0], y1 = p1[1];

        int code0 = computeCode(x0, y0);
        int code1 = computeCode(x1, y1);

        boolean accept = false;

        while (true) {
            if ((code0 | code1) == 0) {
                accept = true;
                break;
            } else if ((code0 & code1) != 0) {
                break;
            } else {
                int codeOut;
                int x = 0, y = 0;

                if (code0 != 0) {
                    codeOut = code0;
                } else {
                    codeOut = code1;
                }

                if ((codeOut & TOP) != 0) {
                    x = x0 + (x1 - x0) * (yMax - y0) / (y1 - y0);
                    y = yMax;
                } else if ((codeOut & BOTTOM) != 0) {
                    x = x0 + (x1 - x0) * (yMin - y0) / (y1 - y0);
                    y = yMin;
                } else if ((codeOut & RIGHT) != 0) {
                    y = y0 + (y1 - y0) * (xMax - x0) / (x1 - x0);
                    x = xMax;
                } else if ((codeOut & LEFT) != 0) {
                    y = y0 + (y1 - y0) * (xMin - x0) / (x1 - x0);
                    x = xMin;
                }

                if (codeOut == code0) {
                    x0 = x;
                    y0 = y;
                    code0 = computeCode(x0, y0);
                } else {
                    x1 = x;
                    y1 = y;
                    code1 = computeCode(x1, y1);
                }
            }
        }

        if (accept) {
            p0[0] = x0;
            p0[1] = y0;
            p1[0] = x1;
            p1[1] = y1;
        }

        return accept;
    }
}