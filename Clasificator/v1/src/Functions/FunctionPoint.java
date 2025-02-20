package Functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private double x, y;
    public FunctionPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point){
        x = point.x;
        y = point.y;
    }
    public FunctionPoint(){
        x = 0;
        y = 0;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    public static double getValueOnLine(FunctionPoint left, FunctionPoint right, double x){
        if(Double.compare(x, left.getX()) == -1 || Double.compare(x, right.getX()) ==1)
        {
            return Double.NaN;
        }
        double x0=left.getX();
        double y0=left.getY();
        double dx=right.getX()-x0;
        double dy=right.getY()-y0;
        return (dy/dx)*(x-x0)+y0;
    }

    @Override
    public String toString()
    {
        return "( "+x+" ; "+y+" )";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || obj.getClass() != getClass())
            return false;
        FunctionPoint objPoint =  (FunctionPoint) obj;
        if (Double.compare(objPoint.getX(), x)!=0 || Double.compare(objPoint.getY(), y)!=0){
            return false;
        }
        return true;
    }
    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + (int) (Double.doubleToLongBits(x) | (Double.doubleToLongBits(x) >>> 32));
        result = 31 * result + (int) (Double.doubleToLongBits(y) | (Double.doubleToLongBits(y) >>> 32));
        return (result*31);
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint clone = new FunctionPoint(x,y);
        return clone;
    }
}
