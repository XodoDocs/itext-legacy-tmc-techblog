# Introduction

pdfCalligraph is an add-on module for iText 7, designed to seamlessly handle any kind of advanced shaping operations 
when writing textual content to a PDF file. Its main function is to correctly render complex writing systems 
such as the right-to-left Hebrew and Arabic scripts, 
and the various writing systems of the Indian subcontinent and its surroundings. 
In addition, it can also handle kerning and other optional features that can be provided by certain fonts in other writing systems.

In this position paper, we first provide a number of cursory introductions:
we'll start out by exploring the murky history of encoding standards for digital text, 
and then go into some detail about how the Arabic and Brahmic alphabets are structured. 
Afterwards, we will of course discuss the problems those writing systems pose in the PDF standard, 
the solutions provided to these problems by iText 7's add-on pdfCalligraph, and of course also a hands-on user guide.

# Technical overview

We will not be sharing revolutionary insights in this section, 
so if you are comfortable with your knowledge about character encodings, 
the Arabic alphabet, and/or the Brahmic scripts, you can feel free to skip those sections.

## A bit of encoding history

In order to represent data in a digital way, 
ASCII -> Unicode (code points, code blocks, alternates)

## A bit of writing history

Over the last 5000+ years, humanity has created a plethora of writing systems. 
After an extended initial period of protowriting, when people tried to convey 
concepts and/or words by drawing images, a number of more abstract systems evolved. 
One of the most influential writing systems was the script developed 
and exported by the seafaring trade nation of Phoenicia. From it evolved, 
after extended periods of time, alphabets like Greek and its descendants (Latin, Cyrillic, Runes, etc), 
and the Aramaic abjad and its descendants (Arabic, Hebrew, Syriac, etc). 
Although this is a matter of scientific debate, it is possible that the Phoenician alphabet is also 
an ancestor of the entire Brahmic family of scripts, which contains Kannada, Thai, Telugu, 
and over a hundred other writing systems used primarily in South-East Asia and the Indian subcontinent.

The other highly influential writing system that is probably an original invention, the Han script, 
has descendants used throughout the rest of East Asia in the Chinese-Japanese-Korean spheres of influence.

## A very brief introduction to the Arabic script

Arabic is a writing system used for a large number of languages in the greater Middle East.
It is most prominently known from its usage for the Semitic language Arabic, 
and from that language's close association with Islam, since most religious Muslim literature is written in Arabic.
As a result, many other non-Semitic language communities in and around the culturally Arabic/Muslim sphere of 
influence have also adopted the Arabic script, like Farsi (Iran), Pashto (Afghanistan),
Mandinka (Senegal, Gambia), Malay (Malaysia, Indonesia, ...), etc.
Some of these communities have introduced new letters for the alphabet, 
often based on the existent ones, to account for sounds and features not found in the original Arabic language.

Arabic is an abjad, meaning that, in principle, only the consonants of a given word will be written. 
Like most other abjads, it is 'impure' in that the long vowels (/a:/, /i:/, /u:/) are also written, 
the latter two with the same character that is also used for /j/ and /w/. 
The missing information about the presence and quality of short vowels must be filled in by the reader; 
hence, it is usually necessary for the user to actually know the language that is written 
in order to be able to fully pronounce the written text.

Standard Arabic has 28 characters, but there are only 16 base forms: a number of the characters are dotted variants of others.
These basic modification dots, called i'jam, have been all but mandatory in writing since at least the 11th century.

