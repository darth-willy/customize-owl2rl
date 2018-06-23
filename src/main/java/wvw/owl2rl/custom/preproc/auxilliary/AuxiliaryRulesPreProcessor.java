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

package wvw.owl2rl.custom.preproc.auxilliary;

import java.io.IOException;

import wvw.owl2rl.custom.preproc.PreProcessConfig;
import wvw.owl2rl.custom.preproc.PreProcessException;
import wvw.owl2rl.custom.preproc.PreProcessResults;
import wvw.owl2rl.custom.preproc.PreProcessTypes;
import wvw.owl2rl.custom.preproc.PreProcessor;
import wvw.owl2rl.custom.res.ServiceResources;

public class AuxiliaryRulesPreProcessor extends PreProcessor {

	public AuxiliaryRulesPreProcessor(ServiceResources res) {
		super(res);
	}

	public PreProcessResults doPreprocess(PreProcessConfig config) throws PreProcessException {

		try {
			String auxRules = res.getContents(rootPath + "auxilliary/rules.sparql");
			String rules = config.getRules() + "\n\n" + auxRules;
			
			return new PreProcessResults(rules);

		} catch (IOException e) {
			throw new PreProcessException(e);
		}
	}

	public PreProcessTypes getType() {
		return PreProcessTypes.AUX_RULES;
	}
}
