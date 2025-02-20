package Classification;

import Functions.FunctionPoint;
import Functions.InappropriateFunctionPointException;
import Functions.LinkedListTabulatedFunction;
import Functions.TabulatedFunction;

public class TeClassification {
    private final int COUNT_MINIMAL_UNIQUE_VALUE = 3;
    private final int ALTER_INFINITY = 1000000;

    private int countParam;
    private int countCombination;
    public TeClassification(int countParam, int countCombination){
        this.countParam = countParam;
        this.countCombination = countCombination;
    }
    /** Анализ зависимости вероятности выживания от значения параметра;
     * dataset[i][j] - набор действий количеством i. Параметров + результат = j;
     * Результат вида суммарное количество применений комбинации параметров, ...
     * m - номер анализируемого комбинации;
     * paramVer[][] - вернет полученную зависимость;*/
    public void getAnalyze(double[][] dataset, int m, TabulatedFunction[][] paramVer){
        /*Если значение одно, то функция, константа
        * На границах определения функция всегда прямая
        * Точка считается таковой если, ее параметр встречался больше минимального количество раз
        * Если точка таковая, то для любого уникального значения можно построить функцию*/
        paramVer[1][0] = getAnalyzeToValue(dataset,0,countParam + 2 * 1);//Комбинация номер 1
        System.out.println(paramVer[1][0].toString());
        paramVer[1][1] = getAnalyzeToValue(dataset,1,countParam + 2 * 1);
        System.out.println(paramVer[1][1].toString());
        paramVer[1][2] = getAnalyzeToValue(dataset,2,countParam + 2 * 1);
        System.out.println(paramVer[1][2].toString());
        paramVer[1][3] = getAnalyzeToValue(dataset,3,countParam + 2 * 1);
        System.out.println(paramVer[1][3].toString());
        paramVer[1][4] = getAnalyzeToValue(dataset,4,countParam + 2 * 1);
        System.out.println(paramVer[1][4].toString());


    }

    /** Анализ зависимости вероятности выживания от значения параметра;
     * dataset[i][j] - набор действий количеством i. Параметров + результат = j;
     * Результат вида суммарное количество применений комбинации параметров, ...
     * n - номер анализируемого параметра;
     * m - номер анализируемого значения;
     * parmVer[][] - вернет полученную зависимость;*/
    public TabulatedFunction getAnalyzeToValue(double[][] dataset, int n,int m){

        double[] arrayUniqueNumber = getParametr(dataset, n);
        //arrayUniqueNumber = FindDuplicate(arrayUniqueNumber,COUNT_MINIMAL_UNIQUE_VALUE); // Поиск полезных точек
        arrayUniqueNumber = FindDiffValue(arrayUniqueNumber);// Уникальные значение параметра

        int countUniqueNumber = arrayUniqueNumber.length;
        FunctionsArray.SortArray(arrayUniqueNumber);

        double[][] arrayPoints = new double[countUniqueNumber][3]; //Количество раз которое выжил при значении x
        for (int i = 0; i < countUniqueNumber; i++) {//Заполняем один из графиков его уникальными значения параметра
            arrayPoints[i][0] = arrayUniqueNumber[i];
        }
        for (int j = 0; j < dataset.length; j++) {
            for (int i = 0; i < countUniqueNumber; i++) {
                if (Double.compare(dataset[j][n], arrayPoints[i][0]) == 0) {//Если нашел параметр из действия
                    for(int iOption = countParam; iOption < dataset[0].length; iOption+=2){
                        arrayPoints[i][1] += dataset[j][iOption];
                    }
                    // Считаем сколько раз произошла активация этого значения при этом параметре
                    arrayPoints[i][2] += dataset[j][m+1];
                }
            }
        }
        FunctionPoint[] pointsResults = new FunctionPoint[countUniqueNumber];
        System.out.println("Get analyze");
        for(int i = 0; i < countUniqueNumber; i++) {
            double dataAll = arrayPoints[i][1]; //Сумма всех активаций
            double alive = arrayPoints[i][2] / dataAll;
            pointsResults[i] = new FunctionPoint(arrayUniqueNumber[i], alive);
        }
        TabulatedFunction parmVer;
        if(countUniqueNumber == 1){//Параметр константа
            parmVer = new LinkedListTabulatedFunction(new FunctionPoint[]{
                    new FunctionPoint(-ALTER_INFINITY,0),
                    new FunctionPoint(pointsResults[0]),
                    new FunctionPoint(ALTER_INFINITY,0)
            });
        }else{
            parmVer = new LinkedListTabulatedFunction(pointsResults);
            try {
                parmVer.addPoint(new FunctionPoint(-ALTER_INFINITY,0));
                parmVer.addPoint(new FunctionPoint(ALTER_INFINITY,0));
            } catch (InappropriateFunctionPointException e) {
                throw new RuntimeException(e);
            }
        }

        return parmVer;

    }
    double[] getParametr(double[][] dataset, int n){
        double[] arrayResult = new double[dataset.length];
        for(int i = 0; i < arrayResult.length; i++){
            arrayResult[i] = dataset[i][n];
        }
        return arrayResult;
    }
    /**Поиск уникальных значений из массива array[]*/
    double[] FindDiffValue(double[] array){
        int countDifNumber = 0; // количество найденных уникальных цифр
        double[] difNumber = new double[array.length];
        boolean check;
        for(int j = 0; j < array.length; j++){
            check = true;
            for(int i = countDifNumber - 1; i >= 0; i--){
                if(Double.compare(array[j],difNumber[i]) == 0)
                    check = false;
            }
            if(check) { //Добавляем новое число
                difNumber[countDifNumber] = array[j];
                countDifNumber++;
            }
        }
        double[] arrayResult = new double[countDifNumber];
        for (int i = 0; i < countDifNumber; i++)
            arrayResult[i] = difNumber[i];
        return arrayResult;
    }
    /** Поиск значений которые повторятся больше k раз*/
    double[] FindDuplicate(double[] array, int k){
        double[] arrayDupValue = new double[array.length];
        int countDupValue = 0;

        int countValue = 0;
        double valueNow = 0;
        for(int j = 0; j < array.length; j++){
            countValue = 0;
            valueNow = array[j];
            for(int i = j; i < array.length; i++){
                if(Double.compare(valueNow,array[i]) == 0)
                    countValue++;
            }
            if(countValue > k){
                arrayDupValue[countDupValue] = valueNow;
                countDupValue++;
            }
        }
        double[] arrayResult = new double[countValue];
        for (int i = 0; i < countValue; i++){
            arrayResult[i] = arrayDupValue[i];
        }
        return  arrayResult;
    }

