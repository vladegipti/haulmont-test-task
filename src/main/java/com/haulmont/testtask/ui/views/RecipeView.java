package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.model.Recipe;
import com.haulmont.testtask.ui.windows.RecipeWindow;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;

public class RecipeView extends VerticalLayout implements View {
    Grid<Recipe> recipeGrid;
    TextField patientTf, descriptionTf, priorityTf;

    public RecipeView() {
        Panel filterPanel = new Panel("Фильтр");

        patientTf = new TextField("Пациент");
        patientTf.addStyleName(ValoTheme.TEXTFIELD_TINY);
        patientTf.addValueChangeListener(this::onFilterChange);

        descriptionTf = new TextField("Описание");
        descriptionTf.addStyleName(ValoTheme.TEXTFIELD_TINY);
        descriptionTf.addValueChangeListener(this::onFilterChange);

        priorityTf = new TextField("Приоритет");
        priorityTf.addStyleName(ValoTheme.TEXTFIELD_TINY);
        priorityTf.addValueChangeListener(this::onFilterChange);

        Button resetFiltersButton = new Button("Сбросить все");
        resetFiltersButton.addStyleName(ValoTheme.BUTTON_TINY);
        resetFiltersButton.addClickListener(clickEvent -> {
            patientTf.setValue("");
            descriptionTf.setValue("");
            priorityTf.setValue("");
        });

        HorizontalLayout filterLayout = new HorizontalLayout(patientTf, descriptionTf, priorityTf, resetFiltersButton);
        filterLayout.setComponentAlignment(resetFiltersButton, Alignment.BOTTOM_LEFT);
        filterLayout.setMargin(true);
        filterPanel.setContent(filterLayout);

        recipeGrid = new Grid<>();
        recipeGrid.addColumn(Recipe::getDescription).setCaption("Описание");
        recipeGrid.addColumn(recipe -> recipe.getPatient().getFirstName().charAt(0) + ". " + recipe.getPatient().getPatronymic().charAt(0) + ". " + recipe.getPatient().getLastName()).setCaption("Пациент");
        recipeGrid.addColumn(recipe -> recipe.getDoctor().getFirstName().charAt(0) + ". " + recipe.getDoctor().getPatronymic().charAt(0) + ". " + recipe.getDoctor().getLastName()).setCaption("Доктор");
        recipeGrid.addColumn(Recipe::getDateOfIssue, new DateRenderer("%1$td.%1$tm.%1$tY")).setCaption("Дата выдачи");
        recipeGrid.addColumn(Recipe::getValidFor).setCaption("Срок действия");
        recipeGrid.addColumn(Recipe::getPriority).setCaption("Приоритет");
        recipeGrid.setSizeFull();
        try {
            recipeGrid.setItems(new RecipeDAO().selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button addButton = new Button("Добавить");
        addButton.addStyleNames(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL);
        addButton.addClickListener(clickEvent -> {
            RecipeWindow recipeWindow = new RecipeWindow(recipeGrid, false);
            UI.getCurrent().addWindow(recipeWindow);
        });

        Button editButton = new Button("Изменить");
        editButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editButton.setEnabled(false);
        editButton.addClickListener(clickEvent -> {
            RecipeWindow recipeWindow = new RecipeWindow(recipeGrid, true);
            UI.getCurrent().addWindow(recipeWindow);
        });

        Button deleteButton = new Button("Удалить");
        deleteButton.addStyleNames(ValoTheme.BUTTON_DANGER, ValoTheme.BUTTON_SMALL);
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(clickEvent -> {
            if (!recipeGrid.asSingleSelect().isEmpty()) {
                try {
                    new RecipeDAO().delete(recipeGrid.asSingleSelect().getValue());
                    updateGrid();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addButton, editButton, deleteButton);

        recipeGrid.addSelectionListener(selectionEvent -> {
            deleteButton.setEnabled(!recipeGrid.asSingleSelect().isEmpty());
            editButton.setEnabled(!recipeGrid.asSingleSelect().isEmpty());
        });

        addComponents(filterPanel, recipeGrid, buttonsLayout);
        setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
    }

    private void onFilterChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
        ListDataProvider<Recipe> dataProvider = (ListDataProvider<Recipe>) recipeGrid.getDataProvider();
        dataProvider.setFilter(recipe -> {
            boolean doesNameMatch = caseInsensitiveContains(recipe.getPatient().getFirstName().charAt(0) + ". " + recipe.getPatient().getPatronymic().charAt(0) + ". " + recipe.getPatient().getLastName(), patientTf.getValue());
            boolean doesDescriptionMatch = caseInsensitiveContains(recipe.getDescription(), descriptionTf.getValue());
            boolean doesPriorityMatch = caseInsensitiveContains(recipe.getPriority(), priorityTf.getValue());

            return doesNameMatch && doesDescriptionMatch && doesPriorityMatch;
        });
    }

    private boolean caseInsensitiveContains(String where, String what) {
        return where.toLowerCase().contains(what.toLowerCase());
    }

    private void updateGrid() {
        try {
            recipeGrid.setItems(new RecipeDAO().selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }
}
