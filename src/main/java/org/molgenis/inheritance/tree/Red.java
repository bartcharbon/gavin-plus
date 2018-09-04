package org.molgenis.inheritance.tree;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.PedigreeUtils;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.InheritanceResult;
import org.molgenis.inheritance.model.Pedigree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.molgenis.inheritance.Checks.*;

//Two parents at least one affected
public class Red
{
	private static final Logger LOG = LoggerFactory.getLogger(Red.class);

	public static InheritanceResult filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		LOG.debug("Entering 'Red' filtertree");
		InheritanceResult result;
		if (isDominant(gene))
		{
			if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = InheritanceResult.create(true, "Rd1");
			}
			else if (Checks.isNonPenetrant())
			{
				result = InheritanceResult.create(true, "Rd2");
			}
			else if (Checks.isOccuringInAffectedParent(gavinRecord, pedigree))
			{
				result = InheritanceResult.create(true, "Rd3");
			}
			else
			{
				result = IF.filter("Rd_IF_1");
			}

		}
		else if (isUnknownInheritanceMode(gene) || isRecessive(gene))
		{
			if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				if (!Checks.isHomozygote(gavinRecord, PedigreeUtils.getUnaffectedParent(pedigree)))
				{
					result = InheritanceResult.create(true, "Rd4");
				}
				else
				{
					result = IF.filter("Rd_IF_2");
				}
			}
			else if (Checks.isCompound(gavinRecord, gavinRecordsForGene, pedigree))
			{
				result = InheritanceResult.create(true, "Rd5");
			}
			else
			{
				result = IF.filter("Rd_IF_3");
			}
		}
		else if (isXLinked(gene))
		{
			if (PedigreeUtils.getFather(pedigree).isAffected())
			{
				result = InheritanceResult.create(true, "Rd6");
			}
			else if (!Checks.subjectHasVariant(gavinRecord, PedigreeUtils.getFather(pedigree)))
			{
				result = InheritanceResult.create(true, "Rd7");
			}
			else
			{
				result = IF.filter("Rd_IF_4");
			}
		}
		else if (isDominantOrRecessive(gene))
		{
			if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = InheritanceResult.create(true, "Rd8");
			}
			else if (Checks.isNonPenetrant())
			{
				result = InheritanceResult.create(true, "Rd9");
			}
			else if (Checks.isOccuringInAffectedParent(gavinRecord, pedigree))
			{
				result = InheritanceResult.create(true, "Rd10");
			}
			else if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				if (!Checks.isHomozygote(gavinRecord, PedigreeUtils.getUnaffectedParent(pedigree)))
				{
					result = InheritanceResult.create(true, "Rd11");
				}
				else
				{
					result = IF.filter("Rd_IF_5");
				}
			}
			else if (Checks.isCompound(gavinRecord, gavinRecordsForGene, pedigree))
			{
				result = InheritanceResult.create(true, "Rd12");
			}
			else
			{
				result = IF.filter("Rd_IF_6");
			}
		}
		else
		{
			result = IF.filter("Rd_IF_7");
		}
		return result;
	}
}
