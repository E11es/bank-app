package com.github.view;

import com.github.entity.Bank;
import com.github.entity.Credit;
import com.github.service.implement.BankServiceImpl;
import com.github.service.implement.CreditServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "credits")
@PageTitle("Credits | Application")
public class CreditListView extends VerticalLayout {
    private CreditServiceImpl creditService;
    private Grid<Credit> grid = new Grid<>(Credit.class);
    private CreditForm creditForm;

    public CreditListView(CreditServiceImpl creditService, BankServiceImpl bankService) {
        this.creditService = creditService;
        addClassName("credit-list-view");
        setSizeFull();
        configureGrid();

        creditForm = new CreditForm(bankService.findAll());
        creditForm.addListener(CreditForm.SaveEvent.class, this::saveCredit);
        creditForm.addListener(CreditForm.DeleteEvent.class, this::deleteCredit);
        creditForm.addListener(CreditForm.CloseEvent.class, event -> closeEditor());

        VerticalLayout content = new VerticalLayout();
        content.add(grid, creditForm);
        content.expand(creditForm);
        content.addClassName("content");
        content.setSizeFull();
        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("credit-grid");
        grid.setSizeFull();
        grid.removeColumnByKey("bank");
        grid.setColumns("limit", "interestRate");
        grid.addColumn(credit -> {
            Bank bank = credit.getBank();
            return bank == null ? "-" : bank.getName();
        }).setHeader("Bank");
        grid.getColumns().forEach(creditColumn -> creditColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editCredit(event.getValue()));
    }

    private void saveCredit(CreditForm.SaveEvent event) {
        creditService.save(event.getCredit());
        updateList();
        closeEditor();
    }

    private void deleteCredit(CreditForm.DeleteEvent event) {
        creditService.delete(event.getCredit());
        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(creditService.findAll());
    }

    private void editCredit(Credit credit) {
        if (credit == null) {
            closeEditor();
        } else {
            creditForm.setCredit(credit);
            creditForm.setVisible(true);
            addClassName("editing");
        }
    }

    void addCredit() {
        grid.asSingleSelect().clear();
        editCredit(new Credit());
        removeClassName("editing");
    }

    private void closeEditor() {
        creditForm.setCredit(null);
        creditForm.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        Button addCreditButton = new Button("Add credit");
        addCreditButton.addClickListener(click -> addCredit());

        HorizontalLayout toolbar = new HorizontalLayout(addCreditButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}
