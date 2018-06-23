/**
 * Copyright 2016 William Van Woensel

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 * 
 * 
 * @author wvw
 * 
 */
package wvw.owl2rl.custom.select.domain;

import java.io.StringReader;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import wvw.owl2rl.custom.config.OntologyConfig;
import wvw.owl2rl.custom.select.RulesetSelectionException;
import wvw.owl2rl.custom.select.SelectConfig;
import wvw.owl2rl.custom.select.SelectOutput;
import wvw.owl2rl.custom.select.domain.rule.IRule;
import wvw.owl2rl.custom.select.domain.rule.IRuleset;
import wvw.owl2rl.custom.select.domain.rule.RuleParseException;
import wvw.owl2rl.custom.select.domain.rule.RulePrinter;

public abstract class DomainBasedSelection {

	protected Model model;

	public SelectOutput select(SelectConfig config) throws RulesetSelectionException {
		DomainBasedSelectConfig dbConfig = (DomainBasedSelectConfig) config;

		OntologyConfig ontology = dbConfig.getOntology();
		ontology.setContent(dbConfig.getAxioms() + "\n" + ontology.getContent());

		try {
			IRuleset ruleset = parse(dbConfig.getRules());

			List<IRule> ret = selectRuleset(ruleset, ontology, dbConfig);
			outputRemoved(ruleset, ret);

			return new SelectOutput(print(ret));

		} catch (RuleParseException e) {
			throw new RulesetSelectionException(e);
		}
	}

	private void outputRemoved(IRuleset orRules, List<IRule> resRules) {
		for (IRule rule : orRules.getRules())

			if (!resRules.contains(rule))
				System.out.println("removed: " + rule.getId());
	}

	public abstract List<IRule> selectRuleset(IRuleset rules, OntologyConfig ontology, DomainBasedSelectConfig config)
			throws RulesetSelectionException;

	protected abstract IRuleset parse(String rules) throws RuleParseException;

	protected String print(List<IRule> rules) {
		RulePrinter printer = new RulePrinter();

		return printer.print(rules);
	}

	protected void createModel(OntologyConfig ontology) {
		model = ModelFactory.createDefaultModel();

		model.read(new StringReader(ontology.getContent()), "", ontology.getSyntax());
	}
}
