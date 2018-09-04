package org.molgenis.inheritance.tree;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.molgenis.inheritance.Checks.*;

//One parent, affected
public class Green
{
	private static final Logger LOG = LoggerFactory.getLogger(Green.class);

	public static InheritanceResult filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		LOG.debug("Entering 'Green' filtertree");
		InheritanceResult result;
		Subject parent = pedigree.getFather() != null ? pedigree.getFather() : pedigree.getMother();

		if (isDominant(gene))
		{
			result = InheritanceResult.create(true, "Gr1");
		}
		else if (isUnknownInheritanceMode(gene) || isRecessive(gene) || isDominantOrRecessive(gene))
		{
			if (Checks.isHomozygote(gavinRecord, pedigree.getChild()))
			{
				result = InheritanceResult.create(true, "Gr2");
				;
			}
			else if (isMultipleVariantsInOneGene(gavinRecordsForGene))
			{
				if (!Checks.subjectHasVariant(gavinRecord, parent))
				{
					result = InheritanceResult.create(true, "Gr3");
					;
				}
				else
				{
					result = IF.filter("Gr_IF_1");
				}
			}
			else
			{
				result = IF.filter("Gr_IF_2");
			}
		}
		else if (isXLinked(gene))
		{
			if (pedigree.getChild().getGender() != Gender.MALE)
			{
				result = InheritanceResult.create(true, "Gr4");
				;
			}
			else if (parent.getGender() == Gender.MALE && !parent.isAffected())
			{
				result = InheritanceResult.create(true, "Gr5");
				;
			}
			else if (!Checks.subjectHasVariant(gavinRecord, parent))
			{
				result = InheritanceResult.create(true, "Gr6");
				;
			}
			else
			{
				result = IF.filter("Gr_IF_3");
			}
		}
		else
		{
			result = IF.filter("Gr_IF_4");
		}
		return result;
	}
}
