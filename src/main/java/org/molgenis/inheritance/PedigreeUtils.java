package org.molgenis.inheritance;

import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;

public class PedigreeUtils
{
	public static Subject getAffectedParent(Pedigree pedigree)
	{
		//FIXME: what to do if both unaffected?
		if (pedigree.getFather().isAffected())
		{
			return pedigree.getFather();
		}
		if (pedigree.getMother().isAffected())
		{
			return pedigree.getMother();
		}

		throw new RuntimeException("No affected parent found!");
	}

	public static Subject getUnaffectedParent(Pedigree pedigree)
	{
		//FIXME: what to do if both unaffected?
		if (!pedigree.getFather().isAffected())
			{
				return pedigree.getFather();
			}
		if (!pedigree.getMother().isAffected())
		{
			return pedigree.getMother();
		}

		throw new RuntimeException("No unaffected parent found!");
	}
}
