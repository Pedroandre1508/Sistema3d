# 💻 Software de Computação Gráfica

Este projeto implementa um visualizador 3D interativo de objetos em Java, com suporte a arquivos `.obj`. Ele permite manipulação de modelos tridimensionais em tempo real, incluindo rotação, translação, zoom e carregamento de arquivos externos.

---

## 🚀 Como Executar

Para rodar o programa:

1. **Certifique-se de ter o JDK instalado.**
2. **Compile e execute o main.java**

```bash
javac Main.java
java Main
```

---

## 🧰 Funcionalidades

- Visualização 3D de objetos
- Rotação nos eixos X, Y e Z
- Zoom (ampliar e reduzir)
- Translação (movimentação do objeto)
- Carregamento de arquivos .obj

---

## 🕹️ Guia de Movimentação dos Objetos

| Tecla            | Ação                                                            |
| ---------------- | --------------------------------------------------------------- |
| `W`              | Move os objetos para cima (translação no eixo Y negativa)       |
| `S`              | Move os objetos para baixo (translação no eixo Y positiva)      |
| `A`              | Move os objetos para a esquerda (translação no eixo X negativa) |
| `D`              | Move os objetos para a direita (translação no eixo X positiva)  |
| `Q`              | Rotaciona os objetos no eixo Y no sentido anti-horário          |
| `E`              | Rotaciona os objetos no eixo Y no sentido horário               |
| `C`              | Rotaciona os objetos no eixo X para cima                        |
| `X`              | Rotaciona os objetos no eixo X para baixo                       |
| `↑ (seta cima)`  | Aumenta a escala dos objetos (zoom in)                          |
| `↓ (seta baixo)` | Diminui a escala dos objetos (zoom out)                         |
| `R`              | Reflete os objetos no eixo X                                    |
| `T`              | Reflete os objetos no eixo Y                                    |
| `Y`              | Aplica cisalhamento no eixo X em relação ao Y                   |
| `U`              | Aplica cisalhamento mais complexo com múltiplos parâmetros      |
| `L`              | Limpa todos os objetos da tela (remove todos os elementos)      |

| Botão do Mouse     | Ação                                                                                    |
| ------------------ | --------------------------------------------------------------------------------------- |
| Botão **esquerdo** | Adiciona um cubo 3D no ponto clicado (se estiver dentro da região de recorte `clipper`) |
| Botão **direito**  | Centraliza o foco da cena no ponto clicado (ajusta a matriz de transformação)           |
