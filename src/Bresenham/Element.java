package Bresenham;

public class Element {
    private float[][] vertices;
    private int[][] faces;

    public Element(float[][] customVertices) {
        this.vertices = customVertices;
    
        // Definir as faces do cubo (2 triângulos por face)
        this.faces = new int[][]{
            {0, 1, 2}, {0, 2, 3}, // Traseira
            {4, 5, 6}, {4, 6, 7}, // Frontal
            {0, 1, 5}, {0, 5, 4}, // Inferior
            {2, 3, 7}, {2, 7, 6}, // Superior
            {0, 3, 7}, {0, 7, 4}, // Esquerda
            {1, 2, 6}, {1, 6, 5}  // Direita
        };
    }

    public void transform(Matrix4x4 matrix) {
        for (int i = 0; i < vertices.length; i++) {
            // Transladar para o ponto de foco
            vertices[i][0] -= matrix.getFocusX();
            vertices[i][1] -= matrix.getFocusY();
    
            // Aplicar a transformação
            vertices[i] = matrix.multiply(vertices[i]);
    
            // Transladar de volta do ponto de foco
            vertices[i][0] += matrix.getFocusX();
            vertices[i][1] += matrix.getFocusY();
        }
    }

    public float[][] getVertices() {
        return vertices;
    }
}