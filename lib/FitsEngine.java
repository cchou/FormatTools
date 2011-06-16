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
 *
 */

package shades;

import java.io.File;

import edu.harvard.hul.ois.fits.Fits;
import edu.harvard.hul.ois.fits.FitsOutput;
import edu.harvard.hul.ois.fits.exceptions.FitsConfigurationException;

public class FitsEngine {
	private Fits fits;
	
	public FitsEngine (String fitsConfig) throws FitsConfigurationException {
		fits = new Fits(fitsConfig);
	}
	public void validateFile(String input, String outputFile) throws Exception{
		File inputFile = new File(input);
		FitsOutput result = fits.examine(inputFile);	
		if(result.getCaughtExceptions().size() > 0) {
			for(Exception e: result.getCaughtExceptions()) {
				System.err.println("Warning: " + e.getMessage());
			}
		}
		result.saveToDisk(outputFile);
	}


	/**
     * a quick test
     * depending on the run time arguments.
     * @param args   The run time arguments
     */
    public static void main( String [] args ) throws Exception {
    	FitsEngine fits = new FitsEngine("/Users/Carol/tools/fits-0.2.6");
    	fits.validateFile("/Users/Carol/work/testdata/image/j2k/SW00002210.jp2", "output.xml");
    }
}
