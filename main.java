import java.util.Scanner;
import java.util.Random;

public class MyProgram
{
    public static void main(String[] args)
    {
         Random rand = new Random();
        //junk sentences add more personality! :D
        final String[] junkSentences = {"Hmm okay..","bro ok"};
        final String[] junkSentences2 = {"pleasee enter a ","put a ","Can you please enter a ","Input a "};
        
        final String[] sentences = {"It was a ", "November day."}; //these are in order!
        final String[] wordTypes = {"adj","noun"}; //adj, noun, verb, number
        //number will make it so it is the same adjective | cannot be greater than its index
        String story = "";
        String[] inputtedWords = {};
        
        Scanner input = new Scanner(System.in);
        
        for (int i = 0; i < wordTypes.length; i++){
            if (wordTypes[i].matches(".*\\d.*")){
                //referencing a previous 
                Scanner in = new Scanner(wordTypes[i]).useDelimiter("[^0-9]+"); //stack overflow
                int integer = in.nextInt();
                if (integer < i){
                    
                }
                else{
                    System.out.println("Apparently the person who made the madlib doesn't know what they are doing! An exception occured.");
                }
                
            }
            else{
                System.out.println(i);
                int randInt = rand.nextInt(junkSentences2.length-1); // is 0 the first index in a table java?
                System.out.println(junkSentences2[randInt]+wordTypes[i]+".");
                String data = input.nextLine();
                
                if (data.length() > 100){
                    System.out.println("jeez, you didn't have to write a whole essay");
                }
                else if (data.length() <= 1){
                   System.out.println("Can you actually write something? Whatever let's move on...");
                }
                else{
                    randInt = rand.nextInt(junkSentences.length-1);
                    System.out.println(junkSentences[randInt]);
                }
                inputtedWords.add(data);    
                
            }
            
        }
        
        
    }
}
