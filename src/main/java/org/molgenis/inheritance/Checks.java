package org.molgenis.inheritance;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.data.vcf.datastructures.Sample;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;

import java.util.List;
import java.util.stream.Collectors;

import static org.molgenis.inheritance.PedigreeUtils.getFather;

public class Checks
{
	public static boolean isDeNovo(GavinRecord record, Pedigree pedigree)
	{
		for (Subject subject : pedigree.getParents())
		{
			if (subjectHasVariant(record, subject))
			{
				return false;
			}
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
			Subject father = getFather(pedigree);
			Subject mother = getFather(pedigree);
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
}
