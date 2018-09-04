package org.molgenis.inheritance.ped;

import org.molgenis.inheritance.model.Gender;
import org.molgenis.inheritance.model.Pedigree;
import org.molgenis.inheritance.model.Subject;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class PedParserTest
{

	@Test
	public void testParse() throws IOException
	{

		File inputPedFile = new File(
				"src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.ped");
		PedParser pedParser = new PedParser();
		List<Pedigree> actual = pedParser.parse(inputPedFile);

		List<Pedigree> expected = new ArrayList<>();
		Subject janFictief = Subject.create("jan", true, Gender.MALE);
		Subject pietFictief = Subject.create("piet", false, Gender.MALE);
		Subject gerdaFictief = Subject.create("gerda", true, Gender.FEMALE);
		Subject gerdaGeit = Subject.create("gerda", true, Gender.FEMALE);
		Subject jannieGeit = Subject.create("jannie", false, Gender.FEMALE);
		Subject janGeit = Subject.create("jan", true, Gender.MALE);
		Subject karelGeit = Subject.create("karel", false, Gender.MALE);

		expected.add(Pedigree.create(janFictief, pietFictief, gerdaFictief));
		expected.add(Pedigree.create(pietFictief, null, null));
		expected.add(Pedigree.create(gerdaFictief, null, null));
		expected.add(Pedigree.create(gerdaGeit, janGeit, jannieGeit));
		expected.add(Pedigree.create(karelGeit, janGeit, jannieGeit));
		expected.add(Pedigree.create(janGeit, null, null));
		expected.add(Pedigree.create(jannieGeit, null, null));

		for (Pedigree expectedValue : expected)
		{
			assertTrue(actual.contains(expectedValue));
		}
	}
}