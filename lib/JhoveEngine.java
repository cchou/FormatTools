/*
 * DAITSS Copyright (C) 2007 University of Florida
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package shades;

import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.JhoveException;
import edu.harvard.hul.ois.jhove.Module;
import edu.harvard.hul.ois.jhove.OutputHandler;

public class JhoveEngine {
	/* the required application object for JHOVE */
	private App jhoveApp;
	private JhoveBase jhovebase;
	private static final int [] DATE    = {2007, 10, 23};
	
	/**
	 * class constructor.
	 */
	public JhoveEngine (String jhoveConfig) throws JhoveException
	{
	    jhoveApp = new App ("", "", DATE, "", "");
	    jhovebase = new JhoveBase();
	    jhovebase.setChecksumFlag(true); // retrieve the checksum
	    jhovebase.init(jhoveConfig, null);
	}
    
	/**
	 * Validate a file by invoking the protected dispatch() method of the
	 * JhoveBase class.
	 *
	 * @param module  		JHOVE module
	 * @param file    		The formatted file being validated
	 * @param outputFile    name of the output file
	 * @return False  if the module cannot perform validation
	 */
	public void validateFile (String moduleName, String file, String outputFile) throws Exception
	{
		Module module =  jhovebase.getModule(moduleName);
		OutputHandler handler = jhovebase.getHandler("xml");
		String[] files = new String[1];
		files[0] = file;
	    jhovebase.dispatch (jhoveApp, module, null, handler, outputFile, files);
	}
	
	/**
     * a quick test
     * depending on the run time arguments.
     * @param args   The run time arguments
     */
    public static void main( String [] args ) throws Exception {
    	JhoveEngine je = new JhoveEngine("/Users/Carol/tools/jhove/conf/jhove.conf");
    	je.validateFile("tiff-hul", "/Users/Carol/Workspace/describe/files/badMix_dateTimeCreated.tif", "output.xml");
    }
}
