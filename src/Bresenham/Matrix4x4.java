package Bresenham;

public class Matrix4x4 {
    float[][] mat;
    private float focusX = 0;
    private float focusY = 0;

    public Matrix4x4() {
        mat = new float[4][4];
        setIdentity();
    }

    public void setIdentity() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mat[i][j] = (i == j) ? 1 : 0;
            }
        }
    }

    public float getFocusX() {
        return focusX;
    }
    
    public float getFocusY() {
        return focusY;
    }

    public void setFocus(float x, float y) {
        this.focusX = x;
        this.focusY = y;
    }
    

    public void setRotationX(float angle) {
        setIdentity();
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        mat[1][1] = cos;
        mat[1][2] = -sin;
        mat[2][1] = sin;
        mat[2][2] = cos;
    }

    public void setRotationY(float angle) {
        setIdentity();
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        mat[0][0] = cos;
        mat[0][2] = sin;
        mat[2][0] = -sin;
        mat[2][2] = cos;
    }

    public void setRotationZ(float angle) {
        setIdentity();
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        mat[0][0] = cos;
        mat[0][1] = -sin;
        mat[1][0] = sin;
        mat[1][1] = cos;
        mat[2][2] = 1; 
        mat[3][3] = 1; 
    }

    public float[] multiply(float[] vec) {
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = mat[i][0] * vec[0] + mat[i][1] * vec[1] + mat[i][2] * vec[2] + mat[i][3] * vec[3];
        }
        return result;
    }
}