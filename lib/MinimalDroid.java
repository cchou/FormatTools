package shades;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import uk.gov.nationalarchives.droid.FileFormatHit;
import uk.gov.nationalarchives.droid.IdentificationFile;
import uk.gov.nationalarchives.droid.binFileReader.AbstractByteReader;
import uk.gov.nationalarchives.droid.binFileReader.ByteReader;
import uk.gov.nationalarchives.droid.signatureFile.FFSignatureFile;
import uk.gov.nationalarchives.droid.xmlReader.SAXModelBuilder;

/**
 * A simple JAVA interface for DROID.  Used for simple file format identification
 * @author carol
 *
 */
public class MinimalDroid {
	public static final String SIGNATURE_FILE_NS = "http://www.nationalarchives.gov.uk/pronom/SignatureFile";
	    
	private FFSignatureFile mySigFile;
	
	public MinimalDroid(String sigFile) {
		try {
			mySigFile = parseSigFile(sigFile);
	        mySigFile.prepareForUse();
		} catch (Exception e) {
	        mySigFile = null;
	    }
	}
	
	/**
	 * identify the file by looking through the internal signature in the signature file
	 * @param filename
	 */
	public HashMap<String,String> IdentifyFile(String filename) {
		IdentificationFile idFile = new IdentificationFile(filename);
        ByteReader testFile = null;
        try {
            testFile = AbstractByteReader.newByteReader(idFile);
        } catch(OutOfMemoryError e) {
            testFile = AbstractByteReader.newByteReader(idFile, false);
            testFile.setErrorIdent();
            testFile.setIdentificationWarning("The application ran out of memory while loading this file (" + e.toString()+")");
        }
        
        try {
            mySigFile.runFileIdentification(testFile);
        } catch(Exception e) {
            testFile.setErrorIdent();
            testFile.setIdentificationWarning("Error during identification attempt: " + e.toString());
        }
        
        System.out.println("==================================");
     
        //display file classification and any warning
        System.out.println("     " + filename);
        if(testFile.getIdentificationWarning().length()>0)
            System.out.println("with warning: "+testFile.getIdentificationWarning());
        
        
        HashMap<String, String> puids = new HashMap<String, String>();
        //display list of hits
        for(int ih=0; ih<testFile.getNumHits(); ih++) {
        	FileFormatHit ht = testFile.getHit(ih);
            String specificityDisplay = ht.isSpecific()?"specific":"generic";
            System.out.println("          " +ht.getHitTypeVerbose() + " " + specificityDisplay + 
            		" hit for " + ht.getFileFormat().getName() + " [MIME_TYPE: " + ht.getMimeType() + "] "
            		+ "  [PUID: " + ht.getFileFormat().getPUID()+ "]");
            if(testFile.getHit(ih).getHitWarning().length()>0) {
                System.out.println("               WARNING: " + testFile.getHit(ih).getHitWarning());
            }
            // get the PUID of those matched formats
            puids.put(testFile.getHit(ih).getFileFormat().getPUID(), specificityDisplay);
        }
        
        return puids;
    }
	
	/**
	 * parse droid signature file.  Required before any format identification.
	 * @param theFileName
	 * @return
	 * @throws Exception
	 */
	public FFSignatureFile parseSigFile(String theFileName) throws Exception{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser saxParser = factory.newSAXParser();
        XMLReader parser = saxParser.getXMLReader();
        SAXModelBuilder mb = new SAXModelBuilder();
        mb.setupNamespace(SIGNATURE_FILE_NS, true);
        parser.setContentHandler( mb );
        
        //read in the XML file
        java.io.BufferedReader in = new java.io.BufferedReader(
        	new java.io.InputStreamReader(new java.io.FileInputStream(theFileName),"UTF8"));
        parser.parse( new InputSource(in) );
        return (FFSignatureFile)mb.getModel();
    }
	
	/**
     * a quick test
     * depending on the run time arguments.
     * @param args   The run time arguments
     */
    public static void main( String [] args ) throws Exception {
       MinimalDroid sp = new MinimalDroid("/home/carol/Desktop/droid-1.1/DROID_SignatureFile_V12.xml");
       sp.IdentifyFile("/home/carol/tiff2jp2.odt");
       sp.IdentifyFile("/home/carol/ootest.csv");
       sp.IdentifyFile("/home/carol/recognizedFormats.xls");
    }
}
