package com.haulmont.testtask.ui.windows;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.model.Doctor;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;

public class DoctorWindow extends Window {
    private Doctor doctor;

    public DoctorWindow(Grid<Doctor> doctorGrid, boolean toEdit) {
        setResizable(false);
        setDraggable(false);
        setModal(true);
        setHeight("370px");
        setWidth("400px");
        center();

        VerticalLayout layout = new VerticalLayout();

        FormLayout form = new FormLayout();
        form.setSizeFull();

        Binder<Doctor> binder = new Binder<>();

        TextField firstNameTf = new TextField("Имя");
        firstNameTf.setRequiredIndicatorVisible(true);
        firstNameTf.setWidthFull();
        firstNameTf.setMaxLength(20);
        firstNameTf.focus();
        binder.forField(firstNameTf).asRequired("Введите имя").bind(Doctor::getFirstName, Doctor::setFirstName);

        TextField lastNameTf = new TextField("Фамилия");
        lastNameTf.setRequiredIndicatorVisible(true);
        lastNameTf.setWidthFull();
        lastNameTf.setMaxLength(35);
        binder.forField(lastNameTf).asRequired("Введите фамилию").bind(Doctor::getLastName, Doctor::setLastName);

        TextField patronymicTf = new TextField("Отчество");
        patronymicTf.setRequiredIndicatorVisible(true);
        patronymicTf.setWidthFull();
        patronymicTf.setMaxLength(20);
        binder.forField(patronymicTf).asRequired("Введите отчество").bind(Doctor::getPatronymic, Doctor::setPatronymic);

        TextField specializationTf = new TextField("Специализация");
        specializationTf.setRequiredIndicatorVisible(true);
        specializationTf.setWidthFull();
        specializationTf.setMaxLength(40);
        binder.forField(specializationTf).asRequired("Введите специализацию").bind(Doctor::getSpecialization, Doctor::setSpecialization);

        form.addComponents(firstNameTf, lastNameTf, patronymicTf, specializationTf);

        if (toEdit) {
            setCaption("Редактировать данные");
            doctor = doctorGrid.asSingleSelect().getValue();
            binder.setBean(doctor);
        } else setCaption("Добавить доктора");

        Button okButton = new Button("OK");
        okButton.addStyleName(ValoTheme.BUTTON_SMALL);
        okButton.addClickListener(event -> {
            if (binder.validate().isOk()) {
                try {
                    if (toEdit)
                        new DoctorDAO().update(doctor);
                    else
                        new DoctorDAO().insert(new Doctor(firstNameTf.getValue(), lastNameTf.getValue(), patronymicTf.getValue(), specializationTf.getValue()));
                    doctorGrid.setItems(new DoctorDAO().selectAll());
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
