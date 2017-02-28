# Are PDF signatures shattered?

Last week there was a huge buzz surrounding SHA-1. The hashing algorithm has been deemed unsafe to use for a long time now, but recently a team of researchers was able to break SHA-1. The attack relied on a PDF file containing an image which they exploited to generate a collision. A collision can be quite scary as it can allow an attacker to generate two files that have the same digest value. If a victim was to sign one of these files using SHA-1, the signed digest would be applicable to both documents. Meaning that you could validate both documents using the same signature. It would appear as if the victim signed both documents and not just one of them. You might imagine that this can be abused when signing contracts.

Hoiw does this affect PDF and iText? And how should you deal with this?


## Does this mean that PDF is an unsafe format?

Of course not. The issue here is that SHA-1 has been broken, not PDF. The PDF specification explicitly allows for different hashing algorithms, like the more secure SHA-256 or SHA-512, to be used. SHA-1 has been officially deprecated since 2011 by the NIST and it has been deemed unsafe to use as early as 2005. Developers and businesses who deal with digital signatures and security know this and (should) have moved on years ago. People surprised by this research and its result, weren't paying much attention to why they should or shouldn't use a hashing algorithm.

The upcoming PDF 2.0 specification* has dealt with this some time ago by deprecating its use:

> "NOTE The use of MD5 and SHA1 is deprecated."
> DigestMethod entry in Table 255, PDF 2.0 specification (ISO 32000-2).

*PDF 2.0 has been in the works for the past few years and is due for release this year, before the summer.



## How can I future proof my signatures?

## How can I fix my SHA-1 signatures?

## Conclusion

