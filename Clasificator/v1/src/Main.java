import Classification.ClassificationForSolvingProblem;
import Classification.DataParamWR;
import Classification.Dataset;
import Classification.StoreDataInMem.DataSetArrDef;
import Testing.ManualGame;
import Testing.Random.RandomNumber;
import Testing.Sapeur.Sapeur;
import Functions.TabulatedFunction;

import Classification.TeClassification;
import Testing.TestingGame;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException, IOException {

        RandomNumber random = new RandomNumber(10);

        TestingGame testingGame = (TestingGame) random;
        //ManualGame manualGame = (ManualGame) ;
        ClassificationForSolvingProblem classificationForSolvingProblem
                = new ClassificationForSolvingProblem(testingGame);
        classificationForSolvingProblem.initLoop();
    }
}