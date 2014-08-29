/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geogig.geoserver.web.repository;

import java.io.File;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.geogig.geoserver.config.RepositoryInfo;

public class RepositoryEditPanel extends FormComponentPanel<RepositoryInfo> {

    private static final long serialVersionUID = -870873448379832051L;

    private GeoGigDirectoryFormComponent parent;

    private TextField<String> name;

    public RepositoryEditPanel(final String wicketId, IModel<RepositoryInfo> model,
            final boolean isNew) {
        super(wicketId, model);

        IModel<String> parentModel = new PropertyModel<String>(model, "parentDirectory");
        parent = new GeoGigDirectoryFormComponent("parentDirectory", parentModel);
        parent.setEnabled(isNew);
        add(parent);

        IModel<String> nameModel = new PropertyModel<String>(model, "name");
        name = new TextField<String>("repositoryName", nameModel);
        name.setEnabled(isNew);
        name.setRequired(true);
        name.setLabel(new ResourceModel("name", "Repo name"));
        add(name);

        add(new IValidator<RepositoryInfo>() {
            @Override
            public void validate(IValidatable<RepositoryInfo> validatable) {
                parent.processInput();
                name.processInput();
                RepositoryInfo repo = validatable.getValue();
                String uri = repo.getLocation();
                File repoDir = new File(uri);
                ValidationError error = new ValidationError();
                if (repoDir.exists()) {
                    error.addMessageKey("errDirectoryExists");
                }
                if (!repoDir.getParentFile().canWrite()) {
                    error.addMessageKey("errParentReadOnly");
                }
                if (!error.getKeys().isEmpty()) {
                    validatable.error(error);
                }
            }
        });
    }

    @Override
    protected void convertInput() {
        RepositoryInfo modelObject = getModelObject();
        setConvertedInput(modelObject);
    }
}