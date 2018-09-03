package org.molgenis.inheritance.tree;

import org.molgenis.cgd.CGDEntry;
import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.model.Gender;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;

import java.util.List;

import static org.molgenis.cgd.CGDEntry.GeneralizedInheritance.*;

//One parent, affected
public class Green
{
	public static boolean filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		boolean result;
		Subject parent = pedigree.getParents().get(0);
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		if (inheritance == DOMINANT)
		{
			result = true;
		}
		else if (inheritance == NOTINCGD || inheritance == RECESSIVE || inheritance == DOMINANT_OR_RECESSIVE)
		{
			if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				result = true;
			}
			else if (gene.getNrOfVariants() > 0)
			{
				if (!Checks.subjectHasVariant(gavinRecord, parent))
				{
					result = true;
				}
				else
				{
					result = IF.filter();
				}
			}
			else
			{
				result = IF.filter();
			}
		}
		else if (inheritance == X_LINKED)
		{
			if (pedigree.getChild().getGender() != Gender.MALE)
			{
				result = true;
			}
			else if (parent.getGender() == Gender.MALE && !parent.isAffected())
			{
				result = true;
			}
			else if (!Checks.subjectHasVariant(gavinRecord, parent))
			{
				result = true;
			}
			else
			{
				result = IF.filter();
			}
		}
		else
		{
			result = IF.filter();
		}
		return result;
	}
}
