# pdfChain : blockchain for the masses

## what is a blockchain?

A blockchain is a distributed database that is used to maintain a continuously growing list of records, called blocks. 
Each block contains a timestamp and a link to a previous block. 
A blockchain is typically managed by a peer-to-peer network collectively adhering to a protocol for validating new blocks. 
By design, blockchains are inherently resistant to modification of the data. 
Once recorded, the data in any given block cannot be altered retroactively without the alteration of all subsequent blocks and a collusion of the network majority. 
Functionally, a blockchain can serve as "an open, distributed ledger that can record transactions between two parties efficiently and in a verifiable and permanent way. 
The ledger itself can also be programmed to trigger transactions automatically."

![Figure 0: a blockchain being built](Images/blockchain_01.png)

## why should you use it?

A blockchain supersedes older technology that deals with authentication and non-repudiation.
First, there are many ways you can sign a document.
Typically by "signing" we mean creating a hash of a document and storing it.
With a blockchain, the useful part is that once such a hash is stored, it can not be changed or deleted. This gives you two advantages:

1. The hash itself identifies the file from which it was computed
2. The fact that your hash is in the blockchain gives you a point in time when the operation was done.

Later you can say: 
Hey, I’ve created this hash on 10 Oct 2016: here is the transaction in the blockchain which contains the hash. I’ve created it according to this formula from this file. 
Now any person can take your file and compute the hash again and verify that it matches the one stored in the blockchain. 
All this works because:

1. It is very easy to compute the hash from a file but very difficult to craft a similar file which will produce exactly the same hash.
2. It is practically impossible to change the data stored inside blockchain.
3. Every transaction in the blockchain has a timestamp, so having the transaction we know exactly when it was done.

The default iText implementation of the blockchain concept is specifically geared towards pdf documents. It stores:
 - a *hash value* of the document
 - the name of the algorithm that was used for *hashing*
 - a *signed hash value* of the document
 - the name of the algorithm that was used for *signing*
 - the pdf ID array
 
This allows you not only to store hash values of documents, but also to digitally sign them.
Being able to swap the hashing algorithm (in case of hashing algorithms becoming outdated) enables LTV (long term validation).

## what does iText provide?

### simple document security with blockchain

The interfaces we impose on blockchain implementations are minimal, yet they provide us with the needed abstraction to enable us to build complex applications and workflows on top of them.
We abstract a blockchain as a multimap, allowing end-users to store an object (represented by Record, which is `HashMap<String, Object>`) and tying it to a key (String).

```java
public interface IBlockChain {

    /**
     * Put data on the blockchain
     *
     * @param key  the key being used to put the data on the blockchain
     * @param data the data being put on the blockchain
     */
    public boolean put(String key, Record data);

    /**
     * Get data from the blockchain
     *
     * @param key the key being queried
     * @return
     */
    public List<Record> get(String key);

    /**
     * Get all data from the blockchain
     * @return
     */
    public List<Record> all();
}
```

### concrete implementation using JSON-RPC and MultiChain

As a proof of concept we have provided an implementation of the interface IBlockchain using JSON-RPC (remote procedure call) and MultiChain.
If you want to learn more about setting up a blockchain instance with MultiChain, check out their website for more resources.
In particular the getting started guide at https://www.multichain.com/getting-started/

## example(s)

putting a document on the blockchain

```java
	// define a multichain instance
	IBlockChain mc = new MultiChain(
                "http://127.0.0.1",                                  // address of another node on the chain
                4352,                                                // port
                "chain1",                                            // name of the blockchain
                "stream1",                                           // name of the stream
                "multichainrpc",                                     // username
                "BHcXLKwR218R883P6pjiWdBffdMx398im4R8BEwfAxMm");     // password

	// provide the details about signing and hashing
	sign.AbstractExternalSignature sgn = new sign.DefaultExternalSignature(new File("path_to_keystore"), "demo", "password");

	// file being handled
	File inputFile = new File("input.pdf");

	// instantiate blockchain
	pdfchain.PdfChain blockchain = new pdfchain.PdfChain(mc, sgn);
	
	blockchain.put(inputFile);
```

retrieving document information from the blockchain

```java
	IBlockChain mc = new MultiChain(
                "http://127.0.0.1",
                4352,
                "chain1",
                "stream1",
                "multichainrpc",
                "BHcXLKwR218R883P6pjiWdBffdMx398im4R8BEwfAxMm");

	sign.AbstractExternalSignature sgn = new sign.DefaultExternalSignature(new File("path_to_keystore"), "demo", "password");

	File inputFile = new File("input.pdf");

	pdfchain.PdfChain blockchain = new pdfchain.PdfChain(mc, sgn);
	for (Map<String, Object> docEntry : blockchain.get(inputFile)) {
		for (Map.Entry<String, Object> entry : docEntry.entrySet())
			System.out.println(padRight(entry.getKey(), 32) + " : " + entry.getValue());
		System.out.println("");
	}
```

```java
blocktime                        : 1499691151
id2                              : �Ʊ��B�}ә`�-o�R
id1                              : z�L{�Wd=����G�
publishers                       : [14pwDpkcfRvSiw6DJWpP7RdcYgv5NfRRn6Dudr]
txid                             : b0092d7eb967ac2e45671742ddf1a0a96bc049a4bbfe3528888b6d9ff396b7a2
hsh                              : ��B�����șo�$'�A�d��L���xR�U
confirmations                    : 22
key                              : ï¿½ï¿½Bï¿½ï¿½ï¿½ï¿½ï¿½È oï¿½$'ï¿½Aï¿½dï¿½ï¿½Lï¿½ï¿½ï¿½xRï¿½U
shsh                             : <garbled>
```