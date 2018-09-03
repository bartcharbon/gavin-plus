package org.molgenis.inheritance.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Pedigree
{
	public abstract Subject getChild();

	public abstract List<Subject> getParents();

	public static Pedigree create(Subject child, List<Subject> parents)
	{
		return new AutoValue_Pedigree(child, parents);
	}

}
