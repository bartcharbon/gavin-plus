package org.molgenis.inheritance.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Subject
{
	public abstract String getSampleId();

	public abstract boolean isAffected();

	public abstract Gender getGender();

	public static Subject create(String sampleId, boolean affected, Gender gender)
	{
		return new AutoValue_Subject(sampleId, affected, gender);
	}
}
