package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function a;
    private double shiftX;
    private double shiftY;

    public Shift(Function a, double shiftX, double shiftY){
        this.a = a;
        this.shiftX =  shiftX;
        this.shiftY = shiftY;
    }
    @Override
    public double getLeftDomainBorder() {
        return a.getLeftDomainBorder()+shiftX;
    }

    @Override
    public double getRightDomainBorder() {
        return a.getRightDomainBorder()+shiftX;
    }

    @Override
    public double getFunctionValue(double x) {
        return a.getFunctionValue(x+shiftX)+shiftY;
    }

    public Function getA() {
        return a;
    }

    public void setA(Function a) {
        this.a = a;
    }

    public void setShiftX(double shiftX) {
        this.shiftX = shiftX;
    }

    public double getShiftX() {
        return shiftX;
    }

    public void setShiftY(double shiftY) {
        this.shiftY = shiftY;
    }

    public double getShiftY() {
        return shiftY;
    }
}
