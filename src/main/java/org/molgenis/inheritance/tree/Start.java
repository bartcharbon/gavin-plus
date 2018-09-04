package org.molgenis.inheritance.tree;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.InheritanceResult;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Start
{
	private static final Logger LOG = LoggerFactory.getLogger(Start.class);

	public InheritanceResult filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		LOG.debug("Starting filtertree");
		InheritanceResult result;

		if (pedigree.getMother() != null && pedigree.getFather() == null)
		{
			result = filterOneParent(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant, pedigree.getMother());
		}
		else if (pedigree.getMother() == null && pedigree.getFather() != null)
		{
			result = filterOneParent(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant, pedigree.getFather());
		}
		else if (pedigree.getMother() != null && pedigree.getFather() != null)
		{
			result = filterTwoParents(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant);
		}
		//both parents null
		result = Black.filter(gavinRecord, gavinRecordsForGene, gene, pedigree);
		return result;
	}

	private InheritanceResult filterOneParent(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant, Subject parent)
	{
		InheritanceResult result;
		if (parent.isAffected())
		{
			result = Yellow.filter(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant);
		}
		else
		{
			result = Green.filter(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant);
		}
		return result;
	}

	private InheritanceResult filterTwoParents(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene,
			Gene gene, Pedigree pedigree, boolean penetrant)
	{
		InheritanceResult result;
		int affectedParents = 0;
		if (pedigree.getFather().isAffected())
		{
			affectedParents++;
		}
		if (pedigree.getMother().isAffected())
		{
			affectedParents++;
		}
		//Documentation states "1 parent affected", however the case of 2 affected parents is not descibed at all
		//Implemented as "at least one parent affected"
		if (affectedParents > 0)
		{
			result = Blue.filter(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant);
		}
		else
		{
			result = Red.filter(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant);
		}
		return result;
	}
}
