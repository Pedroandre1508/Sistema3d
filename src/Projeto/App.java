package Projeto;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class App extends JPanel {

    private long lastTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    private CohenSutherland clipper;
    private List<Element> elements;
    private Matrix4x4 transformationMatrix;
    private Matrix4x4 projectionMatrix;
    private Matrix4x4 viewMatrix;
    private int focusX = -1;
    private int focusY = -1;

    public App() {
        clipper = null;
        elements = new ArrayList<>();
        transformationMatrix = new Matrix4x4();
        projectionMatrix = new Matrix4x4();
        viewMatrix = new Matrix4x4();
        viewMatrix.setIdentity();
        projectionMatrix.setPerspectiva(1000); // Configura a matriz de perspectiva

         // Adiciona o botão para carregar arquivo OBJ
         JButton loadObjButton = new JButton("Carregar OBJ");
         loadObjButton.addActionListener(e -> loadObjFile());
         this.add(loadObjButton);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // Botão direito do mouse
                    focusX = e.getX();
                    focusY = e.getY();

                    // Atualiza a matriz de transformação para centralizar o foco
                    transformationMatrix.setIdentity();
                    transformationMatrix.mat[0][3] = (getWidth() / 2.0f) - focusX;
                    transformationMatrix.mat[1][3] = (getHeight() / 2.0f) - focusY;

                    repaint(); // Solicita a atualização da tela
                } else if (e.getButton() == MouseEvent.BUTTON1) { // Botão esquerdo do mouse
                    // Ajusta as coordenadas do clique para o sistema de coordenadas 3D
                    int clickX = e.getX() - (getWidth() / 2);
                    int clickY = e.getY() - (getHeight() / 2);

                    if (clickX >= clipper.getXMin() - (getWidth() / 2) && clickX <= clipper.getXMax() - (getWidth() / 2)
                            && clickY >= clipper.getYMin() - (getHeight() / 2) && clickY <= clipper.getYMax() - (getHeight() / 2)) {

                        int size = 50;

                        // Adiciona o quadrado no local ajustado
                        elements.add(createCube(clickX, clickY, 0, size));
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) { // Botão do meio (rodinha)
                    int clickX = e.getX();
                    int clickY = e.getY();

                    // Desenha um quadrado azul no local do clique
                    Graphics g = getGraphics();
                    g.setColor(Color.BLUE);
                    g.fillRect(clickX - 25, clickY - 25, 10, 10); // Quadrado de 50x50
                }

                repaint();
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W ->
                        transformation("translate", 0, -10, 0); // Move para frente
                    case KeyEvent.VK_S ->
                        transformation("translate", 0, 10, 0);  // Move para trás
                    case KeyEvent.VK_A ->
                        transformation("translate", -10, 0, 0); // Move para a esquerda
                    case KeyEvent.VK_D ->
                        transformation("translate", 10, 0, 0);  // Move para a direita
                    case KeyEvent.VK_Q ->
                        transformation("rotationY", -10);       // Rotaciona no eixo Y (anti-horário)
                    case KeyEvent.VK_E ->
                        transformation("rotationY", 10);        // Rotaciona no eixo Y (horário)
                    case KeyEvent.VK_C ->
                        transformation("rotationX", -10);
                    case KeyEvent.VK_X ->
                        transformation("rotationX", 10);        // Rotaciona no eixo X (para baixo)
                    case KeyEvent.VK_UP ->
                        transformation("scale", 1.1f, 1.1f, 1.1f);
                    case KeyEvent.VK_DOWN ->
                        transformation("scale", 0.9f, 0.9f, 0.9f);
                    case KeyEvent.VK_R ->
                        transformation("reflect", 1, 0, 0);     // Reflexão no eixo X
                    case KeyEvent.VK_T ->
                        transformation("reflect", 0, 1, 0);     // Reflexão no eixo Y
                    case KeyEvent.VK_Y ->
                        transformation("shear", -0.5f, 0, 1);   
                    case KeyEvent.VK_U ->
                        transformation("shear", 0.5f, 0, 0, 0, 0, 0);
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

    public void transformation(String type, float... params) {
        Matrix4x4 transformation = new Matrix4x4();
        transformation.setIdentity();

        switch (type) {
            case "translate" -> {
                transformation.mat[0][3] = params[0];
                transformation.mat[1][3] = params[1];
                transformation.mat[2][3] = params.length > 2 ? params[2] : 0;
            }
            case "scale" -> {
                transformation.mat[0][0] = params[0];
                transformation.mat[1][1] = params[1];
                transformation.mat[2][2] = params.length > 2 ? params[2] : 1;
            }
            case "rotationX" ->
                transformation.setRotationX(params[0]);
            case "rotationY" ->
                transformation.setRotationY(params[0]);
            case "rotationZ" ->
                transformation.setRotationZ(params[0]);
            case "reflect" -> {
                transformation.mat[0][0] = params[0] == 1 ? -1 : 1; // Reflexão no eixo X
                transformation.mat[1][1] = params[1] == 1 ? -1 : 1; // Reflexão no eixo Y
                transformation.mat[2][2] = params.length > 2 && params[2] == 1 ? -1 : 1; // Reflexão no eixo Z
            }
            case "shear" -> {
                transformation.mat[0][1] = params[0]; // Cisalhamento no eixo X em relação ao Y
                transformation.mat[0][2] = params.length > 1 ? params[1] : 0; // Cisalhamento no eixo X em relação ao Z
                transformation.mat[1][0] = params.length > 2 ? params[2] : 0; // Cisalhamento no eixo Y em relação ao X
                transformation.mat[1][2] = params.length > 3 ? params[3] : 0; // Cisalhamento no eixo Y em relação ao Z
                transformation.mat[2][0] = params.length > 4 ? params[4] : 0; // Cisalhamento no eixo Z em relação ao X
                transformation.mat[2][1] = params.length > 5 ? params[5] : 0; // Cisalhamento no eixo Z em relação ao Y
            }
        }

        // Atualiza a matriz de visualização
        viewMatrix = viewMatrix.multiply(transformation);
        repaint();
    }

     private void loadObjFile() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    Element objElement = parseOBJ(file);
                    elements.add(objElement);
                    repaint();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao carregar o arquivo OBJ: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Garante que o foco retorne à janela principal
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        }).start();
    }

    private Element parseOBJ(File file) throws IOException {
        List<Ponto3D> vertices = new ArrayList<>();
        List<Triangulo3D> triangulos = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("v ")) {
                    // Lê os vértices
                    String[] parts = line.split("\\s+");
                    float x = Float.parseFloat(parts[1]);
                    float y = Float.parseFloat(parts[2]);
                    float z = Float.parseFloat(parts[3]);
                    vertices.add(new Ponto3D(x, y, z));
                } else if (line.startsWith("f ")) {
                    // Lê as faces e converte para triângulos
                    String[] parts = line.split("\\s+");
                    List<Integer> faceIndices = new ArrayList<>();
                    for (int i = 1; i < parts.length; i++) {
                        String[] vertexData = parts[i].split("/");
                        int vertexIndex = Integer.parseInt(vertexData[0]);
                        if (vertexIndex < 0) {
                            vertexIndex = vertices.size() + vertexIndex + 1; // Índices negativos
                        }
                        faceIndices.add(vertexIndex - 1); // Corrige para índice baseado em 0
                    }
                    // Divide faces com mais de 3 vértices em triângulos
                    for (int i = 1; i < faceIndices.size() - 1; i++) {
                        triangulos.add(new Triangulo3D(
                                vertices.get(faceIndices.get(0)),
                                vertices.get(faceIndices.get(i)),
                                vertices.get(faceIndices.get(i + 1))
                        ));
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("Erro ao processar o arquivo OBJ: " + e.getMessage());
        }
    
        Element objElement = new Element(triangulos);
        centerElementInClippingArea(objElement);
        return objElement;
    }

    private void centerElementInClippingArea(Element element) {
        // Calcula o centro do objeto
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE, maxZ = Float.MIN_VALUE;

        for (Triangulo3D triangulo : element.getTriangulos()) {
            for (Ponto3D ponto : new Ponto3D[]{triangulo.p1, triangulo.p2, triangulo.p3}) {
                minX = Math.min(minX, ponto.x);
                minY = Math.min(minY, ponto.y);
                minZ = Math.min(minZ, ponto.z);
                maxX = Math.max(maxX, ponto.x);
                maxY = Math.max(maxY, ponto.y);
                maxZ = Math.max(maxZ, ponto.z);
            }
        }

        float centerX = (minX + maxX) / 2;
        float centerY = (minY + maxY) / 2;
        float centerZ = (minZ + maxZ) / 2;

        // Translada o objeto para o centro da área de clipping
        float translateX = (clipper.getXMin() + clipper.getXMax()) / 12 - centerX;
        float translateY = (clipper.getYMin() + clipper.getYMax()) / 12 - centerY;
        float translateZ = 0 - centerZ; // Centraliza no plano Z

        Matrix4x4 translationMatrix = new Matrix4x4();
        translationMatrix.setIdentity();
        translationMatrix.mat[0][3] = translateX;
        translationMatrix.mat[1][3] = translateY;
        translationMatrix.mat[2][3] = translateZ;

        element.transform(translationMatrix);

    }

    private Element createCube(float x, float y, float z, float size) {
        List<Triangulo3D> triangulos = new ArrayList<>();

        Ponto3D p1 = new Ponto3D(x, y, z);
        Ponto3D p2 = new Ponto3D(x + size, y, z);
        Ponto3D p3 = new Ponto3D(x + size, y + size, z);
        Ponto3D p4 = new Ponto3D(x, y + size, z);
        Ponto3D p5 = new Ponto3D(x, y, z + size);
        Ponto3D p6 = new Ponto3D(x + size, y, z + size);
        Ponto3D p7 = new Ponto3D(x + size, y + size, z + size);
        Ponto3D p8 = new Ponto3D(x, y + size, z + size);

        // Frente
        triangulos.add(new Triangulo3D(p1, p2, p3));
        triangulos.add(new Triangulo3D(p1, p3, p4));

        // Trás
        triangulos.add(new Triangulo3D(p5, p6, p7));
        triangulos.add(new Triangulo3D(p5, p7, p8));

        // Laterais
        triangulos.add(new Triangulo3D(p1, p5, p6));
        triangulos.add(new Triangulo3D(p1, p6, p2));
        triangulos.add(new Triangulo3D(p4, p8, p7));
        triangulos.add(new Triangulo3D(p4, p7, p3));

        return new Element(triangulos);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Define o fundo da tela
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int squareSize = Math.min(getWidth(), getHeight()) - 100;
        int xMin = (getWidth() - squareSize) / 2;
        int yMin = (getHeight() - squareSize) / 2;
        int xMax = xMin + squareSize;
        int yMax = yMin + squareSize;

        clipper = new CohenSutherland(xMin, yMin, xMax, yMax);

        g.setColor(Color.ORANGE);
        g.drawRect(clipper.getXMin(), clipper.getYMin(), clipper.getXMax() - clipper.getXMin(), clipper.getYMax() - clipper.getYMin());

        g.setColor(Color.GRAY);
        for (Element element : elements) {
            for (Triangulo3D triangulo : element.getTriangulos()) {
                drawTriangle(g, triangulo);
            }
        }

        if (focusX != -1 && focusY != -1) {
            g.setColor(Color.RED);
            g.fillRect(focusX - 2, focusY - 2, 5, 5);
        }

        g.setColor(Color.BLACK);
        g.drawString("FPS: " + fps, 10, 20);
    }

    private void drawTriangle(Graphics g, Triangulo3D triangle) {
        Ponto3D p1 = transformPoint(triangle.p1, viewMatrix);
        Ponto3D p2 = transformPoint(triangle.p2, viewMatrix);
        Ponto3D p3 = transformPoint(triangle.p3, viewMatrix);

        p1 = projectPoint(p1);
        p2 = projectPoint(p2);
        p3 = projectPoint(p3);

        int[] p1Clipped = {(int) p1.x, (int) p1.y};
        int[] p2Clipped = {(int) p2.x, (int) p2.y};
        int[] p3Clipped = {(int) p3.x, (int) p3.y};

        if (clipper.clipLine(p1Clipped, p2Clipped)) {
            g.drawLine(p1Clipped[0], p1Clipped[1], p2Clipped[0], p2Clipped[1]);
        }
        if (clipper.clipLine(p2Clipped, p3Clipped)) {
            g.drawLine(p2Clipped[0], p2Clipped[1], p3Clipped[0], p3Clipped[1]);
        }
        if (clipper.clipLine(p3Clipped, p1Clipped)) {
            g.drawLine(p3Clipped[0], p3Clipped[1], p1Clipped[0], p1Clipped[1]);
        }
    }

    private Ponto3D transformPoint(Ponto3D point, Matrix4x4 matrix) {
        float[] transformed = matrix.multiply(point.toArray());
        return new Ponto3D(transformed[0] / transformed[3], transformed[1] / transformed[3], transformed[2] / transformed[3]);
    }

    private Ponto3D projectPoint(Ponto3D point) {
        float[] projected = projectionMatrix.multiply(point.toArray());
        if (projected[3] == 0) projected[3] = 1; // Evita divisão por zero
        float x = (projected[0] / projected[3]) + (getWidth() / 2.0f);
        float y = (projected[1] / projected[3]) + (getHeight() / 2.0f);
        float z = projected[2] / projected[3];
        return new Ponto3D(x, y, z);
    }
}
