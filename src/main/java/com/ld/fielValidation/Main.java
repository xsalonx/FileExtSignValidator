package com.ld.fielValidation;

public class Main {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No input");
            return;
        }
        for (String path : args) {
            FileExtSignValidatable file = new FileExtSignValidatable(path);
            System.out.println(file.validate());

        }
    }
}
