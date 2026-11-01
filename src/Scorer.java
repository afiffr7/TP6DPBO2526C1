public class Scorer {
    private int posX;
    private int VeloX;

    public Scorer(int posX, int veloX) {
        this.posX = posX;
        VeloX = veloX;
    }

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
