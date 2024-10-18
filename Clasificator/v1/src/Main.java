//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static int getRanNumber(int min, int max)
    {
        return (int) (Math.random() * (max+1 - min) + min);
    }
    public static int getAnalyze(int[][] dataset, int n, int k, double[][] parmVer){
        int[] dataDead= new int[k];
        int[] dataSurv= new int[k];
        for(int i = 0; i < dataset.length; i++ ){
            if(dataset[i][dataset[0].length-1] == 1){
                dataSurv[dataset[i][n]-1]++;
            }else{
                dataDead[dataset[i][n]-1]++;
            }
        }
        for(int i = 0; i < k; i++) {
            int dataAll = dataDead[i] + dataSurv[i];
            double dead = (dataDead[i] / 1.0d / dataAll);
            dead = ((int)(dead* 100))/100d;
            double surv = dataSurv[i] / 1.0d / dataAll;
            surv = ((int)(surv * 100))/100d;
            parmVer[n][i] = surv;
            System.out.print("["+(i+1)+"]:"+dataAll+" "+surv+"/"+dead+" ");
        }
        System.out.println();
        return 0;
    }
    public static void analyze(double[] parametr, double[] AmplitudeParm){
        double max = parametr[0];
        double min = parametr[0];
        for(int i = 0; i < parametr.length; i++){
            if(parametr[i] > max)  max = parametr[i];
            if(parametr[i] < min) min = parametr[i];
        }

        for(int i = 0; i < parametr.length; i++) {
            AmplitudeParm[i] = parametr[i] / max;
            AmplitudeParm[i] = ((int)(AmplitudeParm[i]* 100))/100d;
            System.out.print(AmplitudeParm[i]+" ");
        }
        System.out.println();
    }
    public static void  Test(int[][] dataBase, double[][] parametr){
        double Ver = 1;
        int errors = 0;
        int correctPlus = 0;
        int allPlus = 0;

        for(int i = 0; i < dataBase.length; i++){
            Ver = 1;
            boolean check = true;
            for(int i2 = 0; i2 < parametr.length; i2++){
                Ver *= parametr[i2][dataBase[i][i2]-1];
            }
            if(dataBase[i][dataBase[0].length - 1] != 0 || Ver > 0.01) {
                if (dataBase[i][dataBase[0].length - 1] == 1)
                    allPlus++;
                if ((Ver >= 0.3 && dataBase[i][dataBase[0].length - 1] == 0) || (Ver < 0.3 && dataBase[i][dataBase[0].length - 1] == 1)) {
                    errors++;
                    check = false;
                }
                if (Ver >= 0.3 && dataBase[i][dataBase[0].length - 1] == 1) {
                    correctPlus++;
                }
                System.out.println("Жив или мертв " + Ver + " " + dataBase[i][dataBase[0].length - 1]+ " "+ (check));
                System.out.print("");
            }
        }
        System.out.println("Errors/All "+ errors +" " + dataBase.length+ " " + correctPlus+" "+ allPlus );

    }
    public static void initDataSet(int[][] dataset){
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
    public static void main(String[] args) {
        int[][] dataset = new int[1000][6];
        initDataSet(dataset);
        double[][] parmSurvVer = new double[5][6];
        double[][] AmplitudeParm = new double[5][6];
        getAnalyze(dataset,0,6, parmSurvVer);
        getAnalyze(dataset,1,6, parmSurvVer);
        getAnalyze(dataset,2,6, parmSurvVer);
        getAnalyze(dataset,3,2, parmSurvVer);
        getAnalyze(dataset,4,6, parmSurvVer);

        analyze(parmSurvVer[0], AmplitudeParm[0]);
        analyze(parmSurvVer[1], AmplitudeParm[1]);
        analyze(parmSurvVer[2], AmplitudeParm[2]);
        analyze(parmSurvVer[3], AmplitudeParm[3]);
        analyze(parmSurvVer[4], AmplitudeParm[4]);
        int[][] datasetTest = new int[100][6];
        initDataSet(datasetTest);
        Test(datasetTest, AmplitudeParm);
    }
}