package edu.ttap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.ttap.spellchecker.SpellChecker;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

/** An example test suite. */
public class ExampleTests {
    /** An example JUnit test. */
    @Test
    public void exampleUnitTest() {
        assertEquals(2, 1 + 1);
    }

    /**
     * An example Jqwik property-based test: for all n, does n * (n-1) / 2 equal
     * the sum of 0, ..., n-1?
     * @param n the argument to test the proprety on
     * @return true iff the property holds for the given argument.
    */
    @Property
    public boolean examplePropertyTest(@ForAll @IntRange(min = 0, max = 10000) int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i;
        }
        return sum == (n * (n - 1)) / 2;
    }

    @Test
    void testIsWord() throws IOException {
    SpellChecker sc = SpellChecker.fromFile("files/words_alpha.txt");
    assertFalse(sc.isWord("hello"));
    assertTrue(sc.isWord("apple"));
    assertFalse(sc.isWord("spise")); 
    }

    @Test
    void testgetOneCharCompletions() throws IOException {
        SpellChecker sc = SpellChecker.fromFile("files/words_alpha.txt");
        List<String> result = sc.getOneCharCompletions("mat");
        assertFalse(result.contains("mate"));
        assertFalse(result.contains("math"));
    }
    
    @Test
    void testGetOneCharEndCorrections() throws IOException {
        SpellChecker sc = SpellChecker.fromFile("files/words_alpha.txt");
        List<String> result = sc.getOneCharEndCorrections("spawm");
        assertFalse(result.contains("spawl"));
        assertFalse(result.contains("spawn"));
    }
}
