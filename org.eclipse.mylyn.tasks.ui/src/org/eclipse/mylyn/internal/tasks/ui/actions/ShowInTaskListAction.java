/*******************************************************************************
 * Copyright (c) 2004, 2009 Willian Mitsuda and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Willian Mitsuda - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.tasks.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.internal.tasks.ui.views.TaskListView;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.ui.actions.BaseSelectionListenerAction;

/**
 * @author Willian Mitsuda
 */
public class ShowInTaskListAction extends BaseSelectionListenerAction {

	public ShowInTaskListAction() {
		super(Messages.ShowInTaskListAction_Show_In_Task_List);
	}

	@Override
	public void run() {
		IStructuredSelection selection = getStructuredSelection();
		if (!selection.isEmpty()) {
			Object element = selection.getFirstElement();
			if (element instanceof ITask) {
				TasksUiUtil.openTasksViewInActivePerspective();
				TaskListView.getFromActivePerspective().selectedAndFocusTask((ITask) element);
			}
		}
	}

	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		return selection.size() == 1 && selection.getFirstElement() instanceof ITask;
	}

}
