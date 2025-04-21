package Projeto;

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
    
    public void zera() {
		for(int y = 0; y < 4;y++) {
			for(int x = 0; x < 4;x++) {
				mat[y][x] = 0;
			}
		}
	}

    public void setPerspectiva(double d) {

		zera();
		mat[0][0] = 1;
		mat[0][1] = 0;
		mat[0][2] = 0;
		mat[0][3] = 0;

		mat[1][0] = 0;
		mat[1][1] = 1;
		mat[1][2] = 0;
		mat[1][3] = 0;
		
		mat[2][0] = 0;
		mat[2][1] = 0;
		mat[2][2] = 0;
		mat[2][3] = 0;
		
		mat[3][2] = (float) (1/d);
		mat[3][3] = 1;
	}

    public void setRotationAxis(float ux, float uy, float uz, float angle) {
        setIdentity();
    
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float oneMinusCos = 1 - cos;
    

        float len = (float) Math.sqrt(ux * ux + uy * uy + uz * uz);
        if (len != 0) {
            ux /= len;
            uy /= len;
            uz /= len;
        }
    
        mat[0][0] = cos + ux * ux * oneMinusCos;
        mat[0][1] = ux * uy * oneMinusCos - uz * sin;
        mat[0][2] = ux * uz * oneMinusCos + uy * sin;
    
        mat[1][0] = uy * ux * oneMinusCos + uz * sin;
        mat[1][1] = cos + uy * uy * oneMinusCos;
        mat[1][2] = uy * uz * oneMinusCos - ux * sin;
    
        mat[2][0] = uz * ux * oneMinusCos - uy * sin;
        mat[2][1] = uz * uy * oneMinusCos + ux * sin;
        mat[2][2] = cos + uz * uz * oneMinusCos;
    
        mat[3][3] = 1;
    }

    public float[] multiply(float[] vec) {
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = mat[i][0] * vec[0] + mat[i][1] * vec[1] + mat[i][2] * vec[2] + mat[i][3] * vec[3];
        }
        return result;
    }
    
    public Matrix4x4 multiply(Matrix4x4 other) {
        Matrix4x4 result = new Matrix4x4();
        result.setIdentity(); // Inicializa como identidade para evitar valores residuais
    
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.mat[i][j] = 0; // Inicializa o valor
                for (int k = 0; k < 4; k++) {
                    result.mat[i][j] += this.mat[i][k] * other.mat[k][j];
                }
            }
        }
    
        return result;
    }
}