package com.haulmont.testtask.ui.windows;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.RecipeDAO;
import com.haulmont.testtask.model.Doctor;
import com.haulmont.testtask.model.Patient;
import com.haulmont.testtask.model.Recipe;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class RecipeWindow extends Window {
    private Recipe recipe;

    public RecipeWindow(Grid<Recipe> recipeGrid, boolean toEdit) {
        setResizable(false);
        setDraggable(false);
        setModal(true);
        setHeight("570px");
        setWidth("450px");
        center();

        VerticalLayout layout = new VerticalLayout();

        FormLayout form = new FormLayout();
        form.setSizeFull();

        Binder<Recipe> binder = new Binder<>();

        TextArea descriptionTxt = new TextArea("Описание");
        descriptionTxt.setRequiredIndicatorVisible(true);
        descriptionTxt.setWidthFull();
        descriptionTxt.setMaxLength(255);
        descriptionTxt.focus();
        binder.forField(descriptionTxt).asRequired("Введите описание").bind(Recipe::getDescription, Recipe::setDescription);

        ComboBox<Patient> patientComboBox = new ComboBox<>("Пациент");
        patientComboBox.setRequiredIndicatorVisible(true);
        patientComboBox.setWidthFull();
        patientComboBox.setEmptySelectionAllowed(false);
        binder.forField(patientComboBox).asRequired("Выберите пациента").bind(Recipe::getPatient, Recipe::setPatient);
        try {
            patientComboBox.setItems(new PatientDAO().selectAll());
            patientComboBox.setItemCaptionGenerator(patient -> patient.getFirstName().charAt(0) + ". " + patient.getPatronymic().charAt(0) + ". " + patient.getLastName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ComboBox<Doctor> doctorComboBox = new ComboBox<>("Доктор");
        doctorComboBox.setRequiredIndicatorVisible(true);
        doctorComboBox.setWidthFull();
        doctorComboBox.setEmptySelectionAllowed(false);
        binder.forField(doctorComboBox).asRequired("Выберите доктора").bind(Recipe::getDoctor, Recipe::setDoctor);
        try {
            doctorComboBox.setItems(new DoctorDAO().selectAll());
            doctorComboBox.setItemCaptionGenerator(doctor -> doctor.getFirstName().charAt(0) + ". " + doctor.getPatronymic().charAt(0) + ". " + doctor.getLastName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DateField dateOfIssueDf = new DateField(LocalDate.now());
        dateOfIssueDf.setCaption("Дата выдачи");
        dateOfIssueDf.setRequiredIndicatorVisible(true);
        dateOfIssueDf.setWidthFull();
        dateOfIssueDf.setDateFormat("dd.MM.yyyy");
        dateOfIssueDf.setParseErrorMessage("Неверный формат даты");
        binder.forField(dateOfIssueDf).asRequired("Введите дату выдачи").withConverter(new LocalDateToDateConverter(ZoneId.systemDefault())).bind(Recipe::getDateOfIssue, Recipe::setDateOfIssue);

        TextField validForTf = new TextField("Срок действия");
        validForTf.setRequiredIndicatorVisible(true);
        validForTf.setWidthFull();
        binder.forField(validForTf).asRequired("Введите срок действия").withConverter(new StringToIntegerConverter("Срок действия указывается числом в днях")).withValidator(integer -> integer > 0, "Введите корректный срок действия").bind(Recipe::getValidFor, Recipe::setValidFor);

        ComboBox<String> priorityComboBox = new ComboBox<>("Приоритет");
        priorityComboBox.setRequiredIndicatorVisible(true);
        priorityComboBox.setWidthFull();
        priorityComboBox.setEmptySelectionAllowed(false);
        binder.forField(priorityComboBox).asRequired("Выберите приоритет").bind(Recipe::getPriority, Recipe::setPriority);
        priorityComboBox.setItems(new ArrayList<>(Arrays.asList("Нормальный", "Cito", "Statim")));

        form.addComponents(descriptionTxt, patientComboBox, doctorComboBox, dateOfIssueDf, validForTf, priorityComboBox);

        if (toEdit) {
            setCaption("Редактировать данные");
            recipe = recipeGrid.asSingleSelect().getValue();
            binder.setBean(recipe);
        } else setCaption("Добавить рецепт");

        Button okButton = new Button("OK");
        okButton.addStyleName(ValoTheme.BUTTON_SMALL);
        okButton.addClickListener(event -> {
            if (binder.validate().isOk()) {
                try {
                    if (toEdit)
                        new RecipeDAO().update(recipe);
                    else {
                        new RecipeDAO().insert(new Recipe(descriptionTxt.getValue(), patientComboBox.getValue(), doctorComboBox.getValue(), Date.from(dateOfIssueDf.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Integer.parseInt(validForTf.getValue()), priorityComboBox.getValue()));
                    }
                    recipeGrid.setItems(new RecipeDAO().selectAll());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                close();
            }
        });

        Button cancelButton = new Button("Отменить");
        cancelButton.addStyleName(ValoTheme.BUTTON_SMALL);
        cancelButton.addClickListener(event -> close());

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(okButton, cancelButton);

        layout.addComponents(form, buttonsLayout);
        layout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);

        setContent(layout);
    }
}
