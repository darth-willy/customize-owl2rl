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
package wvw.owl2rl.custom.servlet;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import wvw.owl2rl.custom.config.Config;
import wvw.owl2rl.custom.preproc.PreProcessConfig;
import wvw.owl2rl.custom.preproc.PreProcessException;
import wvw.owl2rl.custom.preproc.PreProcessResults;
import wvw.owl2rl.custom.preproc.PreProcessor;
import wvw.owl2rl.custom.preproc.auxilliary.AuxiliaryRulesPreProcessor;
import wvw.owl2rl.custom.preproc.ont.binarize.BinarizePreProcessor;
import wvw.owl2rl.custom.preproc.ont.instantiate.InstantiateRulesPreProcessor;
import wvw.owl2rl.custom.res.ServiceResources;
import wvw.owl2rl.custom.select.RulesetSelectionException;
import wvw.owl2rl.custom.select.SelectOutput;
import wvw.owl2rl.custom.select.def.DefaultSelectConfig;
import wvw.owl2rl.custom.select.def.DefaultSelectConfig.InitSelectConfig;
import wvw.owl2rl.custom.select.def.DefaultSelection;
import wvw.owl2rl.custom.select.domain.DomainBasedSelectConfig;
import wvw.owl2rl.custom.select.domain.DomainBasedSelection;
import wvw.owl2rl.custom.select.domain.DomainBasedSelectionFactory;
import wvw.owl2rl.custom.select.domain.DomainSelectTypes;
import wvw.owl2rl.custom.servlet.msg.ErrorMessage;
import wvw.owl2rl.custom.servlet.msg.PreProcessResultMsg;
import wvw.owl2rl.custom.servlet.msg.ResponseTypes;
import wvw.owl2rl.custom.servlet.msg.SelectionOutputMessage;
import wvw.owl2rl.custom.servlet.msg.StringResultMessage;
import wvw.owl2rl.custom.servlet.msg.serial.EnumDeserializer;
import wvw.owl2rl.custom.servlet.msg.serial.EnumSerializer;
import wvw.utils.OutputUtils;
import wvw.utils.StreamUtils;

