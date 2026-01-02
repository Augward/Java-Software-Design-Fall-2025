# Document Processing and Conversion Tool


Imagine that you work for a company that produces books about computer hardware and software.
This company has a large collection of digital books that are encoded in [DocBook](https://tdg.docbook.org) XML format.  

You have been asked to write a program that can convert the XML DocBook format to other formats as specified by indepentently supplied plugins.

Task:
Convert the DocBook XML format to the following formats using a separate plugin for each conversion type:

| JavaFileName           | Output Format                                      | Description                                                                                                                |
|------------------------|----------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| DocBookToMarkdown.java | Github compatible Markdown                         | Convert the DocBook XML format to a Github compatible Markdown format.                                                     |
| DocBookToASCII.java    | Plain ASCII text                                   | Convert the DocBook XML format to a plain ASCII text format.                                                               |
| DocBookToTOCJSON.java  | JSON file of ONLY the Title and top level sections | Convert the DocBook XML format to a JSON file of ONLY the Title and top level sections (i.e. a table of contents summary). |


Each plugin should be implemented as a separate class that implements an interface that must be named `FileConverter`.  The FileConverter interface has a single method that takes two parameters:
`void convert(Path input, Path output)`. 

The main class `DocumentConverterDriver`  should be able to load and run each of the plugins one at a time when they are specified on the commandline.


* Convert contents from the input file and write results to the output file.
    * Implementations are responsible for choosing formats and handling errors.
      */
      void convert(Path input, Path output) throws Exception;

---

### HINTS:

#### Java tools for enhanced testing and verification
The file [DumpDocBookXMLObjectModelDriver.java](../../../../Downloads/conversion_polymorphism_starter_code/conversion_polymorphism-main/conversion_polymorphism-main/src/DumpDocBookXMLObjectModelDriver.java) contains a main method that
can be used to dump the contents of a DocBook XML file to the console.  This can be used to explore the API of the DocBook
XML Object Model and determine how to make other more useful conversions.  This is a good starting point for testing and
verification.  It is recommended to break the complex_document.xml file into smaller files containing only one XML based
feature (i.e. list, bold text, section, etc... ) that can be tested and verified independently.

#### Python tools for enhanced testing and verification

The following represents possible ways of testing and verifying the correctness of your program.  You are not required to use any of the suggestions below.

Reversing the process to take simple unit markdown representations and convert it to DocBook XML is a good starting point for generating unit tests.

The python tool called [pandoc](https://pandoc.org) can be used to convert markdown to docbook xml format.

```bash
python3 -m venv ~/venv/docbook
source ~/venv/docbook/bin/activate
pip install pandoc
```

```python
import pandoc
# Python script to convert markdown to docbook xml format
pandoc -f markdown -t docbook -s simple.md -o simple.xml
```

