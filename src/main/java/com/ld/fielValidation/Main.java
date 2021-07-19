package com.ld.fielValidation;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No input");
        }

        for (String s : args) {
            System.out.println(s);
        }
    }
}
