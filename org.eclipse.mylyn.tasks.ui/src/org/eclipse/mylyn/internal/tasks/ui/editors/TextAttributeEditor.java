/*******************************************************************************
 * Copyright (c) 2004, 2009 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.tasks.ui.editors;

import org.eclipse.mylyn.internal.provisional.commons.ui.CommonFormUtil;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.editors.AbstractAttributeEditor;
import org.eclipse.mylyn.tasks.ui.editors.LayoutHint;
import org.eclipse.mylyn.tasks.ui.editors.LayoutHint.ColumnSpan;
import org.eclipse.mylyn.tasks.ui.editors.LayoutHint.RowSpan;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Steffen Pingel
 */
public class TextAttributeEditor extends AbstractAttributeEditor {

	private Text text;

	private boolean cflowModifyText;

	public TextAttributeEditor(TaskDataModel manager, TaskAttribute taskAttribute) {
		super(manager, taskAttribute);
		setLayoutHint(new LayoutHint(RowSpan.SINGLE, ColumnSpan.SINGLE));
	}

	protected Text getText() {
		return text;
	}

	@Override
	public void createControl(Composite parent, FormToolkit toolkit) {
		if (isReadOnly()) {
			text = new Text(parent, SWT.FLAT | SWT.READ_ONLY);
			text.setFont(EditorUtil.TEXT_FONT);
			text.setData(FormToolkit.KEY_DRAW_BORDER, Boolean.FALSE);
			text.setToolTipText(getDescription());
			text.setText(getValue());
		} else {
			text = toolkit.createText(parent, getValue(), SWT.FLAT);
			text.setFont(EditorUtil.TEXT_FONT);
			text.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
			text.setToolTipText(getDescription());
			text.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					cflowModifyText = true;// prevent infinite recursion and also prevent resetting the caret location
					try {
						setValue(text.getText());
					} finally {
						cflowModifyText = false;
					}
					CommonFormUtil.ensureVisible(text);
				}
			});
		}
		toolkit.adapt(text, false, false);
		setControl(text);
	}

	public String getValue() {
		return getAttributeMapper().getValue(getTaskAttribute());
	}

	public void setValue(String text) {
		getAttributeMapper().setValue(getTaskAttribute(), text);
		attributeChanged();
	}

	@Override
	public void refresh() {
		if (!cflowModifyText && text != null && !text.isDisposed()) {
			text.setText(getValue());
		}
	}

	@Override
	public boolean shouldAutoRefresh() {
		return true;
	}
}
