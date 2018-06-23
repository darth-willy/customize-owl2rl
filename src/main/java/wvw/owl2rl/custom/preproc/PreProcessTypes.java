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

public enum PreProcessTypes {

	AUX_RULES(false), BINARIZE(true, true), INST_RULES(true, false);

	private boolean requiresOntology = false;
	private boolean outputsOntology = false;

	private PreProcessTypes(boolean requiresOntology) {
		this.requiresOntology = requiresOntology;
	}

	private PreProcessTypes(boolean requiresOntology, boolean outputsOntology) {
		this.requiresOntology = requiresOntology;

		this.outputsOntology = outputsOntology;
	}

	public boolean requiresOntology() {
		return requiresOntology;
	}

	public boolean outputsOntology() {
		return outputsOntology;
	}

	public String toString() {
		return super.toString().toLowerCase();
	}
}
