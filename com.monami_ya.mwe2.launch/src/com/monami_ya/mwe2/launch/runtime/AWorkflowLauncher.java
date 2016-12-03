/*******************************************************************************
 * Copyright (c) 2008,2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *  Monami-ya LLC, Japan : Copied and modify to fit A-Workflow.
 *******************************************************************************/
package com.monami_ya.mwe2.launch.runtime;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.mwe2.launch.runtime.Mwe2Launcher;
import org.eclipse.emf.mwe2.launch.runtime.Mwe2Runner;

import com.google.inject.Injector;
import com.monami_ya.mwe2.language.AWorkflowStandaloneSetup;

public class AWorkflowLauncher extends Mwe2Launcher {
	private static final String PARAM = "p";

	@Override
	public void run(String[] args) {
		Options options = getOptions();
		final CommandLineParser parser = new PosixParser();
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
			if (line.getArgs().length == 0)
				throw new ParseException("No module name specified.");
			if (line.getArgs().length > 1)
				throw new ParseException("Only one module name expected. But " + line.getArgs().length
						+ " were passed (" + line.getArgList() + ")");

			String moduleName = line.getArgs()[0];
			Map<String, String> params = new HashMap<String, String>();
			String[] optionValues = line.getOptionValues(PARAM);
			if (optionValues != null) {
				for (String string : optionValues) {
					int index = string.indexOf('=');
					if (index == -1) {
						throw new ParseException(
								"Incorrect parameter syntax '" + string + "'. It should be 'name=value'");
					}
					String name = string.substring(0, index);
					String value = string.substring(index + 1);
					if (params.put(name, value) != null) {
						throw new ParseException("Duplicate parameter '" + name + "'.");
					}
				}
			}
			// check OperationCanceledException is accessible
			OperationCanceledException.class.getName();

			Injector injector = new AWorkflowStandaloneSetup().createInjectorAndDoEMFRegistration();
			Mwe2Runner mweRunner = injector.getInstance(Mwe2Runner.class);
			if (moduleName.contains("/")) {
				mweRunner.run(URI.createURI(moduleName), params);
			} else {
				mweRunner.run(moduleName, params);
			}
		} catch (NoClassDefFoundError e) {
			if ("org/eclipse/core/runtime/OperationCanceledException".equals(e.getMessage())) {
				System.err.println("Could not load class: org.eclipse.core.runtime.OperationCanceledException");
				System.err.println("Add org.eclipse.equinox.common to the class path.");
			} else {
				throw e;
			}
		} catch (final ParseException exp) {
			final HelpFormatter formatter = new HelpFormatter();
			System.err.println("Parsing arguments failed.  Reason: " + exp.getMessage());
			formatter.printHelp("java " + AWorkflowLauncher.class.getName() + " some.mwe2.Module [options]\n", options);
			return;
		}
	}

}
