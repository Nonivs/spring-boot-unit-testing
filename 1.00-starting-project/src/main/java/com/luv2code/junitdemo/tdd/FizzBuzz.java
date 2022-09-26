package com.luv2code.junitdemo.tdd;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FizzBuzz {

    //Se o numero for divizivel por 3, fizz
    //Se o numero for divizivel por 5, buzz
    //Se o numero for divizivel por 3 e por 5, fizzbuzz
    //Else, numero


    public static String compute(int i) {

        StringBuilder result = new StringBuilder();

        if (i % 3 == 0) {
            result.append("Fizz");
        }

        if (i % 5 == 0) {
            result.append("Buzz");
        }

        if (result.isEmpty()) {
            result.append(i);
        }

        return result.toString();

//        if(i % 3 == 0 && i % 5 == 0){
//            return "FizzBuzz";
//        }
//
//         if(i % 3 == 0){
//             return "Fizz";
//         }
//
//        if(i % 5 == 0){
//            return "Buzz";
//        }
//        return String.valueOf(i);

    }

}
