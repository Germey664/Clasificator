package Сlassification;

public class FunctionsArray {

    /** Сортировка переданного массива пузырьком*/
    public static void SortArray(double[] array){
        double buf;
        for (int out = array.length - 1; out >= 1; out--){ //сортировка значений
            for (int i = 0; i < out; i++){
                if(Double.compare(array[i], array[i + 1]) > 0){
                    buf = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = buf;
                }
            }
        }
    }

    public static String getStringArray(double[][] dataset, int paramtetrs){
        String string = "";
        for(int j = 0; j < dataset.length;j++){
            string+=j+":[";
            for(int i = 0; i < paramtetrs; i++){
                string+=dataset[j][i]+"; ";
            }
            string+="] [";
            for(int i = paramtetrs; i < dataset[0].length; i++){
                string+=dataset[j][i]+"; ";
            }
            string+="]\n";
        }
        return string;
    }
    public static String getStringArray(double[][] dataset){
        String string = "";
        for(int j = 0; j < dataset.length;j++){
            string+=j+":[";
            for(int i = 0; i < dataset.length; i++){
                string+=dataset[j][i]+"; ";
            }
            string+="]\n";
        }
        return string;
    }
    public static String getStringArray(double[] dataset, int n){
        String string = "";
        for(int j = 0; j < dataset.length;j++){
            string+=j+":[";
            for(int i = 0; i < n; i++){
                string += dataset[j]+"; ";
            }
            string+="]\n";
        }
        return string;
    }
}
