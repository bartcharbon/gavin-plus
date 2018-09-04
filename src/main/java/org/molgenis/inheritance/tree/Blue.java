package org.molgenis.inheritance.tree;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.PedigreeUtils;
import org.molgenis.inheritance.model.Gender;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.InheritanceResult;
import org.molgenis.inheritance.model.Pedigree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.molgenis.inheritance.Checks.*;

//Two parents both unaffected
public class Blue
{
	private static final Logger LOG = LoggerFactory.getLogger(Blue.class);

	public static InheritanceResult filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		LOG.debug("Entering 'Blue' filtertree");
		InheritanceResult result;
		if (isDominant(gene))
		{
			if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = InheritanceResult.create(true, "Bl1");
			}
			else if (Checks.isNonPenetrant())
			{
				result = InheritanceResult.create(true, "Bl2");
				;
			}
			else
			{
				result = IF.filter("Bl_IF_1");
			}
		}
		else if (isUnknownInheritanceMode(gene) || isRecessive(gene))
		{
			if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = InheritanceResult.create(true, "Bl3");
				;
			}
			else if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				result = InheritanceResult.create(true, "Bl4");
				;
			}
			else if (Checks.isHomozygote(gavinRecord, pedigree.getParents().get(0)) || Checks.isHomozygote(gavinRecord,
					pedigree.getParents().get(1)))
			{
				result = InheritanceResult.create(true, "Bl5");
				;
			}
			else if (Checks.isCompound(gavinRecord, gavinRecordsForGene, pedigree))
			{
				result = InheritanceResult.create(true, "Bl6");
				;
			}
			else
			{
				result = IF.filter("Bl_IF_2");
			}
		}
		else if (isXLinked(gene))
		{
			if (pedigree.getChild().getGender() == Gender.MALE)
			{
				if (!Checks.subjectHasVariant(gavinRecord, PedigreeUtils.getFather(pedigree)))
				{
					result = InheritanceResult.create(true, "Bl7");
					;
				}
				else
				{
					result = IF.filter("Bl_IF_3");
				}
			}
			else if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = InheritanceResult.create(true, "Bl8");
				;
			}
			else
			{
				result = IF.filter("Bl_IF_4");
			}
		}
		else if (isDominantOrRecessive(gene))
		{
			if (Checks.isDeNovo(gavinRecord, pedigree))
			{
				result = InheritanceResult.create(true, "Bl9");
				;
			}
			else if (Checks.isNonPenetrant())
			{
				result = InheritanceResult.create(true, "Bl10");
				;
			}
			else if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				result = InheritanceResult.create(true, "Bl11");
				;
			}
			else if (Checks.isHomozygote(gavinRecord, pedigree.getParents().get(0)) || Checks.isHomozygote(gavinRecord,
					pedigree.getParents().get(1)))
			{
				result = InheritanceResult.create(true, "Bl12");
				;
			}
			else if (Checks.isCompound(gavinRecord, gavinRecordsForGene, pedigree))
			{
				result = InheritanceResult.create(true, "Bl13");
				;
			}
			else
			{
				result = IF.filter("Bl_IF_5");
			}
		}
		else
		{
			result = IF.filter("Bl_IF_6");
		}
		return result;
	}
}
