# what is amazon lambda

AWS Lambda lets you run code without provisioning or managing servers. 
You pay only for the compute time you consume - there is no charge when your code is not running. 
With Lambda, you can run code for virtually any type of application or backend service - all with zero administration. 
Just upload your code and Lambda takes care of everything required to run and scale your code with high availability. 
You can set up your code to automatically trigger from other AWS services or call it directly from any web or mobile app.

# why

When you update your Facebook status, you’re using cloud computing. Checking your bank balance on your phone? You’re in the cloud again. Chances are you rely on cloud computing to solve the challenges faced by small businesses, whether you’re firing off emails on the move or using a bunch of apps to help you manage your workload.

In short, cloud is fast becoming the new normal.
This tutorial will show you how to use AWS Lambda. In a next installment, we'll add iText in the mix.
By using existing cloud services and enriching them with iText, your document workflow can live in the cloud.
e.g. Automatically run (cloud hosted) Java code to create an invoice (in a cloud file server) whenever a database (cloud based) gets updated.

# project idea

The idea behind this project is to create a service that can give a StackOverflow leaderboard.
The project will use:
 - Apache commons for HTTP requests
 - JSoup to process the StackOverflow HTML
 - AWS Lambda, to host our java functionality
 - Alexa API, to have an amazon echo dot device read the extracted content aloud

# what you'll need

You will need:
 - developer account for amazon (https://developer.amazon.com/)
 - amazon echo dot (optional, but cool)
 
# interacting with StackOverflow

The following utility class interacts with StackOverflow.
Please use it at your own discretion. This code is meant as an example. 
We do not condone high-volume scraping of StackOverflow.

````java
public class StackOverflow {

    public static int getReputation(String username)
    {
        try{
            return _getReputation(username);
        }catch(Exception ex)
        {
            return 0;
        }
    }

    private static int _getReputation(String username) throws IOException {
        Document document = Jsoup.parse(new URL("https://meta.stackoverflow.com/users/" + username),5000);

        Element reputationDiv = document.select("div.reputation").first();
        Integer repPoints = asInteger(reputationDiv.text());
        return repPoints == null ? 0 : repPoints;
    }

    private static Integer asInteger(String s)
    {
        s = s.replaceAll("[^0123456789]+","");
        try {
            return Integer.parseInt(s);
        }catch(Exception ex){}
        return null;
    }
}
````

# Speechlet

A speechlet is the part of your Alexa Skill that interacts with the JSON data it receives from Amazon.
It handles the core logic of your skill.

````java
public class StackOverflowSpeechlet implements Speechlet {

    public void onSessionStarted(SessionStartedRequest ssr, Session sn) throws SpeechletException { }

    public SpeechletResponse onLaunch(LaunchRequest lr, Session sn) throws SpeechletException { return null; }

    public SpeechletResponse onIntent(IntentRequest ir, Session sn) throws SpeechletException {
        
        // get intent name
        Intent intent = ir.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if(intentName.equals("leaderboardIntent"))
        {
            return build(buildSOString());
        }

        // exception
        throw new SpeechletException("Invalid Intent");         
    }

    public void onSessionEnded(SessionEndedRequest ser, Session sn) throws SpeechletException { }

    /*
     * utility methods for converting stack overflow results to string
     */
    private String buildSOString()
    {
        String[] userids = {"2065017","512061","6074376","766786","6156384"};        // user-ids (on StackOverflow) for some of the iText dev team
        String[] usernames = {"Benoit","Michael","Joris","Amedee","Samuel"};         // names of the iText dev team     

        List<Object[]> pairs = new ArrayList<>();
        for(int i=0;i<userids.length;i++)
        {
            String userID = userids[i];
            String userName = usernames[i];
            int userReputation = StackOverflow.getReputation(userID);
            pairs.add(new Object[]{userName, userReputation});
        }
        // sort
        java.util.Collections.sort(pairs, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                Integer i1 = (Integer) o1[1];
                Integer i2 = (Integer) o2[1];
                return -i1.compareTo(i2);
            }
        });
        String txt = "";
        for(int i=0;i<pairs.size();i++)
        {
            txt += (pairs.get(i)[0].toString() + " has " + pairs.get(i)[1].toString() + " points. ");
        }
        return txt;
    }

    /*
     * Utility methods for quickly building replies out of String
     */
    
    private SpeechletResponse build(String text)
    {
        return build(text, true);
    }
    
    private SpeechletResponse build(String text, boolean shouldEndSession)
    {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("iText Reader");
        card.setContent(text);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(text);

        SpeechletResponse response = SpeechletResponse.newTellResponse(speech, card);
        response.setShouldEndSession(shouldEndSession);
        return response;        
    }   
}
````


