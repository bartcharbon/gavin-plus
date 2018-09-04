package org.molgenis.inheritance.tree;

import org.molgenis.inheritance.model.InheritanceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: should this be part of the inheritance?
public class IF
{
	private static final Logger LOG = LoggerFactory.getLogger(IF.class);

	public static InheritanceResult filter(String code)
	{
		LOG.debug("Entering 'IF' filtertree");
		return InheritanceResult.create(false, code);
	}
}
