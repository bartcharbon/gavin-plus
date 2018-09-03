package org.molgenis.inheritance.tree;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.inheritance.model.Gene;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;

import java.util.List;

public class Start
{
	public boolean filter(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene, Pedigree pedigree,
			boolean penetrant)
	{
		int nrOfParents = pedigree.getParents().size();
		boolean result;
		switch (nrOfParents)
		{
			case 0:
				result = Black.filter(gavinRecord, gavinRecordsForGene, gene, pedigree);
				break;
			case 1:
				result = filterOneParent(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant);
				break;
			case 2:
				result = filterTwoParents(gavinRecord, gavinRecordsForGene, gene, pedigree, penetrant);
				break;
			default:
				throw new RuntimeException("Maximum number of parents is 2, got:" + nrOfParents);
		}
		return result;
	}

	private boolean filterOneParent(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		Subject parent = pedigree.getParents().get(0);
		boolean result;
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

	private boolean filterTwoParents(GavinRecord gavinRecord, List<GavinRecord> gavinRecordsForGene, Gene gene,
			Pedigree pedigree, boolean penetrant)
	{
		boolean result;
		int affectedParents = 0;
		for (Subject parent : pedigree.getParents())
		{
			if (parent.isAffected())
			{
				affectedParents++;
			}
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
