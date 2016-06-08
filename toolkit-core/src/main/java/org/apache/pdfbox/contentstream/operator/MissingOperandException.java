package org.apache.pdfbox.contentstream.operator;

import org.apache.pdfbox.cos.COSBase;

import java.io.IOException;
import java.util.List;

/**
 * Throw when a PDF operator is missing required operands.
 */
public final class MissingOperandException extends IOException
{
    public MissingOperandException(Operator operator, List<COSBase> operands)
    {
        super("Operator " + operator.getName() + " has too few operands: " + operands);
    }
}