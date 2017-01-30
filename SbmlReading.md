Read SBML Documents 
====================
Parse an SBML Document 
-----------------------
The easiest way to parse SBML documents is using the [validator](http://sems.uni-rostock.de/trac/bives-core/wiki///ModelValidator). It will validate the file and provides you with the occurred error if the document is not valid. However, if the document is valid you get the SBML model for free:

```
#!java
// create a SBML validator
SBMLValidator validator = new SBMLValidator ();

// is that document valid?
if (!validator.validate (document))
	// if not: print the error (which is an exception)
	System.err.println (validator.getError ());

// get the document
SBMLDocument doc = validator.getDocument ();
```

You may provide the *```document```* as an XML string, a File object pointing to a file on the disk, a URL to a model on the internet, or an already parsed [TreeDocument](http://sems.uni-rostock.de/trac/xmlutils/wiki///HowTo#TreeDocument). Please read [ModelRetriever](http://sems.uni-rostock.de/trac/bives-core/wiki///ModelRetriever) to learn how to restrict the **access to local/remote files**. (see also /src/test/java/de/unirostock/sems/ParseExample.java)