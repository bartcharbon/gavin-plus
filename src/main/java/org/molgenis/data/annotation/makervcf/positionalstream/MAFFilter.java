package org.molgenis.data.annotation.makervcf.positionalstream;

import org.molgenis.data.annotation.makervcf.structs.GavinRecord;
import org.molgenis.data.annotation.makervcf.structs.Relevance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by joeri on 6/29/16.
 */
public class MAFFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(MAFFilter.class);
	private Iterator<GavinRecord> relevantVariants;
	private final boolean keepAllVariants;
	double threshold = 0.05;

	public MAFFilter(Iterator<GavinRecord> relevantVariants, boolean keepAllVariants)
	{
		this.relevantVariants = relevantVariants;
		this.keepAllVariants = keepAllVariants;
	}

	public Iterator<GavinRecord> go()
	{
		return new Iterator<GavinRecord>()
		{

			GavinRecord nextResult;

			@Override
			public boolean hasNext()
			{
				while (relevantVariants.hasNext())
				{
					GavinRecord gavinRecord = relevantVariants.next();
					if (gavinRecord.isRelevant())
					{
						for (Relevance rlv : gavinRecord.getRelevance())
						{
							//use GoNL/ExAC MAF to control for false positives (or non-relevant stuff) in ClinVar
							if (rlv.getGonlAlleleFreq() < threshold && rlv.getAlleleFreq() < threshold)
							{
								nextResult = gavinRecord;
								return true;
							}
							else
							{
								LOG.debug(
										"[MAFFilter] Removing relevance for variant at {}:{} because it has AF >{}. ExAC: {}, GoNL: {}",
										gavinRecord.getChromosome(), gavinRecord.getPosition(), threshold,
										rlv.getAlleleFreq(), rlv.getGonlAlleleFreq());
								if (keepAllVariants)
								{
									gavinRecord.setRelevances(Collections.emptyList());
									return true;
								}
							}
						}
					}
					else
					{
						if (keepAllVariants)
						{
							nextResult = gavinRecord;
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public GavinRecord next()
			{
				return nextResult;
			}
		};
	}
}
