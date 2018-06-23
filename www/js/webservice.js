/**
 * Copyright 2016 William Van Woensel
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * 
 * @author wvw
 * 
 */

// - default selection

// load required data
var rules = read("res/owl/owl2rl/full/rules.sparql");
var axioms = read("res/owl/owl2rl/full/axioms.nt");

// choose any combination of selections
var selections = [ 'entailed', 'ineff', 'inf-schema', 'inst-ent'];

// get rule subset (and updated axioms) from web service
defaultSelect(selections, {
	rules : rules, 
	axioms : axioms, 

}, function(ret) {
		showRules(ret.rules);
		
		showOntology(ret.axioms);
	}
);


// - domain-based selection

//// load required data
//var rules = read("res/owl/owl2rl/full/rules.sparql");
//var axioms = read("res/owl/owl2rl/full/axioms.nt");
//
//var ontology = read("res/owl/ontology/example2.nt");
//
//// get rule subset from web service
//// (currently, only available algorithm is "forward_naive")
//
//domainBasedSelect("forward_naive", {
//	rules : rules, 
//	axioms : axioms, 
//	
//	ontology : {
//		content : ontology,
//		
//		syntax : "N-TRIPLE",
//	}
//
//}, function(rules) {
//	showRules(rules);
//});


// - pre-processing (n-ary rules)

//// options: auxilliary, binarize, instantiate
//// (binarize, instantiate: require an input ontology)
//
//// load rules
//var rules = read("res/owl/owl2rl/full/rules.sparql");
//
//// load ontology (if needed)
//var ontology = read("res/owl/ontology/example.nt");
//
////get pre-processed rules from web service
//
//preprocess("instantiate", {	
//	rules : rules,
//	
//	ontology : {
//		content : ontology,
//		
//		syntax : "N-TRIPLE"
//	}
//	
//}, function(ret) {	
//	showRules(ret.rules);
//
//	// (in case of binarize, returns binarized ontology as well)
//	if (ret.ontology)
//		showOntology(ret.ontology);
//});