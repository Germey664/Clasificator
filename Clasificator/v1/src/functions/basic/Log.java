package functions.basic;

import functions.Function;

public class Log implements Function {
    private double e;
    /**e - основание логарифма **/
    public Log(double e){
        this.e = e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getE() {
        return e;
    }

    @Override
    public double getLeftDomainBorder() {
        return 0;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.log(x)  /  Math.log(e);
    }
}