# SpeechletRequestStreamHandler

The SpeechletRequestStreamHandler handles access to your Speechlet. It checks whether the app accessing your logic has permission to do so.
Usually the implementation is pretty straightforward.

````java
public class StackOverflowSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler{
    
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        // supportedApplicationIds.add("amzn1.echo-sdk-ams.app.[unique-value-here]");
        supportedApplicationIds.add("amzn1.ask.skill.aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    public StackOverflowSpeechletRequestStreamHandler() {
        super(new StackOverflowSpeechlet(), supportedApplicationIds);
    }    
    
}
````

# building the amazon lambda function

1. Go to https://aws.amazon.com/
2. Click 'sign in to the console' (top right)
3. Enter your credentials
4. Click on 'Lambda' in the overview of services
5. You should now be in the Amazon Lambda Dashboard
6. Click 'Create Function'
7. Click 'Author from scratch'
8. Add 'Alexa Skills Kit' as the trigger
9. Click 'Next'
10. Enter a name, description, and select 'Java 8' as the runtime
11. Upload your complete jar (the jar should contain all depedencies)
12. Enter the path to the SpeechRequestStreamHandler, and an execution role with the right permissions. (e.g. if you are accessing databases, you need to execute with a role that has permissions to access these databases)
13. Click 'Next' and review the configuration

You should see something like 'ARN - arn:aws:lambda:us-east-1:012345678901:function:xxxxxxxxxxxx' atop your screen.
Copy this ID, the Alexa Skill API will ask you for it later.

# building the alexa skill

1. Go to https://developer.amazon.com/alexa
2. Click 'sign in' (top right)
3. Enter your credentials
4. Click on 'Alexa' in the ribbon atop the page
5. Click on the 'Get started' button in the 'Alexa Skills Kit' option
6. Click the 'Add a new skill' button
7. In the first tab, you'll see the application ID (something like 'amzn1.ask.skill.aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa')
   This should be filled in in your SpeechRequestStreamHandler. You want this to be the only application with access to your skill.
8. Fill in the required field in all the tabs (information about target audience, category of your skill, whether it has in-app purchases, etc)

We used the following for the intent schema

````
{
  "intents": [
    {
      "intent": "leaderboardIntent"
    }
  ]
}
````

and the sample utterances

````
leaderboardIntent give me the iText leaderboard
leaderboardIntent give me the leaderboard
leaderboardIntent show me the itext leaderboard
leaderboardIntent show me the leaderboard
leaderboardIntent recite the itext leaderboard
leaderboardIntent recite the leaderboard
````

# testing our application

One of the tabs on the Alexa Skills Kit page allows you to test your new skill by typing one of the sample utterances.
It then shows you the json that was sent back and forth.
This is a quick and easy way to verify whether your skill was properly set up.

# deploying

If you have an amazon echo dot device, and the account for that device is tied to your developer device you should be able to use your skill on your dot.
This is an example of another skill we built at iText.

[Bruno Lowagie using the iTextPDFReader](https://www.youtube.com/watch?v=cBJyd18MxaQ&t=129s)

# recap

In this tutorial we used AWS Amazon Lambda to power an Alexa Skill.

Learn more? Go to [itextpdf.com](http://itextpdf.com)