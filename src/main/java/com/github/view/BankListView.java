package com.github.view;

import com.github.entity.Bank;
import com.github.service.BankService;
import com.github.service.ClientService;
import com.github.service.CreditService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "banks")
@PageTitle("Banks | Application")
public class BankListView extends VerticalLayout {
    private final Grid<Bank> grid = new Grid<>(Bank.class);
    private final BankForm bankForm;
    private final BankService bankService;
    private final ClientService clientService;
    private final CreditService creditService;

    public BankListView(BankService bankService, ClientService clientService, CreditService creditService) {
        this.bankService = bankService;
        this.clientService = clientService;
        this.creditService = creditService;
        addClassName("bank-list-view");
        setSizeFull();
        configureGrid();
        bankForm = new BankForm(clientService.findAll(), creditService.findAll());
        bankForm.addListener(BankForm.SaveEvent.class, event -> saveBank(event));
        bankForm.addListener(BankForm.DeleteEvent.class, this::deleteBank);
        bankForm.addListener(BankForm.CloseEvent.class, e -> closeEditor());

        VerticalLayout content = new VerticalLayout();
        content.add(grid, bankForm);
        content.expand(bankForm);
        content.addClassName("content");
        content.setSizeFull();
        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void saveBank(BankForm.SaveEvent event) {
        bankService.save(event.getBank());
        updateList();
        closeEditor();
    }

    private void deleteBank(BankForm.DeleteEvent event) {
        bankService.delete(event.getBank());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("bank-grid");
        grid.setColumns("name");
        grid.asSingleSelect().addValueChangeListener(event -> editBank(event.getValue()));
    }

    public void editBank(Bank bank) {
        if (bank == null) {
            closeEditor();
        } else {
            bankForm.setBank(bank);
            bankForm.clientGrid.setItems(clientService.findByBank(bank));
            bankForm.creditGrid.setItems(creditService.findByBank(bank));
            bankForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        bankForm.setBank(null);
        bankForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(bankService.findAll());
    }

    private HorizontalLayout getToolBar() {

        Button addBankButton = new Button("Add bank");
        addBankButton.addClickListener(click -> addBank());

        HorizontalLayout toolbar = new HorizontalLayout(addBankButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    void addBank() {
        grid.asSingleSelect().clear();
        editBank(new Bank());
        removeClassName("editing");
    }
}
