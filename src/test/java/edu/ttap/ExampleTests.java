package edu.ttap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testIsWord() {
        SpellChecker sc = new SpellChecker(List.of("dog", "doge", "dogma"));
        assertTrue(sc.isWord("dog"));
        assertFalse(sc.isWord("digy"));
}
    @Test
    void testGetOneCharEndCorrections_multipleResults() {
        SpellChecker sc = new SpellChecker(List.of("dog", "doge", "dogma"));
        List<String> result = sc.getOneCharEndCorrections("dogy");
        assertTrue(result.contains("doge"));
    }
}