![i'jam distinguishes between otherwise identical forms (from Wikipedia.org)](./ijam.png)

Like the other Semitic abjads, Arabic is written from right to left, and does not have a distinction between upper and lower case. 
It is in all circumstances written cursively, making extensive use of ligatures to join letters together into words. 
As a result, all characters have several appearances, depending on whether or not they're being adjoined to the previous and/or next letter in the word.

![contextual variation of characters (from Wikipedia.org)](./mutations.png)

This concept is easy to illustrate with a hands-on example. The Arabic word 'aniq (meaning elegant) is written with the following letters:

![Arabic word 'aniq, not ligaturized](./typography/elegant%20arabic%20bad%20aniq.svg)

However, in actual writing, the graphical representation shows marked differences. 

![Arabic word 'aniq, properly ligaturized](./typography/elegant%20arabic%20good%20aniq.svg)

The rightmost letter, alif, is unchanged, because by rule it does not join with any subsequent characters.
The leftmost three letters are joined to one another,
to the point where the character in the medial position is unrecognizable compared to its base form, 
save for the double dot underneath it.

It is possible to write fully vocalized Arabic, with all phonetic information available,
through the use of diacritics. This is mostly used for students who are learning Arabic,
and for texts where phonetic ambiguity must be avoided (e.g. the Qur'an).
The phonetic diacritics as a group are commonly known as tashkil;
its most-used members are the harakat (short /a/, /i/, /u/), and the sukun which denotes absence of a vowel.

In the Unicode standard, the Arabic script is encoded into a number of ranges of code points:
* The base forms for both Standard Arabic and a number of derived alphabets in Asia are located in U+0600–U+06FF (Arabic).
* Supplemental characters, mostly for African and European languages, are in U+0750–U+077F (Arabic Supplement) and U+08A0–U+08FF (Arabic Extended-A)
* Two ranges, U+FB50–U+FDFF (Arabic Pres. Forms-A) and U+FE70–U+FEFF (Arabic Pres. Forms-B),
that define unique Unicode points for all contextual appearances (isolated, initial, medial, final) of Arabic glyphs

## A very brief introduction to the Brahmic scripts

So named because of their descent from the ancient alphabet called Brahmi,
the Brahmic scripts are a large family of writing systems used primarily in India and South-East Asia.
They are abugidas, i.e. consonants are written with an implied vowel,
and only deviations from that implied vowel (usually a short /a/ or schwa) are marked.
All Brahmic alphabets are written from left to right, and have as a defining feature
that the characters can change shape in a number of contexts.

The Brahmic family is very large and diverse, with over 100 existing writing systems. Some are used for a
single language (e.g. Telugu), others for dozens of languages (e.g. Devanagari, for Hindi, Marathi, Nepali),
and others only in specific contexts (e.g. Baybayin, only for ritualistic uses of Tagalog).
The Sanskrit language, on the other hand, can be written in many scripts, and has no 'native' alphabet associated with it.

The Brahmic scripts historically diverged into a Northern and a Southern branch.
In a very broad generalization, Northern Brahmic scripts are used for the Indo-European languages prevalent
in Northern India, whereas Southern Brahmic scripts are used in Southern India for Dravidian languages, and for
Tai, Austro-Asiatic, and Austronesian languages in larger South-East Asia.

### Northern Brahmi

Many scripts of the Northern branch show the characteristic horizontal bar to signify the grouping of characters into words.

![Punjabi word kirapaalu (Gurmukhi alphabet)](./typography/elegant%20gurmukhi%20good%20kirapaalu.svg)

In Devanagari, one of the more prominent alphabets of the Northern Brahmi branch,
an implied vowel /a/ is not expressed in writing (#1), while other vowels take the shape of various diacritics (#2-5).
#5 is a special case, because the short /i/ diacritic is positioned to the left of its consonant,
even though it follows it in the byte string. When typing a language written in Devanagari,
one would first input the consonant and then the vowel, but they will be reversed by a good text editor in any visual representation.

![Devanagari t combined with various vowels](./typography/vowels%20devanagari.svg)

Another common occurrence is the use of half-characters in consonant clusters i.e.
affixing a modified version of the first letter to an unchanged form of the second.
When writing consonant clusters, a diacritic called the *halant* must be inserted in the byte sequence
to make it clear that the first consonant must not be pronounced with its inherent vowel.
Editors will interpret the occurrence of halant as a sign that the preceding letter must be rendered as a half-character.

![Devanagari effect of halant](./typography/halant%20devanagari.svg)

If the character accompanied by the halant is followed by a space, then the character is shown with an accent-like diacritic below (#8).
If it is not followed by a space, then a half character is rendered (#7).
As you can see, line #7 contains the right character completely, 
and also everything from the left character up until the long vertical bar. This form is known as a “half character”.


The interesting thing is that #7 and #8 are composed of the exact same characters, only in a different order which has a drastic effect on the eventual visual realization. The reason for this is that the halant is used in both cases, but at a different position in the byte stream. 

### Southern Brahmi

The Southern branch shows more diversity but, in general, will show the characters as more isolated:

![Tamil word nerttiyana](./typography/elegant%20tamil%20good%20nerttiyana.svg)

Some vowels will change the shape of the accompanying consonants, rather than being simple diacritical marks:

![Kannada s combined with various vowels](./typography/vowels%20kannada.svg)

Southern Brahmi also has more of a tendency to blend clustering characters into unique forms
rather than affixing one to the other.

![Kannada effect of halant](./typography/halant%20kannada.svg)

Some scripts will also do more repositioning logic for some vowels, rather than substitutions.

![Tamil k combined with various vowels, stressing repositioning](./typography/vowels%20tamil.svg)


## A bit of font history

TTF & OTF

# Using pdfCalligraph

## A bit of iText history

The iText library was originally written in the context of Western European languages, 
and it was only designed to handle left-to-right alphabetic scripts. 
However, the writing systems of the world can be much more complex and varied 
than just a sequence of letters with no interaction. 
Supporting every type of writing system that humanity has developed is a tall order, 
but we strive to be a truly global company. As such, we are determined to come as close to that goal as technology allows us as long as there's a decent business case [NOTE: phrasing].

In earlier versions of iText, we were already able to render Chinese, Japanese, and Korean (CJK) glyphs in PDF documents, 
and had limited support for the right-to-left Hebrew and Arabic scripts. 
With iText 7, we took the next step and went on to create a module that could support the elusive Brahmic scripts, 
which are used mostly in the Indian subcontinent.

## Java limitations

iText 7

## PDF limitations

## Support

The initial release of pdfCalligraph provided support for the following scripts:

* Arabic, except tashkil
* Hebrew (right-to-left)
* Devanagari, used for Hindi, Marathi, Sindhi, etc
* Tamil

The second release, version 1.0.1, expanded this support to:

* Arabic, including tashkil
* Gurmukhi, used for writing Punjabi
* Kannada

## Using pdfCalligraph

Using pdfCalligraph is exceedingly easy: you just load the correct binaries into your project, 
make sure your valid license file is loaded, 
and iText 7 will automatically go into the pdfCalligraph code when text instructions are encountered 
that contain Indic texts, or a script that is written from right to left.

The iText layout module will automatically look for the pdfCalligraph module on the classpath 
if Indic/Arabic text is encountered by the Renderer Framework. If pdfCalligraph is available, iText will call its functionality 
to provide the correct glyph shapes to write to the PDF file. iText will not attempt any advanced shaping operations 
if the pdfCalligraph module is not loaded on the classpath.

The exact dependencies to load are:

* the pdfCalligraph library itself
* the license key library

pdfCalligraph exposes a number of APIs so that it can be reached from iText Core,
but these APIs do not have to be called by code in applications that leverage pdfCalligraph.

This code will just work:

```java
// initial actions
LicenseKey.loadLicenseFile("/path/to/license.xml");
Document arabicPdf = new Document(new PdfDocument(new PdfWriter("/path/to/output.pdf")));

// create a font, and make it the default for the document
PdfFont f = PdfFontFactory.createFont(FilePath.getFont("/path/to/arabicFont.ttf"));
arabicPdf.setFont(f);

// add content: السلام عليكم (as-salaamu 'aleykum - peace be upon you)
arabicPdf.add(new Paragraph("\u0627\u0644\u0633\u0644\u0627\u0645 \u0639\u0644\u064A\u0643\u0645"));

arabicPdf.close();
```

iText will only attempt to apply advanced shaping in a text on the characters that constitute a majority
[footnote: technically, the plurality https://en.wikipedia.org/wiki/Plurality_(voting) ] 
of characters of that text. This can be overridden by explicitly setting the script for a layout element.
This is done as follows:

```java
PdfFont f = PdfFontFactory.createFont(FilePath.getFont("/path/to/unicodeFont.ttf"));
Paragraph mixed = new Paragraph("The concept of \u0915\u0930\u094D\u092E (karma) is at least as old as the Vedic texts.");
mixed.setFont(f);

mixed.setProperty(Property.FONT_SCRIPT, Character.UnicodeScript.DEVANAGARI);
```

iText will of course fail to do shaping operations with the Latin text,
but it will correctly make the र (ra) into the combining diacritic form it assumes in consonant clusters.

However, this becomes more problematic when mixing two alphabets that both require pdfCalligraph logic.
Therefore, it is generally wiser to separate the contents, when they appear in a single paragraph,
into a number of Text layout objects. This can even be automated:

```java
Map<UnicodeScript, PdfFont> fonts = new EnumMap<>(UnicodeScript.class);
String fontFolder = "C:/Windows/Fonts/";
fonts.put(UnicodeScript.LATIN, PdfFontFactory.createFont("/path/to/latinFont.ttf", PdfEncodings.IDENTITY_H, true));
fonts.put(UnicodeScript.TAMIL, PdfFontFactory.createFont("/path/to/tamilFont.ttf", PdfEncodings.IDENTITY_H, true));

String input = "Translation: \u0BAE\u0BC1\u0BA9\u0BCD\u0BA9\u0BC7\u0BB1\u0BCD\u0BB1\u0BAE\u0BCD means 'improvement' !";

Paragraph para = new Paragraph("");

StringBuilder build = new StringBuilder();
UnicodeScript script = UnicodeScript.of(input.charAt(0));
for (char unicodePoint : input.toCharArray()) {
	if (UnicodeScript.of(unicodePoint).equals(script) || UnicodeScript.of(unicodePoint).equals(UnicodeScript.COMMON)) {
		build.append(unicodePoint);
	} else {
		para.add(new Text(build.toString()).setFont(fonts.get(script)));
		build = new StringBuilder();
		build.append(unicodePoint);
		script = UnicodeScript.of(unicodePoint);
	}
}
para.add(new Text(build.toString()).setFont(fonts.get(script)));

doc.add(para);
doc.close();
```