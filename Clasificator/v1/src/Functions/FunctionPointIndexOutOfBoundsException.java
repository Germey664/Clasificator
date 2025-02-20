package Functions;

 //Данный класс описывает исключение, получаемое при выходе за границы набора точек при обращении к ним по номеру.
public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {

  private int index;

  public FunctionPointIndexOutOfBoundsException()
  {

  }

  public FunctionPointIndexOutOfBoundsException(String message, int index) {
    super(message);
    this.index = index;
  }

  //Метод получения индекса
  public int getIndex() {
    return index;
  }

  @Override
  public String toString()
  {
    return super.toString();
  }

  @Override
  public String getMessage()
  {
    return super.getMessage()+" для запрашиваемого индекса ("+index+")";
  }
}
