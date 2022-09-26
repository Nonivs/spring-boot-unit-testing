package tdd;

import com.luv2code.junitdemo.tdd.FizzBuzz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FizzBuzzTest {

    FizzBuzz fizzBuzz;

    //Se o numero for divizivel por 3, fizz
    //Se o numero for divizivel por 5, buzz
    //Se o numero for divizivel por 3 e por 5, fizzbuzz
    //Else, numero

    @BeforeEach
    void setUpBeforeEach(){

        fizzBuzz = new FizzBuzz();

    }

    @Order(1)
    @Test
    void testForDivisibleBy3(){


        String expected = "Fizz";

        assertEquals(expected, fizzBuzz.compute(3));

    }

    @Order(2)
    @Test
    void testForDivisibleBy5(){

        String expected = "Buzz";

        assertEquals(expected, fizzBuzz.compute(5));
    }

    @Order(3)
    @Test
    void testForDivisibleBy3or5(){

        String expected = "FizzBuzz";

        assertEquals(expected, fizzBuzz.compute(15));

    }

    @Order(4)
    @Test
    void testForNotDivisibleBy3and5(){

        int a = 7;
        String expected = String.valueOf(a);

        assertEquals(expected, fizzBuzz.compute(a));
//        assertNotEquals("Fizz", fizzBuzz.compute(7));
//        assertNotEquals("Buzz", fizzBuzz.compute(7));
//        assertNotEquals("FizzBuzz", fizzBuzz.compute(7));


    }

    @Order(4)
    @ParameterizedTest(name = "value={0}, expected={1}")
    @CsvSource({
            "1,1",
            "2,2",
            "3,Fizz",
            "5,Buzz",
            "15,FizzBuzz"
    })
    void testForNotDivisibleBy3and5(int a, String expected){

        assertEquals(expected, fizzBuzz.compute(a));

    }

}
