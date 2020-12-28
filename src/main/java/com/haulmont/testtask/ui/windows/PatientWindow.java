package com.haulmont.testtask.ui.windows;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.model.Patient;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;

public class PatientWindow extends Window {
    private Patient patient;

    public PatientWindow(Grid<Patient> patientGrid, boolean toEdit) {
        setResizable(false);
        setDraggable(false);
        setModal(true);
        setHeight("370px");
        setWidth("400px");
        center();

        VerticalLayout layout = new VerticalLayout();

        FormLayout form = new FormLayout();
        form.setSizeFull();

        Binder<Patient> binder = new Binder<>();

        TextField firstNameTf = new TextField("Имя");
        firstNameTf.setRequiredIndicatorVisible(true);
        firstNameTf.setWidthFull();
        firstNameTf.setMaxLength(20);
        firstNameTf.focus();
        binder.forField(firstNameTf).asRequired("Введите имя").bind(Patient::getFirstName, Patient::setFirstName);

        TextField lastNameTf = new TextField("Фамилия");
        lastNameTf.setRequiredIndicatorVisible(true);
        lastNameTf.setWidthFull();
        lastNameTf.setMaxLength(35);
        binder.forField(lastNameTf).asRequired("Введите фамилию").bind(Patient::getLastName, Patient::setLastName);

        TextField patronymicTf = new TextField("Отчество");
        patronymicTf.setRequiredIndicatorVisible(true);
        patronymicTf.setWidthFull();
        patronymicTf.setMaxLength(20);
        binder.forField(patronymicTf).asRequired("Введите отчество").bind(Patient::getPatronymic, Patient::setPatronymic);

        TextField phoneNumberTf = new TextField("Номер телефона");
        phoneNumberTf.setRequiredIndicatorVisible(true);
        phoneNumberTf.setWidthFull();
        phoneNumberTf.setMaxLength(20);
        binder.forField(phoneNumberTf).asRequired("Введите номер телефона").bind(Patient::getPhoneNumber, Patient::setPhoneNumber);

        form.addComponents(firstNameTf, lastNameTf, patronymicTf, phoneNumberTf);

        if (toEdit) {
            setCaption("Редактировать данные");
            patient = patientGrid.asSingleSelect().getValue();
            binder.setBean(patient);
        } else setCaption("Добавить пациента");

        Button okButton = new Button("OK");
        okButton.addStyleName(ValoTheme.BUTTON_SMALL);
        okButton.addClickListener(event -> {
            if (binder.validate().isOk()) {
                try {
                    if (toEdit)
                        new PatientDAO().update(patient);
                    else
                        new PatientDAO().insert(new Patient(firstNameTf.getValue(), lastNameTf.getValue(), patronymicTf.getValue(), phoneNumberTf.getValue()));
                    patientGrid.setItems(new PatientDAO().selectAll());
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
