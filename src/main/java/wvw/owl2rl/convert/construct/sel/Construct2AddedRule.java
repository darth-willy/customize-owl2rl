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

package wvw.owl2rl.convert.construct.sel;

import java.util.ArrayList;
import java.util.List;

import org.topbraid.spin.model.Construct;

import wvw.owl2rl.convert.ConvertConfig;
import wvw.owl2rl.convert.ConvertException;
import wvw.owl2rl.convert.construct.Construct2;
import wvw.owl2rl.custom.select.def.AddedRule;
import wvw.utils.rule.RuleWrapper;
import wvw.utils.rule.RulesUtils;

public class Construct2AddedRule extends Construct2 {

	public Construct2AddedRule() {
		super("Construct2AddedRule");
	}

	public Object convertRules(String rulesStr, ConvertConfig config)
			throws ConvertException {

		List<RuleWrapper> rules = RulesUtils.splitRules(rulesStr);

		List<Object> ret = new ArrayList<Object>();
		for (RuleWrapper rule : rules) {
			String id = rule.getComment().substring(1).trim();

			ret.add(new AddedRule(id, rule.getRule()));
		}

		return ret;
	}

	public List<Object> convertRules(List<Object> rules)
			throws ConvertException {
		return null;
	}

	public List<Object> convert(Construct query) throws ConvertException {
		return null;
	}
}