    /** Выполняет преобразование массива действий в массив
     * с суммаризацией результатов по типу комбинаций параметров;
     * [параметры][комбинации {сумма}{положительная1};{сумма2}{положительная2}]*/
    public double[][] SummaryArray(double[][] dataset){
        int countOptions = dataset[0].length - countParam;// количество комбинаций записанных в массив
        double[][] array = new double[dataset.length][countParam + countOptions*2];
        int countActiveArray = 0;
        /*В array будут записаны все уникальные поп параметрам операции. Вместо единичной активации.
        *Будут храниться 2 новых опции. Сумма всех активаций и только положительные.
        */
        for(int iSet = 0; iSet < dataset.length; iSet++){//Перебор по всем операциям
            boolean checkFind = false;//Нашли такую же в уже занесенных?
            for(int iArray = 0; iArray < countActiveArray; iArray++){//Перебор по операциям уже занесенным
                boolean checkParamters = true;//Они одинаковы?
                for(int iParametr = 0; iParametr < countParam; iParametr++){//Сравнение параметров
                    if(Double.compare(dataset[iSet][iParametr], array[iArray][iParametr]) != 0){
                        checkParamters = false;
                        break;
                    }
                }
                if(checkParamters) {//Операция уже занесена. dataset[iSet] == array[iArray]
                    checkFind = true;
                    for(int i = 0; i  < countOptions; i++){//Добавляем сумму
                        array[iArray][i*2+ countParam] += dataset[iSet][i+ countParam];//Сумма
                        if(Double.compare(dataset[iSet][i+ countParam],0) > 0)
                            array[iArray][i*2+ countParam +1] += dataset[iSet][i+ countParam];//Положительная часть
                    }
                    break;
                }
            }
            if(!checkFind){//Добавляем новую операцию в массив
                for(int i = 0; i  < countParam; i++){//копируем параметры
                    array[countActiveArray][i]= dataset[iSet][i];
                }
                for(int i = 0; i  < countOptions; i++){//Добавляем сумму
                    array[countActiveArray][i*2+ countParam] += dataset[iSet][i+ countParam];//Сумма
                    if(Double.compare(dataset[iSet][i+ countParam],0) > 0)
                        array[countActiveArray][i*2+ countParam +1] += dataset[iSet][i+ countParam];//Положительная часть
                }
                countActiveArray++;
            }
            //Операция уже занесена

        }
        double[][] arrayResult = new double[countActiveArray][countParam +countOptions*2];
        for(int i = 0; i < countActiveArray; i++)
            arrayResult[i] = array[i];
        return arrayResult;
    }


    public static int getCountCombinationOption(int[] arrayCountCombination){
        int combination = 1;
        for(int i = 0; i  < arrayCountCombination.length; i++){
            combination *= arrayCountCombination[i];
        }
        return combination;
    }
    public static int getCombinationOption(int[] arrayCountCombination, int[] arrayCombination){
        int combination = 1;
        int m = 1;
        for(int i = 0; i  < arrayCountCombination.length; i++){
            combination *= m * arrayCombination[i]; // 01 + o2*s1 + o3*s1*s2
            m *= arrayCountCombination[i];
        }
        return combination;
    }
    /** Растягивает амплитуду до 1;
     * parametr - входное значение;
     * AmplitudeParm - на выход;*/

    public TabulatedFunction analyze(TabulatedFunction parametr) throws CloneNotSupportedException {
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
}
