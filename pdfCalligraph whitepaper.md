Introduction
---

pdfCalligraph is an add-on module for iText 7, designed to seamlessly handle any kind of advanced shaping operations 
when writing textual content. Its main function is to correctly render complex writing systems 
such as the right-to-left Hebrew and Arabic scripts, 
and the various writing systems of the Indian subcontinent and its surroundings. 
In addition, it can also handle kerning and other optional features that can be provided by certain fonts in other writing systems.


A bit of iText history
---

The iText library was originally written in the context of Western European languages, 
and it was only designed to handle left-to-right alphabetic scripts. 
However, the writing systems of the world can be much more complex and varied 
than just a sequence of letters with no interaction. 
Supporting every type of writing system that humanity has developed is a tall order, 
but we strive to be a truly global company. As such, we are determined to come as close to that goal as technology allows us as long as there's a decent business case [NOTE: phrasing].

In earlier versions of iText, we were already able to render Chinese, Japanese, and Korean (CJK) glyphs in PDF documents, 
and had limited support for the right-to-left Hebrew and Arabic scripts. 
With iText 7, we took the next step and went on to create a module that could support the elusive Brahmic scripts, which are used mostly in the Indian subcontinent.

A bit of font & encoding history
---

TTF & OTF & Unicode

A bit of writing history
---

Over the last 5000+ years, humanity has created a plethora of writing systems. 
After an extended initial period of protowriting, when people tried to convey 
concepts and/or words through drawn images, a number of more abstract systems evolved. 
One of the most influential writing systems was the script developed 
and exported by the seafaring trade nation of Phoenicia. From it evolved, 
after extended periods of time, alphabets like Greek and its descendants (Latin, Cyrillic, Runes, etc), 
and the Aramaic abjad and its descendants (Arabic, Hebrew, Syriac, etc). 
Although this is a matter of scientific debate, it is possible that the Phoenician alphabet is also 
an ancestor of the entire Brahmic family of scripts, which contains Kannada, Thai, Telugu, 
and over a hundred other writing systems used primarily in South-East Asia and the Indian subcontinent.

The other highly influential writing system that is probably an original invention, the Han script, 
has descendants used throughout the rest of East Asia in the Chinese-Japanese-Korean spheres of influence.

A very brief introduction to the Arabic script
---

Arabic is writing system used for a large number of languages in the greater Middle East.
It is most prominently known from its usage for the Arabic language, 
and from that language's close association with Islam, since most religious Muslim literature is written in Arabic.
As a result, many other language communities in and around the culturally Arabic/Muslim sphere of 
influence have also adopted the Arabic script, like Farsi (Iran), Pashto (Afghanistan),
Mandinka (Senegal, Gambia), Malay (Malaysia, Indonesia, ...), etc.
Some of these communities have introduced new letters for the alphabet, 
often based on the existent ones, to account for sounds and features not found in the original Arabic language.

Arabic is an abjad, meaning that, in principle, only the consonants of a given word will be written. 
Like most other abjads, it is 'impure' in that the long vowels (/a:/, /i:/, /u:/) are also written, 
the latter two with the same character that is also used for /j/ and /w/. 
The missing information about the presence and and quality of short vowels must be filled in by the reader; 
hence, it is usually necessary for the user to actually know the language that is written 
in order to be able to fully pronounce the written text.

Standard Arabic has 28 characters, but there are only 16 base forms: a number of the characters are dotted variants of others.
These basic modification dots, called i'jam, have been all but mandatory in writing since at least the 11th century.

![i'jam distinguishes between otherwise identical forms (from Wikipedia.org)](./ijam.png)

Like the other Semitic abjads, Arabic is written from right to left, and does not have a distinction between upper and lower case. 
It is in all circumstances written cursively, making extensive use of ligatures to join letters together into words. 
As a result, all characters have several appearances, depending on whether or not they're adjoined to the previous and/or next letter.

![contextual variation of characters (from Wikipedia.org)](./mutations.png)

This concept is easy to illustrate with a hands-on example. The Arabic word 'aniq (meaning elegant) is written with the following letters:

![Arabic word 'aniq, not ligaturized](./typography/elegant%20arabic%20bad%20aniq.svg)

However, in writing, the graphical representation is shows marked differences. 

![Arabic word 'aniq, properly ligaturized](./typography/elegant%20arabic%20good%20aniq.svg)

The rightmost letter, alif, is unchanged, because by rule it does not join with any subsequent characters.
The leftmost three letters are joined to one another,
to the point where the character in the medial position is unrecognizable compared to its base form, 
save for the double dot underneath it. 

In the Unicode standard, the Arabic script is encoded into a number of ranges of Unicode points:
* The base forms for both Standard Arabic and a number of derived alphabets in Asia are located in U+0600–U+06FF (Arabic).
* Supplemental characters, mostly for African and European languages, are in U+0750–U+077F (Arabic Supplement) and U+08A0–U+08FF (Arabic Extended-A)
* Two ranges, U+FB50–U+FDFF (Arabic Pres. Forms-A) and U+FE70–U+FEFF (Arabic Pres. Forms-B),
that define unique Unicode points for all contextual appearances (isolated, initial, medial, final) of Arabic glyphs

A very brief introduction to the Brahmic scripts
---

So named because of their descent from the ancient alphabet called Brahmi,
the Brahmic scripts are a large family of writing systems used primarily in India and South-East Asia.
The family is split into a Northern and a Southern branch.
The Northern branch is characterized by the use of half-characters in consonant clusters,

They are abugidas, i.e. consonants are written with an implied vowel,
and only deviations from that implied vowel (usually a short /a/ or schwa) are marked.

Support
---

The initial release of pdfCalligraph provided support for the following scripts:

* Arabic, except tashkil
* Hebrew (right-to-left)
* Devanagari, used for Hindi, Marathi, Sindhi, etc
* Tamil

The second release, version 1.0.1, expanded this support to:

* Arabic, including tashkil
* Gurmukhi, used for writing Punjabi
* Kannada

Using pdfCalligraph
---

Using pdfCalligraph is exceedingly easy: you just load the correct binaries into your project, 
make sure your valid license file is loaded, 
and iText 7 will automatically go into the pdfCalligraph code when text instructions are encountered 
that contain Indic texts, or a script that is written from right to left.

Even though it exposes a number of API methods, this is not necessary. 
The iText layout module will automatically look for the pdfCalligraph module on the classpath 
if Indic/Arabic text is encountered. If it is available, it will call pdfCalligraph's functionality 
to provide the correct glyph shapes to write to the PDF file. iText will not attempt any advanced shaping operations 
if the pdfCalligraph module is not loaded on the classpath.

The exact dependencies to load are:

* the pdfCalligraph library itself
* the license key library