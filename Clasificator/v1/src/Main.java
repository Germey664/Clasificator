import functions.FunctionPoint;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    /** Случайное число от min до max;*/
    public static int getRanNumber(int min, int max)
    {
        return (int) (Math.random() * (max+1 - min) + min);
    }
    /** Анализ зависимости вероятности выживания от значения параметра;
     * dataset[i][j] - набор действий количеством i. Параметров + результат = j;
     * n - номер анализирумого параметра;
     * k - количество возможных значений параметра;
     * parmVer[][] - вернет полученную зависимость;*/
    public static void getAnalyze(double[][] dataset, int n, TabulatedFunction[] parmVer){
        int countDifNumber = 0;//поиск уникальных значений параметра
        double[] arrayDifNumber = new double[1024];

        for(int j = 0; j < dataset.length; j++){
            boolean check = true;
            for(int i = 0; i <= countDifNumber; i++){
                if(Double.compare(dataset[j][n],arrayDifNumber[i]) == 0)
                    check = false;
            }
            if(check) {
                arrayDifNumber[countDifNumber] = dataset[j][n];
                countDifNumber++;
            }
        }
        for (int out = countDifNumber - 1; out >= 1; out--){ //сортировка значений
            for (int i = 0; i < out; i++){
                if(Double.compare(arrayDifNumber[i], arrayDifNumber[i + 1]) > 0){
                    double buf =  arrayDifNumber[i];
                    arrayDifNumber[i] = arrayDifNumber[i + 1];
                    arrayDifNumber[i + 1] = buf;
                }
            }
        }
        FunctionPoint [] arrayPointsAlive = new FunctionPoint[countDifNumber]; //Количество раз которое выжил при значении x
        FunctionPoint [] arrayPointsDead = new FunctionPoint[countDifNumber]; //Количество раз которое умер при значении x
        for(int i = 0; i < countDifNumber; i++) {
            arrayPointsAlive[i] = new FunctionPoint(arrayDifNumber[i], 0);
            arrayPointsDead[i] = new FunctionPoint(arrayDifNumber[i], 0);
        }

        for (int j = 0; j < dataset.length; j++) {
            for (int i = 0; i < countDifNumber; i++) {
                if (Double.compare(dataset[j][n], arrayPointsAlive[i].getX()) == 0) {//Если значение параметра действия совпало со значением x
                    if (Double.compare(dataset[j][dataset[0].length - 1], 1) == 0) {//Тогда проверяем выжил или умер
                        arrayPointsAlive[i].setY(arrayPointsAlive[i].getY() + 1);
                    } else {
                        arrayPointsDead[i].setY(arrayPointsDead[i].getY() + 1);
                    }
                }
            }
        }
        FunctionPoint[] pointsResults = new FunctionPoint[countDifNumber];
        System.out.println("Get analyze");
        for(int i = 0; i < countDifNumber; i++) {
            double dataAll = arrayPointsAlive[i].getY() + arrayPointsDead[i].getY();
            double dead = arrayPointsDead[i].getY() / dataAll;
            double alive = arrayPointsAlive[i].getY() / dataAll;
            pointsResults[i] = new FunctionPoint(arrayDifNumber[i],alive);
            System.out.print("["+arrayDifNumber[i]+"]:"+dataAll+" "+(float)alive+"/"+(float)dead+" ");
        }
        parmVer[n] = new LinkedListTabulatedFunction(pointsResults);
        System.out.println();
        System.out.println("---------------------");

    }
    /** Растягивает амплитуду до 1;
     * parametr - входное значение;
     * AmplitudeParm - на выход;*/
    public static TabulatedFunction analyze(TabulatedFunction parametr) throws CloneNotSupportedException {
        double max = parametr.getPoint(0).getY();
        double min = parametr.getPoint(0).getY();
        for(int i = 0; i < parametr.getPointsCount(); i++){
            double y = parametr.getPoint(i).getY();
            if(Double.compare(y, max) > 0)  max = y;
            if(Double.compare(y, min) < 0)  min = y;
        }
        FunctionPoint[] pointsResults = new FunctionPoint[parametr.getPointsCount()];
        for(int i = 0; i < parametr.getPointsCount(); i++) {
            pointsResults[i] = new FunctionPoint(parametr.getPoint(i).getX(),parametr.getPoint(i).getY() / max);
            System.out.print(pointsResults[i].getY()+" ");
        }
        TabulatedFunction amplitudeParm = new LinkedListTabulatedFunction(pointsResults);
        System.out.println();
        return  amplitudeParm;
    }
    /** Тестирование работы алгоритма.*/
    public static void  Test(double[][] dataBase, TabulatedFunction[] parametr){
        double Ver = 1; //Вероятность
        int errors = 0; //Ошибки
        int correctPlus = 0; //Количество правильных прогнозов жизни
        int allPlus = 0; // Общие количество прогнозов жизни

        for(int i = 0; i < dataBase.length; i++){
            Ver = 1;
            boolean check = true;
            for(int i2 = 0; i2 < parametr.length; i2++){
                Ver *= parametr[i2].getFunctionValue(dataBase[i][i2]);
            }
            if(Double.compare(dataBase[i][dataBase[0].length - 1], 0) != 0 || Double.compare(Ver, 0.01) > 0) {
                if (dataBase[i][dataBase[0].length - 1] == 1)
                    allPlus++;
                if ((Double.compare(Ver, 0.3) >= 0 && Double.compare(dataBase[i][dataBase[0].length - 1],0) == 0)
                        ||(Double.compare(Ver, 0.3)< 0 && Double.compare(dataBase[i][dataBase[0].length - 1], 1) == 0)) {
                    errors++;
                    check = false;
                }
                if (Double.compare(Ver, 0.3)>=0 && Double.compare(dataBase[i][dataBase[0].length - 1], 1) == 0) {
                    correctPlus++;
                }
                System.out.println("Жив или мертв " + Ver + " " + dataBase[i][dataBase[0].length - 1]+ " "+ (check));
                System.out.print("");
            }
        }
        System.out.println("Errors/All "+ errors +" " + dataBase.length+ " " + correctPlus+" "+ allPlus );

    }
    /** Заполняет массив примером операций. 5 - параметров. 1 - результат;
     * 1 и 2 - сумма костей больше или равно 8 = выжил;
     * 3 - случайный, от него ничего не зависит;
     * 4 - монетка. Выжил или нет;
     * 5 - случайность. Чем больше число тем ниже шансы выжить;*/
    public static void initDataSet(double[][] dataset){
        int v1, v2, v3, v4, v5;
        int surv = 1;
        for(int i =  0; i < dataset.length; i++){
            v1 = getRanNumber(1,6);
            v2 = getRanNumber(1,6);
            v3 = getRanNumber(1,6);
            v4 = getRanNumber(1,2);
            v5 = getRanNumber(1,6);
            if(v1 + v2 >= 8) surv = 1; else surv = 0;
            if(v4 == 1) surv = 0;
            if(v5 != 1)
                for(int i2 = 0; i2 < v5; i2++)
                    if(getRanNumber(1,10)==7)
                        surv = 0;
            //System.out.println(v1+ " " + v2 + " " +  v3 +" "+ v4+ ": " + surv);
            dataset[i][0] = v1;
            dataset[i][1] = v2;
            dataset[i][2] = v3;
            dataset[i][3] = v4;
            dataset[i][4] = v5;
            dataset[i][5] = surv;

        }
    }
    public static void main(String[] args) throws CloneNotSupportedException {
        /** Массив хранит в себе записи о произведенных действиях. [i][] - номер действия [][i] параметр + действие(5+1)=6*/
        double[][] dataset = new double[1000][6];
        initDataSet(dataset);
        /** Зависимость вероятности от значения. [i][j] i - количество параметров. j - количество возможных значений*/
        TabulatedFunction[] parmSurvVer = new LinkedListTabulatedFunction[5];
        /** Нормализованное значение вероятности*/
        TabulatedFunction[] AmplitudeParm = new TabulatedFunction[5];
        getAnalyze(dataset,0, parmSurvVer);
        getAnalyze(dataset,1, parmSurvVer);
        getAnalyze(dataset,2, parmSurvVer);
        getAnalyze(dataset,3, parmSurvVer);
        getAnalyze(dataset,4, parmSurvVer);

        AmplitudeParm[0] = analyze(parmSurvVer[0]);
        AmplitudeParm[1] = analyze(parmSurvVer[1]);
        AmplitudeParm[2] = analyze(parmSurvVer[2]);
        AmplitudeParm[3] = analyze(parmSurvVer[3]);
        AmplitudeParm[4] = analyze(parmSurvVer[4]);
        double[][] datasetTest = new double[100][6];
        initDataSet(datasetTest);
        Test(datasetTest, AmplitudeParm);
    }
}