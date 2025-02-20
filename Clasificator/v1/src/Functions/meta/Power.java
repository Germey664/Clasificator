package Functions.meta;

import Functions.Function;

public class Power implements Function {
    private Function a;
    private double b;

    public Power(Function a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double getLeftDomainBorder() {
        return a.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return a.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return Math.pow(a.getFunctionValue(x),b);
    }

    public Function getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public void setA(Function a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }
}
