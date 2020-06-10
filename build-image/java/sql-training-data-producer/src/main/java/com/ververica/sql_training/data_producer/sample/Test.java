package com.ververica.sql_training.data_producer.sample;

import java.io.*;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

/**
 * @author Snowden
 * @date 2020/6/10 22:57
 * @description TODO
 */
public class Test {
    public static void main(String[] args) throws ExecutionException, IOException {
        HashSet<Integer> integers = new HashSet<>();
        BufferedReader bufferedReaderUserProfileSub = new BufferedReader(new FileReader(new File(args[0])));
        BufferedReader bufferedReaderBehaviorLog = new BufferedReader(new FileReader(new File(args[1])));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(args[2])));
        String line = null;
        int number = 0 ;
        while ((line = bufferedReaderUserProfileSub.readLine())!=null){
            if(number == 0){
                number+=1;
                continue;
            }else {
                integers.add( Integer.valueOf(line.split(",")[0]));
            }
        }
        int number2 = 0 ;
        String line2 = null;
        while ((line2 = bufferedReaderBehaviorLog.readLine())!= null){
            if(number2 == 0){
                number2+=1;
                continue;
            }else {
                if(integers.contains( Integer.valueOf(line2.split(",")[0]))){
                    bufferedWriter.write(line2);
                    bufferedWriter.write("\n");
                }
                number2+=1;
                if(number2 % 1000000 == 0){
                    System.out.println(number2);
                    bufferedWriter.flush();
                }
            }
        }
        bufferedReaderUserProfileSub.close();
        bufferedReaderBehaviorLog.close();
        bufferedWriter.close();
    }
}
