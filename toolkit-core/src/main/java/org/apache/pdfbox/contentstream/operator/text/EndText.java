package org.apache.pdfbox.contentstream.operator.text;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorProcessor;
import org.apache.pdfbox.cos.COSBase;

import java.io.IOException;
import java.util.List;

/**
 * ET: End text.
 *
 * @author Laurent Huault
 */
public class EndText extends OperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> arguments) throws IOException
    {
        context.setTextMatrix(null);
        context.setTextLineMatrix(null);
        context.endText();
    }

    @Override
    public String getName()
    {
        return "ET";
    }
}
