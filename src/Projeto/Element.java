package Projeto;

import java.util.List;

public class Element {
    private List<Triangulo3D> triangulos;
    private Matrix4x4 localTransform;

    public Element(List<Triangulo3D> triangulos) {
        this.triangulos = triangulos;
        this.localTransform = new Matrix4x4();
    }

    public Matrix4x4 getLocalTransform(){
        return localTransform;
    }

    public List<Triangulo3D> getTriangulos() {
        return triangulos;
    }

    public void transform(Matrix4x4 matrix) {
        Matrix4x4 combinedTransform = matrix.multiply(localTransform);
        for (Triangulo3D triangulo : triangulos) {
            triangulo.p1 = transformPoint(triangulo.p1, combinedTransform);
            triangulo.p2 = transformPoint(triangulo.p2, combinedTransform);
            triangulo.p3 = transformPoint(triangulo.p3, combinedTransform);
        }
    }

    private Ponto3D transformPoint(Ponto3D point, Matrix4x4 matrix) {
        float[] transformed = matrix.multiply(point.toArray());
        return new Ponto3D(transformed[0] / transformed[3], transformed[1] / transformed[3], transformed[2] / transformed[3]);
    }
}