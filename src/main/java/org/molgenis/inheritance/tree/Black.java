package org.molgenis.inheritance.tree;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.InheritanceResult;
import org.molgenis.inheritance.model.Pedigree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.molgenis.inheritance.Checks.*;

public class Black
{
	private static final Logger LOG = LoggerFactory.getLogger(Black.class);

	//no parent data available
	public static InheritanceResult filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree)
	{
		LOG.debug("Entering 'Black' filtertree");
		InheritanceResult result;
		if (isDominant(gene))
		{
			result = InheritanceResult.create(true, "Bck1");
		}
		else if (isUnknownInheritanceMode(gene) || isRecessive(gene) || isDominantOrRecessive(gene))
		{
			if (isMultipleVariantsInOneGene(gavinRecordsForGene))
			{
				result = InheritanceResult.create(true, "Bck2");
			}
			else
			{
				if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
				{
					return InheritanceResult.create(true, "Bck3");
				}
				else
				{
					return IF.filter();
				}
			}
		}
		else if (isXLinked(gene))
		{
			result = InheritanceResult.create(true, "Bck4");
		}
		else
		{
			return IF.filter();
		}
		return result;
	}
}
