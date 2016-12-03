package com.monami_ya.mwe2.language;

import org.eclipse.emf.mwe2.language.Mwe2StandaloneSetup;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class AWorkflowStandaloneSetup extends Mwe2StandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(new AWorkflowRuntimeModule());
	}

}
