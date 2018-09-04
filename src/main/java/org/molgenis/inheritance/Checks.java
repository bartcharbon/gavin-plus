package org.molgenis.inheritance;

import org.molgenis.cgd.CGDEntry;
import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.data.vcf.datastructures.Sample;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.molgenis.cgd.CGDEntry.GeneralizedInheritance.*;

public class Checks
{
	private static final Logger LOG = LoggerFactory.getLogger(Checks.class);
	public static boolean isDeNovo(GavinRecord record, Pedigree pedigree)
	{
		if (subjectHasVariant(record, pedigree.getFather()) || subjectHasVariant(record, pedigree.getMother()))
		{
			return false;
		}
		return true;
	}

	public static boolean isNonPenetrant()
	{
		//TODO Unclear how to implement in the current Inheritance matcher tool this is a user defined setting.
		return false;
	}

	public static boolean isOccuringInAffectedParent(GavinRecord gavinRecord, Pedigree pedigree)
	{
		Subject subject = PedigreeUtils.getAffectedParent(pedigree);
		return subjectHasVariant(gavinRecord, subject);
	}

	public static boolean isHomozygote(GavinRecord record, Subject subject)
	{
		List<Sample> samples = record.getSamples()
									 .filter(sample -> sample.getId().equals(subject.getSampleId()))
									 .collect(Collectors.toList());
		if (samples.size() == 1)
		{
			if (samples.get(0).getGenotype().isPresent())
			{
				String genotype = samples.get(0).getGenotype().get();
				if (genotype.equals("1|1") || genotype.equals("1/1"))
				{//FIXME:other variants of homozygote?
					return true;
				}
			}
		}
		else
		{
			//This cannot happen in VCF format!
			new RuntimeException("Multiple samples found!");
		}
		return false;
	}

	public static boolean isCompound(GavinRecord gavinRecord, List<GavinRecord> gavinRecords, Pedigree pedigree)
	{
		//no other variants found in this gene
		if (gavinRecords.size() == 0) return false;

		boolean fatherHasVariant = false;
		boolean motherHasVariant = false;

		// is 1 van de andere varianten DeNovo?
		boolean otherVariantDeNovo = false;
		for (GavinRecord record : gavinRecords)
		{
			if (Checks.isDeNovo(record, pedigree))
			{
				otherVariantDeNovo = true;
			}
		}

		for (GavinRecord record : gavinRecords)
		{
			Subject father = pedigree.getFather();
			Subject mother = pedigree.getMother();
			if (father != null && subjectHasVariant(record, father)) fatherHasVariant = true;
			if (mother != null && subjectHasVariant(record, mother)) motherHasVariant = true;
		}

		if (fatherHasVariant && motherHasVariant) return true;
		else if (otherVariantDeNovo) return fatherHasVariant || motherHasVariant;
		else return false;
	}

	public static boolean subjectHasVariant(GavinRecord record, Subject subject)
	{
		List<Sample> samples = record.getSamples()
									 .filter(sample -> sample.getId().equals(subject.getSampleId()))
									 .collect(Collectors.toList());
		if (samples.size() == 1)
		{
			if (samples.get(0).getGenotype().isPresent())
			{
				String genotype = samples.get(0).getGenotype().get();
				if (genotype.indexOf("1") != -1)
				{
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isDominantOrRecessive(Gene gene)
	{
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		return inheritance == DOMINANT_OR_RECESSIVE;
	}

	public static boolean isRecessive(Gene gene)
	{
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		return inheritance == RECESSIVE;
	}

	public static boolean isUnknownInheritanceMode(Gene gene)
	{
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		return inheritance == NOTINCGD;
	}

	public static boolean isDominant(Gene gene)
	{
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		return inheritance == DOMINANT;
	}

	public static boolean isXLinked(Gene gene)
	{
		CGDEntry.GeneralizedInheritance inheritance = gene.getCgd().getGeneralizedInheritance();
		return inheritance == X_LINKED;
	}

	public static boolean isMultipleVariantsInOneGene(List<GavinRecord> records)
	{
		return records.size() > 1;
	}
}
