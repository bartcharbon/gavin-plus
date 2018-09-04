package org.molgenis.inheritance;

import org.molgenis.inheritance.exception.BothParentsAffectedException;
import org.molgenis.inheritance.exception.NoUnaffectedParentException;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;

public class PedigreeUtils
{
	private PedigreeUtils()
	{
	}
	public static Subject getAffectedParent(Pedigree pedigree)
	{
		//FIXME: what to do if both unaffected?
		if (pedigree.getFather().isAffected() && !pedigree.getMother().isAffected())
		{
			return pedigree.getFather();
		}
		if (!pedigree.getFather().isAffected() && pedigree.getMother().isAffected())
		{
			return pedigree.getMother();
		}
		else if (pedigree.getFather().isAffected() && pedigree.getMother().isAffected())
		{
			//FIXME
			throw new BothParentsAffectedException("Both parents are affected unclear how to proceed");
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

		throw new NoUnaffectedParentException("No unaffected parent found!");
	}
}
