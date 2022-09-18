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

public class MadlibGenerator
{
    public static void wait(int ms){
        ms=750; //*=1000 | overridden for now
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
        System.out.println();
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
        System.out.println("Received a madlib with "+sentences.size()+" sentences and "+wordTypes.size()+" words to input!");
        return new ArrayList[]{wordTypes, sentences};
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        //junk sentences add more personality! :D
        final String[] junkSentences = {"Hmm okay..","bro ok","Alright.","mm okay","Uhhh that is- okay..", "Okay, lets move on..","Alright.","Okay"};
        final String[] junkSentences2 = {"pleasee enter a(n) ","put a(n) ","Can you please enter a(n) ","Input a ","Type in a(n)"};

        Scanner input = new Scanner(System.in);
        Random rand = new Random();


        System.out.println("Hello! Welcome to Alex's bug-free madlib generator. Say hello!"+" ~ (1/5)");
        String greetingResponse = input.nextLine(); //maybe utilize this
        wait(rand.nextInt(2));
        System.out.println("Yeah hi!! What's your name?"+" ~ (2/5)");
        String userName = input.nextLine();
        wait(rand.nextInt(2));
        System.out.println("What should the target minimum length of the Madlib be? (Enter an integer)"+" ~ (3/5)");
        int min = input.nextInt();
        wait(rand.nextInt(2));
        System.out.println("What should the target maximum length of the Madlib be? (Enter an integer)"+" ~ (4/5)");
        int max = input.nextInt();
        wait(rand.nextInt(2));
        System.out.println("Enter an interesting adjective. ~ (5/5)");
        String endingAdjective = input.nextLine();

        input.nextLine(); //https://stackoverflow.com/questions/23450524/java-scanner-doesnt-wait-for-user-input
        wait(rand.nextInt(2));
        if ((min<=0)||(max<=0)||(min>=100)||(max>=100)||(max<=min)){
            System.out.println("Apparently you screwed something up and your minimum and/or maximum length(s) are invalid. I'll just fix it for you...");
            min = 5;
            max = 25;
            wait(rand.nextInt(2));
        }

        ArrayList[] madLib = getMadlib(min,max);
        ArrayList sentences = madLib[1];
        ArrayList wordTypes = madLib[0];
        //number will make it so it is the same adjective | cannot be greater than its index
        String story = "";
        ArrayList<String> inputtedWords = new ArrayList<String>();

        for (int i = 0; i < wordTypes.size(); i++){
                int randInt = rand.nextInt(junkSentences2.length-1); // is 0 the first index in a table java?
                System.out.println(junkSentences2[randInt]+wordTypes.get(i)+". ~ ("+(i+1)+"/"+wordTypes.size()+")");

                if (i == 0){
                    //Thread.sleep(5000);
                }
                String data = input.nextLine();

                wait(rand.nextInt(2));
                if (data.length() > 20){
                    System.out.println("jeez, you didn't have to write a whole essay");
                    wait(2);
                }
                else if ((data.length() <= 2)&(!data.contains("numbers"))){
                    System.out.println("Can you actually write something? Whatever let's move on...");
                    wait(2);
                }
                else{
                    randInt = rand.nextInt(junkSentences.length-1);
                    System.out.println(junkSentences[randInt]);
                    wait(3);
                }
                inputtedWords.add(data);
        }
        String madlib = "";
        for (int f = 0; f < inputtedWords.size(); f++){
            madlib = madlib +" "+sentences.get(f)+" "+inputtedWords.get(f);

        }
        String parts[] = madlib.split("\\."); //sometimes buggy with Mrs. Mr. , not HUGEEE issue.. Also with question marks and exclamation marks
        //String finalResult = "";
        Date date = new Date(); // This object contains the current date value
        System.out.println("\n"+"--------------------------------"+"\n"+userName+"'s Madlib ~ "+date+"\n");
        for (int i = 0; i < parts.length; i++){
            //finalResult = finalResult+parts[i]+". \n";
            System.out.println(parts[i]+".");
        }
        System.out.println("\n This madlib was created by the "+endingAdjective+" genius, "+userName+". Props to them for creating this masterpiece but madlibz.herokuapp.com truly did all the dirty work of preparing the Madlib."+"\n"+"--------------------------------");
    }
}
