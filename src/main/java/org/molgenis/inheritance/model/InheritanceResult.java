package org.molgenis.inheritance.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class InheritanceResult
{

	public abstract boolean isInheritance();

	public abstract String getExitCode();

	public static InheritanceResult create(boolean inheritance, String exitCode)
	{
		return new AutoValue_InheritanceResult(inheritance, exitCode);
	}
}
