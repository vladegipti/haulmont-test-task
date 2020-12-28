package com.haulmont.testtask.ui.views;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.model.Doctor;
import com.haulmont.testtask.ui.windows.DoctorWindow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;

public class DoctorView extends VerticalLayout implements View {
    Grid<Doctor> doctorGrid;

    public DoctorView() {
        doctorGrid = new Grid<>(Doctor.class);
        doctorGrid.setSizeFull();
        doctorGrid.setColumns("firstName", "lastName", "patronymic", "specialization");
        doctorGrid.getColumn("firstName").setCaption("Имя");
        doctorGrid.getColumn("lastName").setCaption("Фамилия");
        doctorGrid.getColumn("patronymic").setCaption("Отчество");
        doctorGrid.getColumn("specialization").setCaption("Специализация");
        try {
            doctorGrid.setItems(new DoctorDAO().selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button addButton = new Button("Добавить");
        addButton.addStyleNames(ValoTheme.BUTTON_PRIMARY, ValoTheme.BUTTON_SMALL);
        addButton.addClickListener(clickEvent -> {
            DoctorWindow doctorWindow = new DoctorWindow(doctorGrid, false);
            UI.getCurrent().addWindow(doctorWindow);
        });

        Button editButton = new Button("Изменить");
        editButton.addStyleName(ValoTheme.BUTTON_SMALL);
        editButton.setEnabled(false);
        editButton.addClickListener(clickEvent -> {
            DoctorWindow doctorWindow = new DoctorWindow(doctorGrid, true);
            UI.getCurrent().addWindow(doctorWindow);
        });

        Button deleteButton = new Button("Удалить");
        deleteButton.addStyleNames(ValoTheme.BUTTON_DANGER, ValoTheme.BUTTON_SMALL);
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(clickEvent -> {
            if (!doctorGrid.asSingleSelect().isEmpty()) {
                try {
                    new DoctorDAO().delete(doctorGrid.asSingleSelect().getValue());
                    updateGrid();
                } catch (SQLException e) {
                    new Notification("Ошибка удаления", "Невозможно удалить запись, сначала удалите\nсвязанные с ней рецепты.", Notification.Type.TRAY_NOTIFICATION).show(Page.getCurrent());
                }
            }
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(addButton, editButton, deleteButton);

        doctorGrid.addSelectionListener(selectionEvent -> {
            deleteButton.setEnabled(!doctorGrid.asSingleSelect().isEmpty());
            editButton.setEnabled(!doctorGrid.asSingleSelect().isEmpty());
        });

        addComponents(doctorGrid, buttonsLayout);
        setComponentAlignment(buttonsLayout, Alignment.BOTTOM_RIGHT);
    }

    private void updateGrid() {
        try {
            doctorGrid.setItems(new DoctorDAO().selectAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }
}
