package org.molgenis.data.annotation.makervcf.structs;

import org.molgenis.cgd.CGDEntry;
import org.molgenis.data.annotation.core.entity.impl.gavin.Judgment;
import org.molgenis.data.annotation.makervcf.positionalstream.MatchVariantsToGenotypeAndInheritance;
import org.molgenis.data.annotation.makervcf.positionalstream.MatchVariantsToGenotypeAndInheritance.Status;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by joeri on 6/13/16.
 */
public class Relevance
{
	private Judgment judgment;
	private String allele;
	private String gene;
	private String FDR;
	private Map<String, Status> sampleStatus;
	private Map<String, String> sampleGenotypes;
	private Set<String> parentsWithReferenceCalls;
	private double alleleFreq;
	private double gonlAlleleFreq;
	private String transcript;

	CGDEntry cgdInfo;

	public Relevance(String allele, @Nullable String transcript, double alleleFreq, double gonlAlleleFreq, String gene,
			Judgment judgment)
	{
		this.allele = allele;
		this.transcript = transcript;
		this.alleleFreq = alleleFreq;
		this.gonlAlleleFreq = gonlAlleleFreq;
		this.gene = gene;
		this.judgment = judgment;
	}

	public String getFDR()
	{
		return FDR != null ? FDR : "";
	}

	public void setFDR(String FDR)
	{
		this.FDR = FDR;
	}

	public Set<String> getParentsWithReferenceCalls()
	{
		return parentsWithReferenceCalls;
	}

	public void setParentsWithReferenceCalls(Set<String> parentsWithReferenceCalls)
	{
		this.parentsWithReferenceCalls = parentsWithReferenceCalls;
	}

	public String getAllele()
	{
		return allele;
	}

	public String getGene()
	{
		return gene;
	}

	public double getAlleleFreq()
	{
		return alleleFreq;
	}

	public double getGonlAlleleFreq()
	{
		return gonlAlleleFreq;
	}

	public Optional<String> getTranscript()
	{
		return transcript != null ? Optional.of(transcript) : Optional.empty();
	}

	public Judgment getJudgment()
	{
		return judgment;
	}

	public String toStringShort()
	{
		return "Relevance{in gene " + gene + ", judgment:" + judgment + '}';
	}

	@Override
	public String toString()
	{
		return "Relevance{" + "judgment=" + judgment + ", allele='" + allele + '\'' + ", gene='" + gene + '\''
				+ ", FDR='" + FDR + '\'' + ", sampleStatus=" + sampleStatus + ", sampleGenotypes=" + sampleGenotypes
				+ ", parentsWithReferenceCalls=" + parentsWithReferenceCalls + ", alleleFreq=" + alleleFreq
				+ ", gonlAlleleFreq=" + gonlAlleleFreq + ", transcript='" + transcript + '\'' + ", cgdInfo=" + cgdInfo
				+ '}';
	}

	public CGDEntry getCgdInfo()
	{
		return cgdInfo;
	}

	public void setCgdInfo(CGDEntry cgdInfo)
	{
		this.cgdInfo = cgdInfo;
	}

	public Map<String, MatchVariantsToGenotypeAndInheritance.Status> getSampleStatus()
	{
		return sampleStatus != null ? sampleStatus : new HashMap<>();
	}

	public void setSampleStatus(Map<String, Status> sampleStatus)
	{
		this.sampleStatus = sampleStatus;
	}

	public void setSampleGenotypes(Map<String, String> sampleGenotypes)
	{
		this.sampleGenotypes = sampleGenotypes;
	}

	public Map<String, String> getSampleGenotypes()
	{
		return sampleGenotypes != null ? sampleGenotypes : new HashMap<>();
	}

}
