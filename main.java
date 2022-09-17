import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Date;

public class MyProgram
{
    public static void wait(int ms){
        ms=750; //*=1000
        for (int x = 0; x < 3; x++){
            System.out.print(".");
            try
            {
                Thread.sleep(ms/3);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("");
    }
    public static ArrayList[] getMadlib(int min, int max) throws IOException, InterruptedException {
        var uri = URI.create("https://madlibz.herokuapp.com/api/random?minlength="+min+"&maxlength="+max+"/");
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept","application/json")
                .timeout(Duration.ofSeconds(10))
                .build();
        var response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );
        //System.out.println(response.body().getClass().getName());

        String[] lines = response.body().split("\r\n|\r|\n");
        //System.out.println(lines.length); // counting lines

        ArrayList<String> sentences = new ArrayList<String>();
        ArrayList<String> wordTypes = new ArrayList<String>();

        BufferedReader bufReader = new BufferedReader(new StringReader(response.body()));
        String line=null;
        String mode = "wordTypes";
        while( (line=bufReader.readLine()) != null )
        {
            //System.out.println(line);
            if (line.contains("[")){ //check if it is going to new line type
                if (line.contains("blanks")){ //word types | redundant
                    mode = "wordTypes";
                    }
                else if (line.contains("value")){ //sentences
                    mode = "sentences";
                }
            }
            else if ((line.contains("\""))&(line.contains(":")==false)){
                    if (mode == "wordTypes"){
                        wordTypes.add(line.replaceAll("\",", "").trim().replace("\"","")); //commas............................
                    }
                    else if (mode == "sentences"){ //elseif just in case there is more added
                        sentences.add(line.replaceAll("\",", "").trim().replace("\"",""));
                    }
            }
        }
        //System.out.println(wordTypes);
        //System.out.println(sentences);
        return new ArrayList[]{wordTypes, sentences};
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("Hello! Welcome to Alex's bug-free madlib generator. Say hello!");
        String greetingResponse = input.nextLine(); //maybe utilize this
        wait(rand.nextInt(2));
        System.out.println("Yeah hi!! What's your name?");
        String userName = input.nextLine(); //YOU HAVE TO UTILIZE THIS FOR THE PROJECT
        wait(rand.nextInt(2));
        System.out.println("What should the minimum length of the Madlib be? (Enter an integer)");
        int min = input.nextInt();
        wait(rand.nextInt(2));
        System.out.println("What should the maximum length of the Madlib be? (Enter an integer)");
        int max = input.nextInt();
        wait(rand.nextInt(2));
        input.nextLine(); //https://stackoverflow.com/questions/23450524/java-scanner-doesnt-wait-for-user-input
        if ((min <= 0)&(max<=0)&(min>=100)&(max>=100)&(max<=min)){
            System.out.println("Apparently you screwed something up and your minimum and/or maximum length(s) are invalid. I'll just fix it for you...");
            min = 5;
            max = 25;
            wait(rand.nextInt(2));
        }
        ArrayList[] madLib = getMadlib(min,max);
        //junk sentences add more personality! :D
        final String[] junkSentences = {"Hmm okay..","bro ok","Alright.","mm okay","Uhhh that is- okay..", "Okay, lets move on.."};
        final String[] junkSentences2 = {"pleasee enter a ","put a ","Can you please enter a ","Input a "};
        ArrayList sentences = madLib[1];
        ArrayList wordTypes = madLib[0];
        //number will make it so it is the same adjective | cannot be greater than its index
        String story = "";
        ArrayList<String> inputtedWords = new ArrayList<String>();

        for (int i = 0; i < wordTypes.size(); i++){
            /*if ((wordTypes.get(i)).matches(".*\\d.*")){
                continue;
                //referencing a previous
                //Scanner in = new Scanner(wordTypes.get(i)).useDelimiter("[^0-9]+"); //stack overflow
                int integer = in.nextInt();
                if (integer < i){
                    String data = inputtedWords.get(integer);
                    inputtedWords.add(data);
                }
                else{
                    System.out.println("Apparently the person who made the madlib doesn't know what they are doing! An exception occured.");
                    return;
                }

            }*/
            //if{
                int randInt = rand.nextInt(junkSentences2.length-1); // is 0 the first index in a table java?
                System.out.println(junkSentences2[randInt]+wordTypes.get(i)+".");

                if (i == 0){
                    //Thread.sleep(5000);
                }
                String data = input.nextLine();

                wait(rand.nextInt(2));
                if (data.length() > 20){
                    System.out.println("jeez, you didn't have to write a whole essay");
                    wait(2);
                }
                else if ((data.length() <= 2)&(data.contains("numbers")!=true)){
                    System.out.println("Can you actually write something? Whatever let's move on...");
                    wait(2);
                }
                else{
                    randInt = rand.nextInt(junkSentences.length-1);
                    System.out.println(junkSentences[randInt]);
                    wait(3);
                }
                inputtedWords.add(data);
            //}
        }
        String madlib = "";
        for (int f = 0; f < inputtedWords.size(); f++){
            madlib = madlib +" "+sentences.get(f)+" "+inputtedWords.get(f);

        }
        String parts[] = madlib.split("\\."); //sometimes buggy with Mrs. Mr. , not HUGEEE issue..
        String finalResult = "";
        for (int i = 0; i < parts.length; i++){
            finalResult = finalResult+parts[i]+"\n";
        }
        Date date = new Date(); // This object contains the current date value
        System.out.println(userName+"'s Madlib ~ "+date+"\n");
        System.out.println(finalResult+"\n This madlib was created by the creative genius, "+userName+". Props to them for creating this masterpiece but adlibz.herokuapp.com truly did all the dirty work of preparing the madlib.");
    }
}
