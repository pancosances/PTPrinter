package org.apache.pdfbox.pdmodel.font;

import org.apache.pdfbox.cos.COSDictionary;

import java.io.IOException;

/**
 * Type 1 Multiple Master Font.
 *
 * @author Ben Litchfield
 */
public class PDMMType1Font extends PDType1Font
{
    /**
     * Creates an MMType1Font from a Font dictionary in a PDF.
     *
     * @param fontDictionary font dictionary
     */
    public PDMMType1Font(COSDictionary fontDictionary) throws IOException
    {
        super(fontDictionary);
    }
}
