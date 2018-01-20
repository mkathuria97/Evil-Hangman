//HangmanManager manages the state of Hangman in which the computer delays in picking a word until
//it is forced to
import java.util.*;
public class HangmanManager{
   private Set<String> words;
   private int chances;
   private SortedSet<Character> lettersGuessed;
   private String finalPattern;
   
   //pre: throws an IllegalArgumentException if the given length is less than 1
   //or the number of chances to guess is less than 0
   //post: initially all the words to be considered by the HangmanManager from the dictionary
   //are the words of given length
   public HangmanManager(List<String> dictionary, int length, int max){
      if(length < 1 || max < 0){
         throw new IllegalArgumentException();
      }
      words = new TreeSet<String>();
      chances = max;
      lettersGuessed = new TreeSet<Character>();
      finalPattern = "-";
      for(int i = 0; i < length - 1; i++){
         finalPattern += " -";
      }
      for(int i = 0; i < dictionary.size(); i++){
         if(dictionary.get(i).length() == length){
            words.add(dictionary.get(i));
         }
      }
   }
   
   //returns the words being considered by the HangmanManager
   public Set<String> words(){
      return words;
   }
   
   //returns the number of chances left
   public int guessesLeft(){
      return chances;
   }
   
   //returns the letters that have been guessed by the player
   public SortedSet<Character> guesses(){
      return lettersGuessed;
   }
   
   //pre: throws an IllegalStateException if there are no words left to be considered by
   //the HangmanManager
   //post: returns the pattern to be displayed
   //letters that are not guessed are represented by dashes and the letters in the pattern
   //are seperated by spaces
   public String pattern(){
      if(words.size() == 0){
         throw new IllegalStateException();
      }
      return finalPattern;
   }
   
   //pre: throws an IllegalArgumentException if the number of chances left is less than
   //one or there are no words left to be considered
   //pre: throws an IllegalArgumentException if the given guess has already been guessed by the
   //player
   //post: returns the number of occurences of the given guess in the pattern
   public int record(char guess){
      if(chances < 1 || words.size() == 0){
         throw new IllegalStateException();
      }
      if(lettersGuessed.contains(guess)){
         throw new IllegalArgumentException();
      }
      lettersGuessed.add(guess);
      Map<String, Set<String>> patternWords = new TreeMap<String, Set<String>>();
      for(String word : words){
         addPatternWords(patternWords, word, guess);
      }
      newListOfWords(patternWords);
      return guessOccurences(guess);
   }
   
   //creates a pattern for a word
   //adds pattern as a key to the map if the pattern does not exist in the map
   //and add the word of the same pattern as a value to that key
   //if the pattern exists, add the word of the same pattern as a value to that key
   private void addPatternWords(Map<String, Set<String>> patternWords, String word, char guess){
      String pattern = "";
      for(int i = 0; i < word.length() - 1; i++){
         pattern += createPattern(guess, word.charAt(i)) + " ";
      }
      pattern += createPattern(guess, word.charAt(word.length() - 1));
      
      if(!patternWords.containsKey(pattern)){
         Set<String> wordsList = new TreeSet<String>();
         patternWords.put(pattern, wordsList);
         wordsList.add(word);
      }else{
         patternWords.get(pattern).add(word);
      }
   }
   
   //creates a pattern for the word
   private char createPattern(char guess, char letter){
      if(lettersGuessed.contains(letter)){
         return letter;
      }else if(letter == guess){
         return guess;
      }else{
         return '-';
      }
   }
   
   //sets the pattern to be displayed to the pattern that consists of maximum
   //number of words
   //updates the list of words to be considered by the HangmanManager
   private void newListOfWords(Map<String, Set<String>> patternWords){
      int maxLength = 0;
      for(String pattern : patternWords.keySet()){
         if( patternWords.get(pattern).size() > maxLength){
            maxLength = patternWords.get(pattern).size();
            finalPattern = pattern;
            words = patternWords.get(pattern);
         }
      }
   }
   
   //returns the number of occurences of the given guess in the pattern
   private int guessOccurences(char guess){
      int count = 0;
      for(int i = 0 ; i < finalPattern.length(); i++){
         if(finalPattern.charAt(i) == guess){
            count++;
         }
      }
      //decrements the chances left by 1 if the given guess has no occurence in the
      //pattern
      if(count < 1){
         chances--;
      }
      return count;
   }
}
