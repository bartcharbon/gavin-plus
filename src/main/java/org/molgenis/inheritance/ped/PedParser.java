package org.molgenis.inheritance.ped;

import org.molgenis.inheritance.model.Gender;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * PED file:
 * TAB seperated
 * <p>
 * Pedigree Name
 * A unique alphanumeric identifier for this individual's family. Unrelated individuals should not share a pedigree name.
 * <p>
 * Individual ID
 * An alphanumeric identifier for this individual. Should be unique within his family (see above).
 * <p>
 * Father's ID
 * Identifier corresponding to father's individual ID or "0" if unknown father. Note
 * that if a father ID is specified, the father must also appear in the file.
 * <p>
 * Mother's ID
 * Identifier corresponding to mother's individual ID or "0" if unknown mother Note that if a mother ID is specified, the mother must also appear in the file.
 * <p>
 * Sex
 * Individual's gender (1=MALE, 2=FEMALE).
 * <p>
 * Affection status
 * Affection status to be used for association tests (0=UNKNOWN, 1=UNAFFECTED,2=AFFECTED).
 * <p>
 * Any additional columns are ignored
 **/
public class PedParser
{
	public List<Pedigree> parse(File pedFile) throws FileNotFoundException
	{
		Map<String, InternalPedigree> subjectMap = new HashMap<>();
		List<Pedigree> pedigrees = new ArrayList<>();
		try (Scanner pedScanner = new Scanner(pedFile))
		{
			parseLine(subjectMap, pedScanner);
		}
		for (Map.Entry<String, InternalPedigree> entry : subjectMap.entrySet())
		{
			pedigrees.add(createPedigree(subjectMap, entry));
		}
		return pedigrees;
	}

	private Pedigree createPedigree(Map<String, InternalPedigree> subjectMap, Map.Entry<String, InternalPedigree> entry)
	{
		Subject child = entry.getValue().getSubject();
		Subject father = null;
		Subject mother = null;
		if (subjectMap.containsKey(getKey(entry.getValue().getFamilyId(), entry.getValue().getFatherId())))
		{
			father = subjectMap.get(getKey(entry.getValue().getFamilyId(), entry.getValue().getFatherId()))
							   .getSubject();
		}
		if (subjectMap.containsKey(getKey(entry.getValue().getFamilyId(), entry.getValue().getFatherId())))
		{
			mother = subjectMap.get(getKey(entry.getValue().getFamilyId(), entry.getValue().getMotherId()))
							   .getSubject();
		}
		return Pedigree.create(child, father, mother);
	}

	private void parseLine(Map<String, InternalPedigree> subjectMap, Scanner pedScanner)
	{
		String line;
		while (pedScanner.hasNextLine())
		{
			line = pedScanner.nextLine();
			//Header?
			if (line.startsWith("#Family"))
			{
				line = pedScanner.nextLine();
			}
			String[] split = line.split("\t", -1);
			String identifier = getKey(split[0], split[1]);
			InternalPedigree internalPedigree = parseLine(split);
			subjectMap.put(identifier, internalPedigree);
		}
	}

	private String getKey(String family, String subjectId)
	{
		return family + "_" + subjectId;
	}

	private InternalPedigree parseLine(String[] splittedLine)
	{
		String familyId = splittedLine[0];
		String sampleId = splittedLine[1];
		String fatherId = splittedLine[2];
		String motherId = splittedLine[3];
		boolean affected = splittedLine[5].equals("2");
		Gender gender = mapGender(splittedLine[4]);
		return new InternalPedigree(Subject.create(sampleId, affected, gender), familyId, fatherId, motherId);
	}

	private Gender mapGender(String stringValue)
	{
		if (stringValue.equals("1"))
		{
			return Gender.MALE;
		}
		else if (stringValue.equals("2"))
		{
			return Gender.FEMALE;
		}
		throw new RuntimeException("Unknown gender value [" + stringValue + "]");
	}

	private class InternalPedigree
	{
		private Subject subject;
		private String familyId;
		private String fatherId;
		private String motherId;

		public InternalPedigree(Subject subject, String familyId, String father, String mother)
		{
			this.subject = subject;
			this.familyId = familyId;
			this.fatherId = father;
			this.motherId = mother;
		}

		public Subject getSubject()
		{
			return subject;
		}

		public String getFatherId()
		{
			return fatherId;
		}

		public String getMotherId()
		{
			return motherId;
		}

		public String getFamilyId()
		{
			return familyId;
		}
	}
}
