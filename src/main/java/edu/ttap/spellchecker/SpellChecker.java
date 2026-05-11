package edu.ttap.spellchecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

/**
 * A spellchecker maintains an efficient representation of a dictionary for
 * the purposes of checking spelling and provided suggested corrections.
 */
public class SpellChecker {
    /** The number of letters in the alphabet. */
    private static final int NUM_LETTERS = 26;

    /** The path to the dictionary file. */
    private static final String DICT_PATH = "files/words_alpha.txt";

    /**
     * @param filename the path to the dictionary file
     * @return a SpellChecker over the words found in the given file.
     */
    public static SpellChecker fromFile(String filename) throws IOException {
        return new SpellChecker(Files.readAllLines(Paths.get(filename)));
    }

    /** A Node of the SpellChecker structure. */
    private class Node {
        Node[] children;

        boolean isEnd;

        public Node() {
            children = new Node[NUM_LETTERS];
        }
    }

    /** The root of the SpellChecker. */
    private Node root;

    /**
     * Constructs a SpellChecker over the given dictionary.
     * @param dict the list of words to include in the dictionary
     */
    public SpellChecker(List<String> dict) {
        root = new Node();

        for (int i = 0; i < dict.size(); i++) {
            add(dict.get(i));
        }
    }

    /**
     * Adds the given word to the trie.
     * @param word the word to add
     */
    public void add(String word) {
        Node cur = root;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int index = letter - 'a';

            if (cur.children[index] == null) {
                cur.children[index] = new Node();
            }

            cur = cur.children[index];
        }

        cur.isEnd = true;
    }

    /**
     * Checks if the given word is in the dictionary.
     * @param word the word to check
     * @return true if the word is in the dictionary, false otherwise
     */
    public boolean isWord(String word) {
        Node cur = root;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int index = letter - 'a';

            if (cur.children[index] == null) {
                return false;
            }

            cur = cur.children[index];
        }

        return cur.isEnd;
    }

    /**
     * Returns a list of all words in the dictionary that can be formed by
     * adding a single character to the end of the given word.
     * @param word the word to complete
     * @return a list of all possible completions
     */
    public List<String> getOneCharCompletions(String word) {
        List<String> words = new ArrayList<>();
        Node cur = root;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int index = letter - 'a';

            if (cur.children[index] == null) {
                return words;
            }

            cur = cur.children[index];
        }

        for (int i = 0; i < NUM_LETTERS; i++) {
            if (cur.children[i] != null) {
                if (cur.children[i].isEnd) {
                    words.add(word + ((char) ('a' + i)));
                }
            }
        }

        return words;
    }

    /**
     * Returns a list of all words in the dictionary that can be formed by changing
     * a single character in the given word.
     * @param word the word to correct
     * @return a list of all possible corrections
     */
    public List<String> getOneCharEndCorrections(String word) {
        List<String> correctWords = new ArrayList<>();
        char removed = word.charAt(word.length() - 1);
        String beg = word.substring(0, word.length() - 1);

        for (char c = 'a'; c <= 'z'; c++) {
            if (c != removed) {
                if (isWord(beg + c)) {
                    correctWords.add(beg + c);
                }
            }
        }

        return correctWords;
    }

    /**
     * The main entry point for the program.
     * @param args the command-line arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java SpellChecker <command> <word>");
            System.exit(1);
        } else {
            String command = args[0];
            String word = args[1];
            SpellChecker checker = SpellChecker.fromFile(DICT_PATH);

            switch (command) {
                case "check": {
                    System.out.println(checker.isWord(word) ? "correct" : "incorrect");
                    System.exit(0);
                }

                case "complete": {
                    List<String> completions = checker.getOneCharCompletions(word);
                    for (String completion : completions) {
                        System.out.println(completion);
                    }
                    System.exit(0);
                }

                case "correct": {
                    List<String> corrections = checker.getOneCharEndCorrections(word);
                    for (String correction : corrections) {
                        System.out.println(correction);
                    }
                    System.exit(0);
                }

                default: {
                    System.err.println("Unknown command: " + command);
                    System.exit(1);
                }
            }
        }
    }
}