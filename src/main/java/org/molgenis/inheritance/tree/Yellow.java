package org.molgenis.inheritance.tree;

import org.molgenis.cgd.CGDEntry;
import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.PedigreeUtils;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.Pedigree;

import java.util.List;

import static org.molgenis.cgd.CGDEntry.GeneralizedInheritance.*;

//One parent, unaffected
public class Yellow
{
	public static boolean filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		boolean result = true;
		if (inheritance == DOMINANT)
		{
			if (Checks.isNonPenetrant())
			{
				result = true;
			}
			else if (Checks.isDeNovo(gavinRecord, pedigree))
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
				result = true;
			}
			else if (gavinRecordsForGene.size() > 1)
			{
				//FIXME: is current variant part of this list
				if (Checks.subjectHasVariant(gavinRecord, pedigree.getParents().get(0)) && Checks.subjectHasVariant(
						gavinRecordsForGene.get(0), pedigree.getParents().get(0)))
				{
					//FIXME how to get the other variant correctly and what if there are three or more
					result = true;
				}
			}
			else
			{
				result = IF.filter();
			}
		}
		else if (inheritance == X_LINKED)
		{
			if (PedigreeUtils.getFather(pedigree) != null)
			{
				if (PedigreeUtils.getFather(pedigree).isAffected())
				{
					result = true;
				}
				else if (Checks.subjectHasVariant(gavinRecord, PedigreeUtils.getFather(pedigree)))
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
		else if (inheritance == DOMINANT_OR_RECESSIVE)
		{
			if (Checks.isNonPenetrant())
			{
				result = true;
			}
			else if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = true;
			}
			else if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				result = true;
			}
			else if (gavinRecordsForGene.size() > 1)
			{
				//FIXME: is current variant part of this list
				if (Checks.subjectHasVariant(gavinRecord, pedigree.getParents().get(0)) && Checks.subjectHasVariant(
						gavinRecordsForGene.get(0), pedigree.getParents().get(0)))
				{
					//FIXME how to get the other variant correctly and what if there are three or more
					result = true;
				}
			}
			else
			{
				result = IF.filter();
			}
		}
		return result;
	}
}
