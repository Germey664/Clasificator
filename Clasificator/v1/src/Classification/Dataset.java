package Classification;

public interface Dataset {
    public double[] addOperationToTail(double[] operation);
    public double[] addOperationByIndex( int index, double[] operation);

    public double[] deleteOperationToTail();
    public double[] deleteOperationByIndex(int index);

    public double[] getOperationToTail();
    public double[] getOperationByIndex(int index);

    public double[] setOperationToTail(double[] operation);
    public double[] setOperationByIndex(int index, double[] operation);

    public int getLength();
    public int getLengthBase();

    public String getString();
}
