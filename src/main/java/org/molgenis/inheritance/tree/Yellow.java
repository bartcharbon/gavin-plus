package org.molgenis.inheritance.tree;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.Checks;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.InheritanceResult;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.molgenis.inheritance.Checks.*;

//One parent, unaffected
class Yellow
{
	private static final Logger LOG = LoggerFactory.getLogger(Yellow.class);

	private Yellow()
	{
	}
	public static InheritanceResult filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree)
	{
		Subject parent = pedigree.getFather() != null ? pedigree.getFather() : pedigree.getMother();
		LOG.debug("Entering 'Yellow' filtertree");
		if (isDominant(gene))
		{
			if (isNonPenetrant())
			{
				return InheritanceResult.create(true, "Yl_1");
			}
			else if (isDeNovo(gavinRecord, pedigree))
			{
				return InheritanceResult.create(true, "Yl_2");
			}
			else
			{
				return IF.filter("YL_IF_1");
			}
		}
		else if (isUnknownInheritanceMode(gene) || isRecessive(gene))
		{
			if (isHomozygote(gavinRecord, pedigree.getChild()))
			{
				return InheritanceResult.create(true, "Yl_3");
			}
			else if (isMultipleVariantsInOneGene(gavinRecordsForGene))
			{
				for (GavinRecord record : gavinRecordsForGene)
				{
					if (!record.equals(gavinRecord))
					{
						if (Checks.subjectHasVariant(gavinRecord, parent) && Checks.subjectHasVariant(
								gavinRecordsForGene.get(0), parent))
						{
							//FIXME how to get the other variant correctly and what if there are three or more
							return InheritanceResult.create(true, "Yl_4");
						}
						else
						{
							return IF.filter("YL_IF_2");
						}
					}
				}
				throw new RuntimeException("No other record than the one under test found!");
			}
			else
			{
				return IF.filter("YL_IF_3");
			}
		}
		else if (isXLinked(gene))
		{
			if (pedigree.getFather() != null)
			{
				if (pedigree.getFather().isAffected())
				{
					return InheritanceResult.create(true, "Yl_5");
				}
				else if (subjectHasVariant(gavinRecord, pedigree.getFather()))
				{
					return InheritanceResult.create(true, "Yl_6");
				}
				else
				{
					return IF.filter("YL_IF_4");
				}
			}
			else
			{
				return IF.filter("YL_IF_5");
			}
		}
		else if (isDominantOrRecessive(gene))
		{
			if (isNonPenetrant())
			{
				return InheritanceResult.create(true, "Yl_6");
			}
			else if (isDeNovo(gavinRecord, pedigree))
			{
				return InheritanceResult.create(true, "Yl_7");
			}
			else if (isHomozygote(gavinRecord, pedigree.getChild()))
			{
				return InheritanceResult.create(true, "Yl8");
			}
			else if (isMultipleVariantsInOneGene(gavinRecordsForGene))
			{
				//FIXME: is current variant part of this list
				if (Checks.subjectHasVariant(gavinRecord, parent) && Checks.subjectHasVariant(
						gavinRecordsForGene.get(0), parent))
				{
					//FIXME how to get the other variant correctly and what if there are three or more
					return InheritanceResult.create(true, "Yl9");
				}
				else
				{
					return IF.filter("YL_IF_6");
				}
			}
			else
			{
				return IF.filter("YL_IF_7");
			}
		}
		else
		{
			return IF.filter("YL_IF_8");
		}
	}
}
