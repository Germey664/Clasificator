package functions.meta;

import functions.Function;

public class Sum implements Function {
    private Function a, b;
    public Sum(Function a, Function b){
        this.a = a;
        this.b = b;
    }
    @Override
    public double getLeftDomainBorder() {
        return Math.max(a.getLeftDomainBorder(),b.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        return Math.min(a.getRightDomainBorder(),b.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        return a.getFunctionValue(x) + b.getFunctionValue(x);
    }

    public Function getA() {
        return a;
    }
    public Function getB() {
        return b;
    }

    public void setA(Function a) {
        this.a = a;
    }

    public void setB(Function b) {
        this.b = b;
    }
}
