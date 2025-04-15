package Bresenham;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class App extends JPanel {

    private long lastTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    private CohenSutherland clipper;
    private List<Element> cubes;
    private Matrix4x4 transformationMatrix;

    public App() {
        clipper = null;
        cubes = new ArrayList<>();
        transformationMatrix = new Matrix4x4();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // Clique com o botão direito
                    // Definir o ponto de foco para a rotação
                    int focusX = e.getX();
                    int focusY = e.getY();

                    // Atualizar o ponto de foco na matriz de transformação
                    transformationMatrix.setFocus(focusX, focusY);

                    // Desenhar um pequeno quadrado vermelho no ponto de foco
                    Graphics g = getGraphics();
                    g.setColor(Color.RED);
                    g.fillRect(focusX - 2, focusY - 2, 5, 5); // Quadrado de 5x5 pixels
                    g.dispose();
                } else if (e.getButton() == MouseEvent.BUTTON1) { // Clique com o botão esquerdo
                    int clickX = e.getX();
                    int clickY = e.getY();

                    if (clickX >= clipper.getXMin() && clickX <= clipper.getXMax()
                            && clickY >= clipper.getYMin() && clickY <= clipper.getYMax()) {

                        int size = 50; // Tamanho do cubo

                        float[][] vertices = {
                            {clickX - size, clickY - size, -size, 1}, // Vértice 0
                            {clickX + size, clickY - size, -size, 1}, // Vértice 1
                            {clickX + size, clickY + size, -size, 1}, // Vértice 2
                            {clickX - size, clickY + size, -size, 1}, // Vértice 3
                            {clickX - size, clickY - size, size, 1}, // Vértice 4
                            {clickX + size, clickY - size, size, 1}, // Vértice 5
                            {clickX + size, clickY + size, size, 1}, // Vértice 6
                            {clickX - size, clickY + size, size, 1} // Vértice 7
                        };

                        cubes.add(new Element(vertices)); // Adicionar o novo cubo à lista
                    }
                }

                repaint();
            }
        });

        // Configurar foco para capturar eventos de teclado
        setFocusable(true);
        requestFocusInWindow();

        // Adicionar KeyListener para capturar eventos de teclado
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W ->
                        transformation("translate", 0, -10, 0); // Translação para cima
                    case KeyEvent.VK_S ->
                        transformation("translate", 0, 10, 0);  // Translação para baixo
                    case KeyEvent.VK_A ->
                        transformation("translate", -10, 0, 0); // Translação para a esquerda
                    case KeyEvent.VK_D ->
                        transformation("translate", 10, 0, 0);  // Translação para a direita
                    case KeyEvent.VK_Q ->
                        transformation("rotationZ", -10);       // Rotação plana anti-horária
                    case KeyEvent.VK_E ->
                        transformation("rotationZ", 10);        // Rotação plana horária
                    case KeyEvent.VK_X ->
                        transformation("rotationX", 10);        // Rotação no eixo X
                    case KeyEvent.VK_C ->
                        transformation("rotationY", 10);        // Rotação no eixo Y
                    case KeyEvent.VK_Z ->
                        transformation("rotationZ3D", 10);      // Rotação tridimensional no eixo Z
                    case KeyEvent.VK_DOWN ->
                        transformation("scale", 0.9f, 0.9f, 0.9f); // Escala para diminuir
                    case KeyEvent.VK_UP ->
                        transformation("scale", 1.1f, 1.1f, 1.1f); // Escala para aumentar
                    case KeyEvent.VK_R ->
                        transformation("reflect", 1, 0);        // Reflexão no eixo X
                    case KeyEvent.VK_T ->
                        transformation("reflect", 0, 1);        // Reflexão no eixo Y
                    case KeyEvent.VK_Y ->
                        transformation("shear", 0.1f, 0);       // Shear no eixo X
                    case KeyEvent.VK_U ->
                        transformation("shear", 0, 0.1f);       // Shear no eixo Y
                }
            }
        });

        Timer timer = new Timer(1000 / 60, e -> {
            long currentTime = System.nanoTime();
            frames++;
            if (currentTime - lastTime >= 1000000000) {
                fps = frames;
                frames = 0;
                lastTime = currentTime;
            }
            repaint();
        });
        timer.start();
    }

    public void transformation(String tipo, float... params) {
        if (cubes.isEmpty()) {
            return; // Certifique-se de que há cubos antes de aplicar transformações
        }

        transformationMatrix.setIdentity(); // Sempre começar com uma matriz identidade

        switch (tipo) {
            case "translate" -> {
                // Translação em X, Y e Z
                transformationMatrix.mat[0][3] = params[0]; // Translação em X
                transformationMatrix.mat[1][3] = params[1]; // Translação em Y
                transformationMatrix.mat[2][3] = params.length > 2 ? params[2] : 0; // Translação em Z (opcional)
            }
            case "scale" -> {
                // Escala em X, Y e Z
                transformationMatrix.mat[0][0] = params[0]; // Escala em X
                transformationMatrix.mat[1][1] = params[1]; // Escala em Y
                transformationMatrix.mat[2][2] = params.length > 2 ? params[2] : 1; // Escala em Z (opcional)
            }
            case "rotationX" ->
                transformationMatrix.setRotationX(params[0]); // Rotação no eixo X
            case "rotationY" ->
                transformationMatrix.setRotationY(params[0]); // Rotação no eixo Y
            case "rotationZ" ->
                transformationMatrix.setRotationZ(params[0]); // Rotação plana no eixo Z
            case "rotationZ3D" ->
                transformationMatrix.setRotationZ(params[0]); // Rotação tridimensional no eixo Z
            case "reflect" -> {
                // Reflexão em X e Y
                transformationMatrix.mat[0][0] = params[0] == 1 ? -1 : 1; // Reflexão no eixo X
                transformationMatrix.mat[1][1] = params[1] == 1 ? -1 : 1; // Reflexão no eixo Y
            }
            case "shear" -> {
                // Shear em X e Y
                transformationMatrix.mat[0][1] = params[0]; // Shear em X
                transformationMatrix.mat[1][0] = params[1]; // Shear em Y
            }
        }

        // Aplicar a transformação a todos os cubos
        for (Element cube : cubes) {
            cube.transform(transformationMatrix);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int squareSize = Math.min(getWidth(), getHeight()) - 100; // Tamanho do quadrado com margem
        int xMin = (getWidth() - squareSize) / 2;
        int yMin = (getHeight() - squareSize) / 2;
        int xMax = xMin + squareSize;
        int yMax = yMin + squareSize;

        clipper = new CohenSutherland(xMin, yMin, xMax, yMax);

        // Desenhar a área de clipping
        g.setColor(Color.ORANGE);
        g.drawRect(clipper.getXMin(), clipper.getYMin(), clipper.getXMax() - clipper.getXMin(), clipper.getYMax() - clipper.getYMin());

        // Desenhar todos os cubos
        g.setColor(Color.BLACK);
        for (Element cube : cubes) {
            float[][] vertices = cube.getVertices();

            // Definir as arestas do cubo
            int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0}, // Face traseira
                {4, 5}, {5, 6}, {6, 7}, {7, 4}, // Face frontal
                {0, 4}, {1, 5}, {2, 6}, {3, 7}  // Conexões entre frente e trás
            };

            // Desenhar as arestas do cubo, recortando as que estão fora da área de clipping
            for (int[] edge : edges) {
                int x1 = Math.round(vertices[edge[0]][0]);
                int y1 = Math.round(vertices[edge[0]][1]);
                int x2 = Math.round(vertices[edge[1]][0]);
                int y2 = Math.round(vertices[edge[1]][1]);

                int[] p0 = {x1, y1};
                int[] p1 = {x2, y2};

                // Recortar a linha usando o algoritmo de Cohen-Sutherland
                if (clipper.clipLine(p0, p1)) {
                    g.drawLine(p0[0], p0[1], p1[0], p1[1]);
                }
            }
        }

        // Desenhar o ponto de foco
        g.setColor(Color.RED);
        g.fillRect((int) transformationMatrix.getFocusX() - 2, (int) transformationMatrix.getFocusY() - 2, 5, 5);

        // Exibir FPS
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + fps, 10, 20);
    }
}