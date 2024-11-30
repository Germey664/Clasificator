package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function a;
    private double scaleX;
    private double scaleY;
    public Scale(Function a, double scaleX, double scaleY){
        this.a = a;
        this.scaleX =  scaleX;
        this.scaleY = scaleY;
    }
    @Override
    public double getLeftDomainBorder() {
        return a.getLeftDomainBorder() * scaleX;
    }

    @Override
    public double getRightDomainBorder() {
        return a.getRightDomainBorder()*scaleX;
    }

    @Override
    public double getFunctionValue(double x) {
        return a.getFunctionValue(x*scaleX)*scaleY;
    }

    public Function getA() {
        return a;
    }

    public void setA(Function a) {
        this.a = a;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }
}
