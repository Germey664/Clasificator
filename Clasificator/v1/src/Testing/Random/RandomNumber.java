package Testing.Random;

import Classification.DataParamWR;
import Testing.TestingGame;

public class RandomNumber implements TestingGame {
    double[][] dataset;
    int lengthBase  = 5; //Количество параметров.
    int length;//Количество прокруток рулетки(попыток).

    int delta;//Смешение по массиву, текущий исследуемый элемент.
    int answerTrue;//Правильный результат.
    int countAnswer;//Счетчик проверенных ответов.

    /**Проверяет на установку зависимости от случайных параметров.
     * Есть несколько видов игр, поражение в любой из них убивает.**/
    public RandomNumber(int length){
        delta = 0;
        this.length = length;
        dataset = new double[length][lengthBase];
    }

    /** Заполняет массив примером операций. 5 - параметров. 1 - результат;
     * 1 и 2 - сумма костей больше или равно 8 = выжил;
     * 3 - случайный, от него ничего не зависит;
     * 4 - монетка. Выжил или нет;
     * 5 - случайность. Чем больше число тем ниже шансы выжить;*/
    public void init() {
        delta = 0;
        answerTrue =  0;
        countAnswer  =  0;
        for(int i = 0; i < length; i++)
            dataset[i] = loadDataset();
    }
    private double[] loadDataset(){
        double[] d = new double[length];
        d[0] = getRanNumber(1,6);
        d[1] = getRanNumber(1,6);
        if(d[1] == 4) d[1] = 3;
        d[2] = getRanNumber(-3,3);
        d[3] = getRanNumber(1,2);
        d[4] = getRanNumber(1,1);
        return d;
    }

    @Override
    public int getScore() {
        int score = 0;
        score += answerTrue;
        score -= (countAnswer - answerTrue);
        if(delta >=  length && answerTrue > (countAnswer - answerTrue)) score*=2;
        return score;
    }

    @Override
    public int game(int x, int y, int param) {
        if(delta >= length-1)
            return -2;
        double[] d  = dataset[delta];
        int survival = 0;
        if(d[0] + d[1] >= 8) survival = 1; else survival = 0;
        if(d[3] == 1) survival = 0;
        if(d[4] != 1)
            for(int i2 = 0; i2 < d[4]; i2++)
                if(getRanNumber(1,10)==7)
                    survival = 0;
        if(survival == param) answerTrue++;
        countAnswer++;
        delta++;
        return 0;
    }

    @Override
    public String getString() {
        String s = "";
        for(int j = 0; j < length; j++) {
            for (int i = 0; i < lengthBase; i++)
                s += dataset[j][i] + " ";
            s+= "\n";
        }
        return s;
    }

    @Override
    public void getDataParam(Object dataParamWR) {
        DataParamWR _dataParamWR = (DataParamWR) dataParamWR;
        _dataParamWR.write(dataset[delta],lengthBase);
    }

    @Override
    public int getCountParam() {
        return  lengthBase;
    }

    /** Случайное число от min до max;*/
    private static int getRanNumber(int min, int max)
    {
        return (int) (Math.random() * (max+1 - min) + min);
    }
}
