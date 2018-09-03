package org.molgenis.inheritance.model;

import com.google.auto.value.AutoValue;
import org.molgenis.cgd.CGDEntry;

@AutoValue
public abstract class Gene
{
	public abstract String getGeneName();

	public abstract CGDEntry getCgd();

	public static Gene create(String gene, CGDEntry cgdEntry)
	{
		return new AutoValue_Gene(gene, cgdEntry);
	}
}
