# Are PDF signatures shattered?

Last week there was a huge buzz surrounding SHA-1. The hashing algorithm has been deemed unsafe to use for a long time now, but recently a team of researchers was able to break SHA-1. The attack relied on a PDF file containing an image which they exploited to generate a collision. A collision can be quite scary as it can allow an attacker to generate two files that have the same digest value. If a victim was to sign one of these files using SHA-1, the signed digest would be applicable to both documents. This means that you could validate both documents using the same signature. It would appear as if the victim signed both documents and not just one of them. You might imagine that this can be abused when signing contracts.

How does this affect PDF and iText? And how should you deal with this?


## Does this mean that PDF is an unsafe format?

Of course not. The issue here is that SHA-1 has been broken, not PDF. The PDF specification explicitly allows for different hashing algorithms to be used, like the more secure SHA-256 or SHA-512. Additionally, SHA-1 has been officially deprecated since 2011 by the NIST and it has been deemed unsafe to use as early as 2005. Developers and businesses who deal with digital signatures and security know this and (should) have moved on years ago. People surprised by this research and its result, weren't paying much attention to why they should or shouldn't use specific hashing algorithms.

Furthermore in 2009, the PAdES standard mentioned that "The use of SHA-1 is being phased out in some countries and hence the use of other hashing algorithms is recommended". We have been warning developers using the LGPL-MPL versions of iText (dating from 2009) for many years that they shouldn't use some of the signature types that we knew would be deprecated in the PDF 2.0 specification — the long-awaited ISO 32000-2. In PDF 2.0, SHA-1 will be formally deprecated. This means that a PDF 2.0 writer should not use SHA-1 anymore, and a PDF 2.0 reader can reject signatures that still use SHA-1.

Does this mean that PDFs that were signed using the SHA-1 algorithm in the past suddenly become invalid? In principle, it is now proven that the contents of a PDF can be changed without invalidating the signature, if that signature signed a message digest that was created with SHA-1.


## Future proofing your signing processes

Very easily. Stop using SHA-1 and start using another algorithm. And start keeping an eye out for news about that algorithm and cryptography in general. Unfortunately cryptography is an ever evolving industry and there is no definitive algorithm you can use, so you should stay informed on the industry to guarantee that your signed documents are secure and reliable.
    

## How to fix existing SHA-1 signatures

Although it has been deprecated for almost 6 years now, I expect a lot of people still use SHA-1 when signing their documents. If you have a repository of PDF files that still rely on SHA-1, PAdES-4 allows you to add a Document Security Store (DSS) including Validation-Related Information (VRI), as well as a document time-stamp (DTS) signature. 

This process adds an additional signature through a time-stamp. It doesn't replace the original, SHA-1 signature, but it adds a new one. It is important to use a different, more recent hashing algorithm to create this signature. This way you can change the original contents to fit the original SHA-1 signature, but the second signature will be broken because the hashing algorithm you used in the time-stamp signature isn't SHA-1.

This procedure of adding a DSS and a document time-stamp should be repeated before the certificate of the last signature that was added expires, or when there are indications that the algorithms that were used, be it the cryptograph hash function or the encryption algorithm, could be jeopardized.

Our free e-book on digital signatures in PDF describes how to add a DSS to a signed document. You can download the e-book here.

```

```


## Conclusion

This attack doesn't change much for the PDF specification. PDF already anticipated that SHA-1 would have been broken as we can see in PAdES (2009) and PDF 2.0 (2017). We advise you to double check your PDF signatures and to apply a DSS when you've used SHA-1.