package org.apache.pdfbox.contentstream.operator.state;

import android.graphics.Paint;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorProcessor;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import java.io.IOException;
import java.util.List;

/**
 * J: Set the line cap style.
 */
public class SetLineCapStyle extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        Paint.Cap lineCapStyle;
        switch(((COSNumber)arguments.get( 0 )).intValue())  {
            case 0:
                lineCapStyle = Paint.Cap.BUTT;
                break;
            case 1:
                lineCapStyle = Paint.Cap.ROUND;
                break;
            case 2:
                lineCapStyle = Paint.Cap.SQUARE;
                break;
            default:
                lineCapStyle = null;
        }

        context.getGraphicsState().setLineCap( lineCapStyle );
    }

    @Override
    public String getName()
    {
        return "J";
    }
}
