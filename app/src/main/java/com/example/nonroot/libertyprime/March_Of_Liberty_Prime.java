package com.example.nonroot.libertyprime;

import java.lang.reflect.Field;

/**
 * Created by nonroot on 11/19/2016.
 */

public class March_Of_Liberty_Prime {

    public int[] fill_Arrays(int returnArraySize, int fieldSize){

        Field foo[] = R.raw.class.getDeclaredFields();
        int borInd = 0;
        int array[] = new int[returnArraySize];
        try{
            for (int i = returnArraySize; i < fieldSize; i++){
                array[borInd] = foo[i].getInt(null);
                borInd++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return array;
    }


}