public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Gson gson;
	private GsonBuilder gsonBuilder;

	private Logger log;

	private ServiceResources res;

	public MainServlet() {
		super();
	}

	public void init() throws ServletException {
		try {
			initResources();

			initLogger();
			initGson();

			Config.init(res);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initResources() {
		this.res = new ServiceResources(getServletContext());
	}

	private void initLogger() throws IOException {
		log = Logger.getLogger(MainServlet.class.getName());
		Properties properties = new Properties();
		try {
			properties.load(res.getInputStream(("log4j.properties")));
			PropertyConfigurator.configure(properties);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initGson() {
		gsonBuilder = new GsonBuilder();

		regEnums(ResponseTypes.class, DomainSelectTypes.class);
	}

	@SuppressWarnings("rawtypes")
	private void regEnums(Class... clazzes) {
		// (registering Enum.class doesn't work)
		EnumSerializer enumSerial = new EnumSerializer();
		EnumDeserializer enumDeserial = new EnumDeserializer();

		for (Class clazz : clazzes) {
			gsonBuilder.registerTypeAdapter(clazz, enumSerial);

			gsonBuilder.registerTypeAdapter(clazz, enumDeserial);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().write("<html>" + "<body>" + "<h1>Welcome to the Customizing OWL2 RL Web Service!</h1>"
					+ "</body>" + "</html>");

		} catch (IOException e) {
			log.error("Error handling incoming GET: " + OutputUtils.toString(e));

			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("> Received request");

		gson = gsonBuilder.create();

		String url = request.getRequestURL().toString();
		// log.info("url: " + url);

		if (url.matches(".*?select/.*?"))
			doSelectRules(url, request, response);

		else if (url.matches(".*?preprocess/.*?"))
			doPreProcess(url, request, response);

		else {
			String errorMsg = "Expected 'select/[default/domain]', "
					+ "'preprocess/(aux_rules|binarize|inst_rules)', in URL";
			log.error(errorMsg);

			response.getWriter().write(gson.toJson(new ErrorMessage(errorMsg)));
		}
	}

	private void doSelectRules(String url, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (url.endsWith("default"))
			doDefaultSelectRules(request, response);

		else if (url.endsWith("domain"))
			doDomainSelectRules(request, response);
	}

	private void doDefaultSelectRules(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(">> Selecting rules (default)");

		String errorMsg = null;

		try {
			String json = readString(request);
			DefaultSelectConfig config = new DefaultSelectConfig(gson.fromJson(json, InitSelectConfig.class));

			if (!config.isComplete()) {
				errorMsg = "Error selecting rules: incomplete config (required: " + config.getRequiredFields() + ")";

			} else {
				// System.out.println("config: " + config);

				DefaultSelection select = new DefaultSelection();
				SelectOutput output = select.select(config);

				response.getWriter().write(gson.toJson(new SelectionOutputMessage(output)));

				log.info(">> Selection successful, returned result");
			}

		} catch (IOException e) {
			errorMsg = "Error selecting rules: " + OutputUtils.toString(e);

			e.printStackTrace();
		}

		if (errorMsg != null) {
			response.getWriter().write(gson.toJson(new ErrorMessage(errorMsg)));

			log.error(errorMsg);
		}
	}

	private void doDomainSelectRules(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(">> Selecting rules (domain-specific)");

		String errorMsg = null;

		try {
			String json = readString(request);

			DomainBasedSelectConfig config = gson.fromJson(json, DomainBasedSelectConfig.class);
			if (!config.isComplete()) {
				errorMsg = "Error selecting rules: incomplete config (required: " + config.getRequiredFields() + ")";

			} else if (!config.getOntology().isComplete()) {
				errorMsg = "Error selecting rules: incomplete ontology (required: "
						+ config.getOntology().getRequiredFields() + ")";

			} else {
				DomainBasedSelection select = DomainBasedSelectionFactory.create(config);

				String rules = select.select(config).getRules();
				response.getWriter().write(gson.toJson(new StringResultMessage(rules)));

				log.info(">> Selection successful, returned result");
			}

		} catch (IOException | RulesetSelectionException e) {
			errorMsg = "Error selecting rules: " + OutputUtils.toString(e);

			e.printStackTrace();
		}

		if (errorMsg != null) {
			response.getWriter().write(gson.toJson(new ErrorMessage(errorMsg)));

			log.error(errorMsg);
		}
	}

	private void doPreProcess(String url, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String option = url.substring(url.lastIndexOf("/")).replace("/", "");

		log.info(">> Pre-processing (" + option + ")");

		String errorMsg = null;
		PreProcessor processor = null;

		if (option.equals("auxilliary"))
			processor = new AuxiliaryRulesPreProcessor(res);

		else if (option.equals("binarize"))
			processor = new BinarizePreProcessor(res);

		else if (option.equals("instantiate"))
			processor = new InstantiateRulesPreProcessor(res);

		else
			errorMsg = "Unknown preprocessing option: " + option;

		if (processor != null) {
			PreProcessConfig config = gson.fromJson(readString(request), PreProcessConfig.class);

			if (!config.hasRules()) {
				errorMsg = "Error pre-processing rules: no rules specified";

			} else if (processor.getType().requiresOntology()) {

				if (!config.hasOntology())
					errorMsg = "Error pre-processing rules: no ontology specified";

				else if (!config.getOntology().isComplete())
					errorMsg = "Error pre-processing rules: incomplete ontology (required: "
							+ config.getOntology().getRequiredFields() + ")";
			}

			if (errorMsg == null) {
				try {
					PreProcessResults results = processor.preprocess(config);
					response.getWriter().write(gson.toJson(new PreProcessResultMsg(results)));

					log.info(">> Pre-processing successful, returned result");

				} catch (PreProcessException e) {
					errorMsg = "Error pre-processing rules: " + OutputUtils.toString(e);

					e.printStackTrace();
				}
			}
		}

		if (errorMsg != null) {
			response.getWriter().write(gson.toJson(new ErrorMessage(errorMsg)));

			log.error(errorMsg);
		}
	}

	private Reader getReader(HttpServletRequest request) throws IOException {
		String url = request.getParameter("url");
		String path = request.getParameter("path");

		if (url != null)
			return new InputStreamReader(new URL(url).openStream());

		else if (path != null)
			return new FileReader(path);

		else
			return request.getReader();
	}

	private String readString(HttpServletRequest request) throws IOException {
		return StreamUtils.readString(getReader(request));
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}
