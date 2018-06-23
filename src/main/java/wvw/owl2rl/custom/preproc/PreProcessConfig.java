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

package wvw.owl2rl.custom.preproc;

import wvw.owl2rl.custom.config.OntologyConfig;

public class PreProcessConfig {

	private String rules;
	private OntologyConfig ontology;

	public PreProcessConfig() {
	}

	public PreProcessConfig(String rules, OntologyConfig ontology) {
		this.rules = rules;

		this.ontology = ontology;
	}

	public boolean hasRules() {
		return rules != null;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public boolean hasOntology() {
		return ontology != null;
	}

	public OntologyConfig getOntology() {
		return ontology;
	}

	public void setOntologyConfig(OntologyConfig ontology) {
		this.ontology = ontology;
	}

	public boolean isComplete() {
		return rules != null && ontology != null;
	}
}