import Classification.ArrayNode;
import functions.TabulatedFunction;

import Classification.Dataset;
import Classification.TeClassification;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    /** Случайное число от min до max;*/
    public static int getRanNumber(int min, int max)
    {
        return (int) (Math.random() * (max+1 - min) + min);
    }
    /** Тестирование работы алгоритма.*/
    public static void  Test(double[][] dataBase, TabulatedFunction[] parametr){
        double Ver; //Вероятность
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
    public static void initDataSet(double[][] dataset, int[] arrayCountCombination){
        int v1, v2, v3, v4, v5;
        int surv;
        for(int i =  0; i < dataset.length; i++){
            v1 = getRanNumber(1,6);
            v2 = getRanNumber(1,6);
            if(v2 == 4) v2 = 3;
            v3 = getRanNumber(-3,3);
            v4 = getRanNumber(1,2);
            v5 = getRanNumber(1,1);
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
            int[] combination = new int[arrayCountCombination.length];
            combination[0] = surv;
            dataset[i][5+ TeClassification.getCombinationOption(arrayCountCombination,combination)] += 1;


        }
    }
    public static void main(String[] args) throws CloneNotSupportedException {
        int countParam = 5;
        int arrayCountCombination[] = new int[]{2};//сколько значений может принимать действие
        int countCombination = TeClassification.getCountCombinationOption(arrayCountCombination);
        /* Массив хранит в себе записи о произведенных действиях. [i][] - номер действия [][i] параметр + действие(5+1)=6*/
        double[][] dataset = new double[5][countParam+countCombination];
        initDataSet(dataset, arrayCountCombination);
        /*
        [] [{p1, p2, p3, p4, p5}{com1, com2, com3, com4, ..., comN}];
        N = UniqueValue1 * UniqueValue1 * ...; Количество комбинаций. Уникальное значения действия 1  * Уникальное значения действия 2 ...
         int a = 1;
         int com = option1;//Выбор действия
         for()
            com += a*optionI;
            a *= uniqueValueI;*/

        TeClassification teClassification = new TeClassification(countParam, countCombination);
        dataset = teClassification.SummaryArray(dataset);//Преобразование массива с данными. Добавляет новые поля.
        //System.out.println("Array: \n"+FunctionsArray.getStringArray(dataset,5));
        Dataset dataset1 = new Dataset(countParam, arrayCountCombination);
        System.out.println(dataset1);
        ArrayNode node1 = new ArrayNode(20,9);
        ArrayNode node2 = new ArrayNode(25,9);
        ArrayNode node3 = new ArrayNode(30,9);
        ArrayNode node4 = new ArrayNode(35,9);
        node1.setArray(dataset);
        node3.setArray(dataset);
        dataset1.addNodeToTail(node1);
        dataset1.addNodeToTail(node2);
        dataset1.addNodeToTail(node3);
        System.out.println(dataset1.searchIndexByNode(node1));
        System.out.println(dataset1);

        //System.out.println(dataset1);


        /* Зависимость вероятности от значения. [i][j] i - количество параметров. j - количество возможных значений*/
        //TabulatedFunction[][] parmSurvVer = new LinkedListTabulatedFunction[countCombination][countParam];
        //teClassification.getAnalyze(dataset,0,parmSurvVer);


        /* Нормализованное значение вероятности*/
        //TabulatedFunction[] AmplitudeParm = new TabulatedFunction[5];
        /*
        teClassification.getAnalyze(dataset,0, parmSurvVer);
        teClassification.getAnalyze(dataset,1, parmSurvVer);
        teClassification.getAnalyze(dataset,2, parmSurvVer);
        teClassification.getAnalyze(dataset,3, parmSurvVer);
        teClassification.getAnalyze(dataset,4, parmSurvVer);

        */
        /*AmplitudeParm[0] = analyze(parmSurvVer[0]);
        AmplitudeParm[1] = analyze(parmSurvVer[1]);
        AmplitudeParm[2] = analyze(parmSurvVer[2]);
        AmplitudeParm[3] = analyze(parmSurvVer[3]);
        AmplitudeParm[4] = analyze(parmSurvVer[4]);
        double[][] datasetTest = new double[100][6];
        initDataSet(datasetTest);
        Test(datasetTest, AmplitudeParm);
        */
    }
}