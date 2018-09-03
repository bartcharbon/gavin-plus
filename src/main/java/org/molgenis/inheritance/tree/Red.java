package org.molgenis.inheritance.tree;

import org.molgenis.cgd.CGDEntry;
import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.PedigreeUtils;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.Pedigree;

import java.util.List;

import static org.molgenis.cgd.CGDEntry.GeneralizedInheritance.*;

//Two parents at least one affected
public class Red
{
	public static boolean filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		boolean result = true;
		if (inheritance == DOMINANT)
		{
			if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = true;
			}
			else if (Checks.isNonPenetrant())
			{
				result = true;
			}
			else if (Checks.isOccuringInAffectedParent(gavinRecord, pedigree))
			{
				result = true;
			}
			else
			{
				result = IF.filter();
			}

		}
		else if (inheritance == NOTINCGD || inheritance == RECESSIVE)
		{
			if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				if (!Checks.isHomozygote(gavinRecord, PedigreeUtils.getUnaffectedParent(pedigree)))
				{
					result = true;
				}
				else
				{
					result = IF.filter();
				}
			}
			else if (Checks.isCompound(gavinRecord, gavinRecordsForGene, pedigree))
			{
				result = true;
			}
			else
			{
				result = IF.filter();
			}
		}
		else if (inheritance == X_LINKED)
		{
			if (PedigreeUtils.getFather(pedigree).isAffected())
			{
				result = true;
			}
			else if (!Checks.subjectHasVariant(gavinRecord, PedigreeUtils.getFather(pedigree)))
			{
				result = true;
			}
			else
			{
				result = IF.filter();
			}
		}
		else if (inheritance == DOMINANT_OR_RECESSIVE)
		{
			if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = true;
			}
			else if (Checks.isNonPenetrant())
			{
				result = true;
			}
			else if (Checks.isOccuringInAffectedParent(gavinRecord, pedigree))
			{
				result = true;
			}
			else if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				if (!Checks.isHomozygote(gavinRecord, PedigreeUtils.getUnaffectedParent(pedigree)))
				{
					result = true;
				}
				else
				{
					result = IF.filter();
				}
			}
			else if (Checks.isCompound(gavinRecord, gavinRecordsForGene, pedigree))
			{
				result = true;
			}
			else
			{
				result = IF.filter();
			}
		}
		return result;
	}
}
