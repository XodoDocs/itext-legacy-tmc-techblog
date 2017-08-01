# How To Write A Great Support Ticket

Miscommunication is an inevitable part of life.

As humans beings, we all sometimes struggle with getting our point across effectively - the more complex the subject matter, the more communication tends to break down.

When it comes to writing something as important as a support ticket, many people are at a loss as to what they should include, and how to communicate a technical issue to the support team.

Thankfully, there are certain guidelines you can follow which prevent much of the back-and-forth that adds time and frustration to the entire process.

When you need to get something fixed, and fixed fast - make sure you’re sharing all relevant information with your support team; be as clear and concise as possible, and you’ll see improved results that help get you back up and running as quickly as possible.

## Before logging a support ticket
Search first!

We have lots of documentation on [developers.itextpdf.com][website]. We also have a vibrant developer community on [Stack Overflow][stackoverflow]. If you can't find an answer on either website, go ahead and ask us!

## Write a title that summarizes the specific problem
The title is the first thing we will see, and if your title isn't descriptive, we'll have a harder time in triage. So make it count:

 * Pretend you're talking to a busy colleague and have to sum up your entire question in one sentence: what details can you include that will help someone identify and solve your problem? Include any error messages, key APIs, or unusual circumstances that make your question unique.
 * If you're having trouble summarizing the problem, write the title last - sometimes writing the rest of the question first can make it easier to describe the problem.

Examples:

 * Bad: C# Math Confusion
 * Good: Why does using float instead of int give me different results when all of my inputs are integers?
 
 * Bad: Pdf/iText not working
 * Good: Table layout is not as expected: text is centered instead of left-aligned

 * Bad: Text extraction not working
 * Good: Text extraction run in Web environment using simple strategy returns unexpected results (with current and expected results in the description)

 * Bad: Layout Issues (containing three seperate issues)
 * Good: 
   1. pdfXFA omits duplex pages while flattening
   2. pdfXFA incorrect page numbering while flattening
   3. pdfXFA duplictes master page page headers while flattening 

## Be clear about your overall objective
Sometimes it’s easy to get into the details without ever explaining what you’re actually trying to do. So start with that. It helps us understand what you’re trying to do. We might be able to show you a different route, or cut to the chase and let you know it’s not supported.

## Be specific about what you’ve already tried
The last things you want is that they tell you to try something you’ve already done. So by telling us what you’ve done, you’ll eliminate wasted words going back and forth, and you’ll highlight exactly how well you know what you’re doing and it will shape how we respond.

## Focus on the details
Nothing will get your issue resolved more quickly than providing as much detail as possible the very first time you reach out with a support request. Don’t wait for your support rep to ask questions, give us the information up-front and you’ll get much better results.

We require the following information:

 * Version of iText and add-ons
 * Java or .NET
 * Environment: JDK version, Windows, Linux, IIS, Tomcat,...
 * Each customer can register up to 3 support contacts, so you may want to add the names of your colleagues who require access to your ticket.
 * Description of the expected output or behaviour. For layout problems, a visual representation is required.
 * A [Minimal, Complete, and Verifiable example][MCVE]. **Don't just copy in your entire program!**
    * For a support ticket about an exception, it needs to trigger the exception.
    * For a support ticket about output/layout not coming out as expected, the current code that generates the relevant part of the PDF.
    * If your code uses external dependencies, they have to mocked out as much as possible.
    * Send us actual code, not a screenshot of an IDE.
 * All input files (PDF, HTML, CSS,...) that are needed to run the self-contained code snippet.
    * The input file needs to be simple and unclutterd (i.e. avoid a 1000+ page monster PDF when the problem is with a single page)
    * To comply with your local Data Protection laws, you should **never** send us actual customer data. Use dummy data instead.
    * Any bulk input data (e.g. data that's normally retrieved from a database) must be delivered in a format that is easy to parse (CSV, JSON, XML,...), or just hard-coded in the example.
 * Actual and, if available, expected output files in PDF format.

## Proof-read before posting!
Now that you're ready to ask your question, take a deep breath and read through it from start to finish. Pretend you're seeing it for the first time: does it make sense? Try reproducing the problem yourself, in a fresh environment and make sure you can do so using only the information included in your question. Add any details you missed and read through it again. Now is a good time to make sure that your title still describes the problem!

## Follow up
Ticket response times can vary depending on the complexity and the severity of your request vs. others in the queue. Know that you have done your due diligence in getting the issue resolved by following the above steps.

 * When you have registered your issue, either via email or via the JIRA web portal, you will get an email confirmation with your issue number (SUP-nnnn). Any email you send to the support email address, that keeps the issue number intact, will be added to the issue as a comment.
 * You will get a follow up mail when your issue is acknowledged and assigned to a developer. You may get asked for additional information that helps us reproduce the issue. The response time depends on the severity level of your issue, as described in Addendum 2 of the License Agreement.

[website]: http://developers.itextpdf.com/
[stackoverflow]: http://stackoverflow.com/questions/tagged/itext7
[MCVE]: https://stackoverflow.com/help/mcve