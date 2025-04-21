package Projeto;

public class Ponto3D {
    public float x, y, z, w;

    public Ponto3D(float x, float y, float z) {
        this(x, y, z, 1);
    }

    public Ponto3D(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float[] toArray() {
        return new float[]{x, y, z, w};
    }
}