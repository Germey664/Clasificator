package functions;

//Данный класс описывает исключение, выбрасываемое при попытке добавления или изменения точки функции несоответствующим образом.
public class InappropriateFunctionPointException extends Exception {

    //кординады вызвавшие ошибку
    private double x;

    public InappropriateFunctionPointException()
    {

    }

    public InappropriateFunctionPointException(String message, double x)
    {
        super(message);
        this.x = x;
    }

    public double getX()
    {
        return x;
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    @Override
    public String getMessage()
    {
        return super.getMessage()+" для запрашиваемого аргумента ("+x+")";
    }
}