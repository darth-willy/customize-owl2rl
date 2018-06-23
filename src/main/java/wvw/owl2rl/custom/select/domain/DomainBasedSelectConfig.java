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

import wvw.owl2rl.custom.config.OntologyConfig;
import wvw.owl2rl.custom.select.SelectConfig;

public class DomainBasedSelectConfig implements SelectConfig {

	private DomainSelectTypes type;

	private String rules;
	private String axioms;

	private OntologyConfig ontology;

	private String targetInf;

	public DomainBasedSelectConfig() {
	}

	public DomainBasedSelectConfig(DomainSelectTypes type, String rules, String axioms, OntologyConfig ontology,
			String targetInf) {

		this.type = type;

		this.rules = rules;
		this.axioms = axioms;

		this.ontology = ontology;

		this.targetInf = targetInf;
	}

	public DomainSelectTypes getType() {
		return type;
	}

	public void setType(DomainSelectTypes type) {
		this.type = type;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getAxioms() {
		return axioms;
	}

	public void setAxioms(String axioms) {
		this.axioms = axioms;
	}

	public OntologyConfig getOntology() {
		return ontology;
	}

	public void setOntology(OntologyConfig ontology) {
		this.ontology = ontology;
	}

	public String getTargetInf() {
		return targetInf;
	}

	public void setTargetInf(String targetInf) {
		this.targetInf = targetInf;
	}
	
	public boolean isComplete() {
		return rules != null &&  axioms != null && ontology != null;
	}
	
	public String getRequiredFields() {
		return "'rules', 'axioms', 'ontology'";
	}

	public String toString() {
		return "selection type: " + type;
	}
}
