# what is amazon lambda

AWS Lambda lets you run code without provisioning or managing servers. 
You pay only for the compute time you consume - there is no charge when your code is not running. 
With Lambda, you can run code for virtually any type of application or backend service - all with zero administration. 
Just upload your code and Lambda takes care of everything required to run and scale your code with high availability. 
You can set up your code to automatically trigger from other AWS services or call it directly from any web or mobile app.

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

# building the alexa skill


# testing our application

## amazon lambda

First, we need to wrap everything in a single .jar file.
In the amazon console panel, navigate to Lambda. (https://console.aws.amazon.com/lambda/)
1. Create a new lambda function, choosing the blank function as a template.

2. For code entry type, select "upload a .ZIP or JAR file"
   upload the zip you created.

3. in configuration, select the runtime (java 8 should be fine)
   set the full path to the handler
   you'll need to create and set a dummy role to be able to execute this function
   
4. Nothing should be changed in Triggers, Tags and Monitoring

## alexa

1. go to https://developer.amazon.com/edw/home.html#/skills

2. create a new skill

3. Fill in details like Skill Type, Language, Name, and Invocation Name
   You'll see that the Alexa API generated an application Id for your skill.
   You'll need to fill this in at the SpeechletRequestStreamHandler to ensure only this skill can activate the lambda function.
   Obviously, that means you will need to recompile and repackage your code.
   
4. build the interaction model, this is well-documented in GitHub examples

5. In the configuration tab, fill in the lambda ARN (amazon resource name) that points to your lambda function   

## testing

You should now be able to test your skill by entering an example utterance and seeing the reply that comes back from our lambda function.
As an example, we typed "Where am I?" which causes the iTextPDFReader skill to look up your current PdfLocation and print it as a string.

```json
{
  "version": "1.0",
  "response": {
    "outputSpeech": {
      "type": "PlainText",
      "text": "You are currently at chapter 1, page 1, paragraph 1. There are 9 pages in this chapter. There are 3 chapters in this book."
    },
    "card": {
      "content": "You are currently at chapter 1, page 1, paragraph 1. There are 9 pages in this chapter. There are 3 chapters in this book.",
      "title": "iText Reader",
      "type": "Simple"
    },
    "shouldEndSession": false
  },
  "sessionAttributes": {}
}
```

# deploying

If you have an amazon echo dot device, and the account for that device is tied to your developer device you should be able to use your skill on your dot.

[Bruno Lowagie using the iTextPDFReader](https://www.youtube.com/watch?v=cBJyd18MxaQ&t=129s)

# sources

You can find the full source code for this project at [github](https://git.itextsupport.com/users/joris.schellekens/repos/itextpdfreader/browse)

# recap

In this tutorial we used AWS Amazon Lambda to power an Alexa Skill.
We used iText under the hood to enable us to work with tagged pdf documents.

Learn more? Go to [itextpdf.com](http://itextpdf.com)