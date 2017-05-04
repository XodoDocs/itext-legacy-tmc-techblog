# About conversion

Data conversion is the conversion of computer data from one format to another. Throughout a computer environment, data is encoded in a variety of ways. For example, computer hardware is built on the basis of certain standards, which requires that data contains, for example, parity bit checks. Similarly, the operating system is predicated on certain standards for data and file handling. Furthermore, each computer program handles data in a different manner. Whenever any one of these variables is changed, data must be converted in some way before it can be used by a different computer, operating system or program. Even different versions of these elements usually involve different data structures. For example, the changing of bits from one format to another, usually for the purpose of application interoperability or of capability of using new features, is merely a data conversion. Data conversions may be as simple as the conversion of a text file from one character encoding system to another; or more complex, such as the conversion of office file formats, or the conversion of image and audio file formats.

# Assumptions

For the purposes of this article we define two axis along which any document format can be defined.
First "richness", a format is said to be rich if it can contain more than plain text. In this regard we consider json to be a poor format, although it can contain urls that reference external sources, and urls to reference images, it requires external interpretation. Pdf is considered a rich format. A pdf document can contain images, links, videos, tables, various fonts, colors etc. Html (for the purposes of this paper) is (semi-)rich. There is a canonical interpretation for how certain tags ought to be rendered (e.g. strong, italic, underline, paragraph, etc). This is more than can be said about a random json object, or xml snippet. However, when push comes to shove it is ultimately nothing more than plaintext backed up by some standards.

Second we look at "structure". A format is said to be structured if a logical (user-intuitive) structure can be derived from the work, or is already present in the work. HTML is a structured format, as elements typically embody entities users can relate to (paragraph, table, image, etc). (untagged) Pdf is an unstructured format, as a pdf document typically only contains the instructions needed to render the document in a viewer.

In an untagged pdf document there is no concept of "title" or "table", just instructions that draw text, lines, and images. (Although with tagged pdf documents, you can super-impose this structure upon a document.) In a tagged document, the pdf is enriched by adding a structure tree. This tree does contain the logical layout of the document (it groups objectstreams and labels them). Doing so enables the reader to have a clear idea of where to find the titles, paragraphs, images, etc.

# What iText can do

Naturally, the conversion from structured to unstructured is easy. Discarding information is always easy.
Our addon pdfHTML is a good example of this, going from a rich structured format to a rich unstructured format.
Using external libraries it is perfectly possible to convert from .doc and .docx to .pdf.

# What iText does not do

iText can not convert from (untagged) .pdf to HTML. Doing so would require iText to find structure in an unstructured document. The simple task of finding tables, and correctly identifying rows, columns and merged cells is already considered a topic of research. Correctly identifying all possible realizations of varied media is no mean feat.

Similarly, iText can not convert from poor formats (json, xml, raw text) to pdf. Since iText would then have to start adding style attributes (font, font-family, size, layout, etc) where there was none to begin with. And even though some effort could be made in defining a default, it would not take long for someone to come along with a perfectly valid usecase for wanting something other than the default.

# But I have *some software kit* installed, and it converts pdf to *whatnot* perfectly!

Typical example here is software that claims to convert html to pdf. And although the software can achieve the same look (arguably), it certainly can't inject structure. You'll find that these tools more often than not simply try to mimic the visual appearance of a .pdf, rather than attempt to divine meaning. This typically means that the resulting html document can no longer be used afterwards as input to any process that expects valid/sensible html markup.

# Conclusion

Ultimately it comes down to quality. If you want a lossy, stupid conversion, feel free to use a stupid tool. If you need software that is built to adhere to the standards, and deliver high-performance, high-througput document processing, use iText.