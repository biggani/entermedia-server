package org.openedit.entermedia.publishing;

public class PublishResult
{
	protected String fieldErrorMessage;
	protected boolean fieldComplete;
	protected boolean fieldPending;

	public boolean isPending()
	{
		return fieldPending;
	}

	public void setPending(boolean inPending)
	{
		fieldPending = inPending;
	}

	public boolean isComplete()
	{
		return fieldComplete;
	}

	public void setComplete(boolean inComplete)
	{
		fieldComplete = inComplete;
	}

	public String getErrorMessage()
	{
		return fieldErrorMessage;
	}

	public void setErrorMessage(String inErrorMessage)
	{
		fieldErrorMessage = inErrorMessage;
	}
	public boolean isError()
	{
		return fieldErrorMessage != null;
	}
}
