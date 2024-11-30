package functions;

import java.io.*;
/** Класс отвечает за работу с объектами типа TabulatedFunction;
 * Получение TabulatedFunction из Function;
 * А также сохранение и чтение функций в файлы*/
public class TabulatedFunctions implements Serializable{
    public TabulatedFunctions(){

    }
    /**Получает функцию и возвращает её табулированный аналог на заданном отрезке с заданным количеством точек**/
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (Double.compare(leftX, function.getLeftDomainBorder()) < 0 || Double.compare(rightX, function.getRightDomainBorder()) > 0)
            throw new IllegalArgumentException("Границы табулирования выходят за границы определения функции ("+leftX+","+rightX+") ("+function.getLeftDomainBorder()+","+function.getRightDomainBorder()+")");
        double[] values = new double[pointsCount];
        double delta = (rightX + 1 - leftX) / pointsCount;
        for (int i = 0; i < pointsCount; i++)
            values[i] = function.getFunctionValue(leftX + delta * i);
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }
    /**Метод вывода табулированной функции в байтовый поток**/
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        // Создали байтовый поток для записи данных
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        dataOutputStream.writeInt(function.getPointsCount());

        dataOutputStream.writeDouble(function.getLeftDomainBorder());
        dataOutputStream.writeDouble(function.getRightDomainBorder());
        // Записали значения функции
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOutputStream.writeDouble(function.getPointY(i));
        }
    }
    /**Метод ввода табулированной функции из байтового потока**/
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        // Создали байтовый поток для чтения данных
        DataInputStream dataInputStream=new DataInputStream(in);
        int pointsCount = dataInputStream.readInt();
        double leftX = dataInputStream.readDouble();
        double rightX = dataInputStream.readDouble();

        double[] values = new double[pointsCount];
        for(int i=0;i<pointsCount;i++){
            values[i] = dataInputStream.readDouble();
        }
        return new ArrayTabulatedFunction(leftX,rightX,values);
    }
    /**Метод записи табулированной функции в символьный поток**/
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out){
        // Создали символьный поток вывода
        PrintWriter printWriter = new PrintWriter(out);
        printWriter.print(function.getPointsCount()+" ");
        printWriter.print(function.getLeftDomainBorder()+" "+function.getRightDomainBorder());
        // записали точки функции
        for(int i=0;i<function.getPointsCount();i++){
            printWriter.print(" "+function.getPointY(i));
        }
    }

    /**Метод чтения табулированной функции из символьного потока**/
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException{
        StreamTokenizer tokenizer=new StreamTokenizer(in);
        int currentToken = tokenizer.nextToken();
        if(currentToken != StreamTokenizer.TT_NUMBER)
            throw new IllegalArgumentException("Неверный набор данных в потоке");
        int pointsCount=(int)tokenizer.nval;
        currentToken = tokenizer.nextToken();
        if(currentToken != StreamTokenizer.TT_NUMBER)
            throw new IllegalArgumentException("Неверный набор данных в потоке");
        double leftX=tokenizer.nval;
        currentToken = tokenizer.nextToken();
        if(currentToken != StreamTokenizer.TT_NUMBER)
            throw new IllegalArgumentException("Неверный набор данных в потоке");
        double rightX=tokenizer.nval;

        double[] values=new double[pointsCount];
        for(int i=0;i<pointsCount;i++){
            currentToken = tokenizer.nextToken();
            if(currentToken != StreamTokenizer.TT_NUMBER)
                throw new IllegalArgumentException("Неверный набор данных в потоке");
            values[i]=tokenizer.nval;
        }
        return new ArrayTabulatedFunction(leftX,rightX,values);
    }
}
