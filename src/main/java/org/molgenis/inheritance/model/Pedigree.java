package org.molgenis.inheritance.model;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Pedigree
{
	public abstract Subject getChild();

	@Nullable
	public abstract Subject getFather();

	@Nullable
	public abstract Subject getMother();

	public static Pedigree create(Subject child, Subject father, Subject mother)
	{
		return new AutoValue_Pedigree(child, father, mother);
	}

}
