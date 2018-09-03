package org.molgenis.inheritance.tree;

import org.molgenis.cgd.CGDEntry;
import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.Pedigree;

import java.util.List;

import static org.molgenis.cgd.CGDEntry.GeneralizedInheritance.*;

public class Black
{
	//no parent data available
	public static boolean filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree)
	{
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		boolean result = true;
		if (inheritance == DOMINANT)
		{
			result = true;
		}
		else if (inheritance == NOTINCGD || inheritance == RECESSIVE || inheritance == DOMINANT_OR_RECESSIVE)
		{
			if (gavinRecordsForGene.size() > 1)
			{
				result = true;
			}
			else
			{
				if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
				{
					return true;
				}
				else
				{
					return IF.filter();
				}
			}
		}
		else if (inheritance == X_LINKED)
		{
			result = true;
		}
		return result;
	}
}
