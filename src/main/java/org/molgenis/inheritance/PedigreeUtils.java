package org.molgenis.inheritance;

import org.molgenis.inheritance.model.Gender;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;

public class PedigreeUtils
{
	public static Subject getFather(Pedigree pedigree)
	{
		for (Subject subject : pedigree.getParents())
		{
			if (subject.getGender() == Gender.MALE)
			{
				return subject;
			}
		}
		return null;
	}

	public static Subject getAffectedParent(Pedigree pedigree)
	{
		for (Subject subject : pedigree.getParents())
		{
			if (subject.isAffected())
			{
				return subject;
			}
		}
		throw new RuntimeException("No affected parent found!");
	}

	public static Subject getUnaffectedParent(Pedigree pedigree)
	{
		for (Subject subject : pedigree.getParents())
		{
			if (!subject.isAffected())
			{
				return subject;
			}
		}
		throw new RuntimeException("No unaffected parent found!");
	}
}
