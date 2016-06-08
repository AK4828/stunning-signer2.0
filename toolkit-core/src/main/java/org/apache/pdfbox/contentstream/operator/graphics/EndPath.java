package org.apache.pdfbox.contentstream.operator.graphics;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import java.io.IOException;
import java.util.List;

/**
 * n End the path.
 *
 * @author Ben Litchfield
 */
public final class EndPath extends GraphicsOperatorProcessor
{
    @Override
    public void process(Operator operator, List<COSBase> operands) throws IOException
    {
        context.endPath();
    }

    @Override
    public String getName()
    {
        return "n";
    }
}
