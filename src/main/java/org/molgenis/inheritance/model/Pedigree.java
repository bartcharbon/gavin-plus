package org.molgenis.inheritance.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Pedigree
{
	public abstract Subject getChild();

	public abstract Subject getFather();

	public abstract Subject getMother();

	public static Pedigree create(Subject child, Subject father, Subject mother)
	{
		return new AutoValue_Pedigree(child, father, mother);
	}

}
