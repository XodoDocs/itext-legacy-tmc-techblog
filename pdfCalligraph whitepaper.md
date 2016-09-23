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
but we strive to be a truly global company. As such, we are determined to come as close to that goal as technology allows us *as long as there's a decent business case*.

In earlier versions of iText, we were already able to render Chinese-Japanese-Korean (CJK) glyphs in PDF documents, 
and had limited support for the right-to-left Hebrew and Arabic scripts. 
With iText 7, we took the next step and went on to create a module that could support the elusive Brahmic scripts, which are used mostly in the Indian subcontinent.

A bit of writing history
---

Over the last 5000+ years, humanity created a plethora of writing systems. 
After an extended initial period of protowriting, which tried to convey 
thoughts through drawn images, a number of more abstract systems evolved. 
One of the most influential writing systems was the alphabet developed 
and exported by the seafaring trade nation of Phoenicia. From it evolved, 
after extended periods of time, scripts like Greek and its descendants (Latin, Cyrillic, Runes, etc), 
and Aramaic and its descendants (Arabic, Hebrew, Syriac, etc). 
Although this is a matter of scientific debate, it is possible that the Phoenician alphabet is also 
an ancestor of the entire Brahmic family of scripts, which contains Kannada, Thai, Telugu, 
and over a hundred other writing systems used in South-East Asia.

The other highly influential 'original' writing system, the Han script, 
has descendants used throughout the rest of East Asia in the Chinese-Japanese-Korean spheres of influence.

A bit of font & encoding history
---

TTF & OTF

A very brief introduction to the Arabic script
---

Arabic is writing system used for a large number of languages in the greater Middle East area.
It is most prominently known from its usage for the Arabic language, and from that language's close association with Islam, since most religious Muslim literature is written in Arabic.
As a result, many other language communities in and around the culturally Arabic/Muslim sphere of 
influence have also adopted the Arabic script, like Farsi (Iran), Pashto (Afghanistan),
Mandinka (Senegal, Gambia), Malay (Malaysia, Indonesia, ...), etc.
Some of these communities have introduced new letters for the alphabet, often based on the existent ones, to account for sounds and features not found in the original Arabic language.

Arabic is an abjad, meaning that, in principle, only the consonants of a given word will be written. Like most other abjads, it is 'impure' in that the long vowels (/a:/, /i:/, /u:/) are also written, the latter two with the same character that is also used for /j/ and /w/. The missing information about the presence and and quality of short vowels must be filled in by the reader; hence, it is usually necessary for the user to actually know the language that is written in order to be able to fully pronounce the text.

Like the other Semitic abjads, Arabic is written from right to left, and does not have a distinction between upper and lower case. It is in all circumstances written cursively, making extensive use of ligatures to join letters together into words. All characters have several appearances, depending on whether or not they're adjoined to the previous and/or next letter.

Historically, there were 16 base characters in the Arabic script, although there are more consonant phonemes: hence, modifiers are heavily used to distinguish between two separate phonemes that share the same letter. These basic modification dots, called i'jam, have been all but mandatory in writing since at least the 11th century.

![Alt text](./ijam.png)

A very brief introduction to the Brahmic scripts
---

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