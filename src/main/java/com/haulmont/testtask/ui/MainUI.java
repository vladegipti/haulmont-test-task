package com.haulmont.testtask.ui;

import com.haulmont.testtask.ui.views.DoctorView;
import com.haulmont.testtask.ui.views.PatientView;
import com.haulmont.testtask.ui.views.RecipeView;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        TabSheet tabsheet = new TabSheet();
        tabsheet.addTab(new PatientView(), "Пациенты");
        tabsheet.addTab(new DoctorView(), "Доктора");
        tabsheet.addTab(new RecipeView(), "Рецепты");
        tabsheet.addStyleNames(ValoTheme.TABSHEET_FRAMED, ValoTheme.TABSHEET_PADDED_TABBAR);
        tabsheet.setSizeFull();

        VerticalLayout layout = new VerticalLayout(tabsheet);
        layout.setSizeFull();
        layout.setMargin(false);

        setContent(layout);
    }
}
