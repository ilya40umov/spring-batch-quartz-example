package org.sbq.batch.exceptions;

/**
 * @author ilya40umov
 */
public class TransientException extends RuntimeException
{
    public TransientException()
    {
    }

    public TransientException(String message)
    {
        super(message);
    }

    public TransientException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TransientException(Throwable cause)
    {
        super(cause);
    }
}
