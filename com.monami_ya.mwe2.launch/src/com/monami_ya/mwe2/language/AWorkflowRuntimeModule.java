package com.monami_ya.mwe2.language;

import org.eclipse.emf.mwe2.language.Mwe2RuntimeModule;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;

import com.google.inject.Binder;
import com.monami_ya.mwe2.workflow.AWorkflowContext;

public class AWorkflowRuntimeModule extends Mwe2RuntimeModule {

	@Override
	public void configure(Binder binder) {
		binder.bind(IWorkflowContext.class).to(AWorkflowContext.class);
		super.configure(binder);
	}
}
