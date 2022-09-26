package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


//@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
public class DemoUtilsTest {

    DemoUtils demoUtils;

    //MUST BE STATIC
    @BeforeAll
    static void setUpBeforeAll(){

        System.out.println("Startin tests - @BeforeAll");
        System.out.println();

    }


    //MUST BE STATIC
    @AfterAll
    static void tearDownAfterAll(){

        System.out.println("Finished all tests - @AfterAll");

    }

    @BeforeEach
    void setUpBeforeEach(){

        demoUtils = new DemoUtils();
        System.out.println("demoUtils initialized - @BeforeEach");

    }

    @AfterEach
    void tearDownAfterEach(){

        System.out.println("@AfterEach");
        System.out.println();

    }

    @Test
    //@DisplayName("Equals and Not Equals Test for method add()")
    @Disabled("This test is desable")
    void testEqualsAndNotEquals(){

        //SETUP
        int expected = 6;
        int unExpected = 7;

        //ASSERT
        System.out.println("Asserts Equals Test - Add method");
        assertEquals(expected, demoUtils.add(2,4), "2 + 4 must be 6");
        System.out.println("Asserts NOT Equals Test - Add method");
        assertNotEquals(unExpected, demoUtils.add(2,4), "2 + 4 must not be 7");

    }

    @Test
    //@DisplayName("Null and Not Null Test for method checkNull()")
    void testNullAndNotNull(){

        //SETUP
        Object obj1 = null;
        Object obj2 = new Object();

        //ASSERT
        System.out.println("Asserts Null - checkNull method");
        assertNull(demoUtils.checkNull(obj1), "Object must be null");
        System.out.println("Asserts Not Null - checkNull method");
        assertNotNull(demoUtils.checkNull(obj2), "Object must not be null");

    }

    @Test
    void testSameAndNotSame(){

        String str = "Luv2Code";

        System.out.println("Asserts Same");
        assertSame(demoUtils.getAcademy(), demoUtils.getAcademy(), "Should be the same");
        System.out.println("Asserts Not Same");
        assertNotSame(str, demoUtils.getAcademy(), "Should not be the same");

    }

    @Test
    void testTrueAndFalse(){

        int n1 = 50;
        int n2 = 10;

        System.out.println("Asserts True");
        assertTrue(demoUtils.isGreater(n1,n2), "Should be true");
        System.out.println("Asserts False");
        assertFalse(demoUtils.isGreater(n2,n1), "Should be false");

    }

    @Test
    void testArrayEquals(){

        String[] a = {"A", "B", "C"};

        System.out.println("Asserts Array Equals");
        assertArrayEquals(a, demoUtils.getFirstThreeLettersOfAlphabet());

    }

    @Test
    void testIterableEquals (){

        List<String> b = List.of("luv", "2", "code");

        System.out.println("Asserts Iterable Equals");
        assertIterableEquals(b, demoUtils.getAcademyInList());

    }

    @Test
    void testLinesEquals (){

        List<String> b = List.of("luv", "2", "code");

        System.out.println("Asserts Lines Equals");

        assertLinesMatch(b, demoUtils.getAcademyInList());

    }

    @Test
    void testThrownException () throws Exception {

        System.out.println("Asserts Throws Exception");
        assertThrows(Exception.class,() -> demoUtils.throwException(-1));

        System.out.println("Asserts Doesn't Throws Exception");
        assertDoesNotThrow(() -> demoUtils.throwException(0));

    }



}
