package org.molgenis.inheritance;

import org.molgenis.cgd.CGDEntry;
import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.data.vcf.datastructures.Sample;
import org.molgenis.inheritance.exception.MultipleSamplesForIdException;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.molgenis.cgd.CGDEntry.GeneralizedInheritance.*;

public class Checks
{
	private Checks()
	{
	}

	public static boolean isDeNovo(GavinRecord record, Pedigree pedigree)
	{
		return !subjectHasVariant(record, pedigree.getFather()) || subjectHasVariant(record, pedigree.getMother());
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
			Optional<String> genotypeOptional = samples.get(0).getGenotype();
			if (genotypeOptional.isPresent())
			{
				String genotype = genotypeOptional.get();
				return genotype.equals("1|1") || genotype.equals("1/1");
			}
		}
		else
		{
			//This cannot happen in VCF format!
			throw new MultipleSamplesForIdException("Multiple samples found!");
		}
		return false;
	}

	public static boolean isCompound(List<GavinRecord> gavinRecords, Pedigree pedigree)
	{
		//no other variants found in this gene
		if (gavinRecords.isEmpty()) return false;

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
			Optional<String> genotypeOptional = samples.get(0).getGenotype();
			if (genotypeOptional.isPresent())
			{
				String genotype = genotypeOptional.get();
				return genotype.indexOf('1') != -1;
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
