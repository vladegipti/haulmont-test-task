package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.model.Patient;
import com.haulmont.testtask.ui.windows.PatientWindow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;

public class PatientView extends VerticalLayout implements View {
    Grid<Patient> patientGrid;

    public PatientView() {
        patientGrid = new Grid<>(Patient.class);
        patientGrid.setSizeFull();
        patientGrid.setColumns("firstName", "lastName", "patronymic", "phoneNumber");
        patientGrid.getColumn("firstName").setCaption("Имя");
        patientGrid.getColumn("lastName").setCaption("Фамилия");
        patientGrid.getColumn("patronymic").setCaption("Отчество");
        patientGrid.getColumn("phoneNumber").setCaption("Номер телефона");
        try {
            patientGrid.setItems(new PatientDAO().selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button addButton = new Button("Добавить");
        addButton.addStyleNames(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL);
        addButton.addClickListener(clickEvent -> {
            PatientWindow patientWindow = new PatientWindow(patientGrid, false);
            UI.getCurrent().addWindow(patientWindow);
        });

        Button editButton = new Button("Изменить");
        editButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editButton.setEnabled(false);
        editButton.addClickListener(clickEvent -> {
            PatientWindow patientWindow = new PatientWindow(patientGrid, true);
            UI.getCurrent().addWindow(patientWindow);
        });

        Button deleteButton = new Button("Удалить");
        deleteButton.addStyleNames(ValoTheme.BUTTON_DANGER, ValoTheme.BUTTON_SMALL);
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(clickEvent -> {
            if (!patientGrid.asSingleSelect().isEmpty()) {
                try {
                    new PatientDAO().delete(patientGrid.asSingleSelect().getValue());
                    updateGrid();
                } catch (SQLException e) {
                    new Notification("Ошибка удаления", "Невозможно удалить запись, сначала удалите\nсвязанные с ней рецепты.", Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
                }
            }
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addButton, editButton, deleteButton);

        patientGrid.addSelectionListener(selectionEvent -> {
            deleteButton.setEnabled(!patientGrid.asSingleSelect().isEmpty());
            editButton.setEnabled(!patientGrid.asSingleSelect().isEmpty());
        });

        addComponents(patientGrid, buttonsLayout);
        setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
    }

    private void updateGrid() {
        try {
            patientGrid.setItems(new PatientDAO().selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        updateGrid();
    }
}
