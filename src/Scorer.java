public class Scorer { // class scorer untuk membantu perhitungan score
    // attribut
    private int posX;
    private int VeloX;

    public Scorer(int posX, int veloX) { // constructor
        this.posX = posX;
        VeloX = veloX;
    }

    // getter dan setter
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getVeloX() {
        return VeloX;
    }

    public void setVeloX(int veloX) {
        VeloX = veloX;
    }
}
