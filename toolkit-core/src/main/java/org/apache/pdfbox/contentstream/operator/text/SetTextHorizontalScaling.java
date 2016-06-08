package org.apache.pdfbox.contentstream.operator.text;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorProcessor;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import java.io.IOException;
import java.util.List;

/**
 * Tz: Set horizontal text scaling.
 *
 * @author Ben Litchfield
 */
public class SetTextHorizontalScaling extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        COSNumber scaling = (COSNumber)arguments.get(0);
        context.getGraphicsState().getTextState().setHorizontalScaling(scaling.floatValue());
    }

    @Override
    public String getName()
    {
        return "Tz";
    }
}
