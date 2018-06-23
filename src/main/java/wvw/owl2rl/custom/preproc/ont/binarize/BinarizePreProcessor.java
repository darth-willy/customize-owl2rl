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

package wvw.owl2rl.custom.preproc.ont.binarize;

import java.io.IOException;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import wvw.owl2rl.custom.config.OntologyConfig;
import wvw.owl2rl.custom.preproc.PreProcessConfig;
import wvw.owl2rl.custom.preproc.PreProcessException;
import wvw.owl2rl.custom.preproc.PreProcessResults;
import wvw.owl2rl.custom.preproc.PreProcessTypes;
import wvw.owl2rl.custom.preproc.ont.OntologyBasedPreProcessor;
import wvw.owl2rl.custom.res.ServiceResources;

public class BinarizePreProcessor extends OntologyBasedPreProcessor {

	private int newCtr = 0;
	private int lstCtr = 0;

	public BinarizePreProcessor(ServiceResources res) {
		super(res);
	}

	public PreProcessResults doPreprocess(PreProcessConfig config) throws PreProcessException {		
		OntologyConfig ontology = config.getOntology();
		
		try {
			String binOntology = binarizeOntology(ontology.getContent(), ontology.getSyntax());

			String binRules = res.getContents(rootPath + "binarize/rules.spin");
			String rules = config.getRules() + "\n\n" + binRules;
			
			return new PreProcessResults(rules, binOntology);

		} catch (IOException e) {
			throw new PreProcessException(e);
		}
	}

	public PreProcessTypes getType() {
		return PreProcessTypes.BINARIZE;
	}

	private String binarizeOntology(String ontology, String syntax) throws IOException {
		Model m = loadModel(ontology, syntax);

		binarize(intersectionOf, m);
		binarize(propertyChainAxiom, m);

		// NOTE has-key not supported by binarize option

		return getData(m);
	}

	private void binarize(Property p, Model m) {
		List<Statement> stmts = copyStmts(m.listStatements(null, p, (RDFNode) null));

		for (Statement stmt : stmts) {
			Resource list = stmt.getObject().asResource();

			List<Resource> chain = collectChain(list, m);
			binarize(p, chain, m);
		}
	}

	private void binarize(Property p, List<Resource> chain, Model m) {
		binarize(p, chain, 0, m);
	}

	private void binarize(Property p, List<Resource> chain, int idx, Model m) {
		if (chain.size() - 2 - idx == 0)
			return;

		Resource l1 = chain.get(idx);
		Resource l2 = chain.get(idx + 1);

		Resource nl = m.createResource("http://wvw.org/owl/binarize/L" + lstCtr++);
		Resource ne = m.createResource("http://wvw.org/owl/binarize/G" + newCtr++);

		l1.removeAll(rest);
		l1.addProperty(rest, nl);

		nl.addProperty(first, ne);
		nl.addProperty(rest, nil);

		ne.addProperty(p, l2);

		binarize(p, chain, idx + 1, m);
	}
}
