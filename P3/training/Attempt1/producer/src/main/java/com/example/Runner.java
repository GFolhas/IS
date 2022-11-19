package com.example;

import java.util.Scanner;

public class Runner 
{
    public static void main( String[] args ){
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        while(!s.equals("end")){
            SampleProducer sampleProducer = new SampleProducer(s);
            s = in.nextLine();
        }
        SampleProducer sampleProducer = new SampleProducer(s);
    }
}
