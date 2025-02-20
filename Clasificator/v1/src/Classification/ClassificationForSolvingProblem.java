package Classification;

import Classification.StoreDataInMem.DataSetArrDef;
import Testing.TestingGame;

public class ClassificationForSolvingProblem {
    TestingGame testingGame;
    DataParamWR dataParamWR;
    Dataset dataset;

    int countLoop;
    boolean MLWork;

    int lastUsed;
    int scoreUniversal = 0;
    int localScore;
    int localCountLoop;


    public ClassificationForSolvingProblem(TestingGame testingGame){
        this.testingGame = testingGame;
        dataParamWR = new DataParamWR(testingGame.getCountParam());
        dataset = new DataSetArrDef(100,testingGame.getCountParam());
    }
    public void initLoop(){
        MLWork = true;
        countLoop = 0;
        init();
        while (MLWork)
            update();
    }
    private void init(){
        localScore = 0;
        localCountLoop = 0;
        testingGame.init();
    }
    public void update(){
        countLoop++;
        if(countLoop > 10)
            MLWork = false;

        testingGame.getDataParam(dataParamWR);
        localScore = testingGame.getScore();
        localCountLoop++;
        System.out.println("S: "+ localScore + ", " + localCountLoop + " : " + (localScore*1.0)/localCountLoop);
        dataset.addOperationToTail(dataParamWR.readAllData());
        System.out.println(dataset.getString());
        int status = testingGame.game(0,0,0);
        if(status == -2) init();
    }

}
