package com.github.view;

import com.github.entity.Bank;
import com.github.entity.Client;
import com.github.service.BankService;
import com.github.service.ClientService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "clients")
@PageTitle("Clients | Application")
public class ClientListView extends VerticalLayout {
    private final ClientService clientService;
    private final Grid<Client> grid = new Grid<>(Client.class);
    private final ClientForm clientForm;

    public ClientListView(ClientService clientService, BankService bankService) {
        this.clientService = clientService;
        addClassName("client-list-view");
        setSizeFull();
        configureGrid();
        clientForm = new ClientForm(bankService.findAll());
        clientForm.addListener(ClientForm.SaveEvent.class, this::saveClient);
        clientForm.addListener(ClientForm.DeleteEvent.class, this::deleteClient);
        clientForm.addListener(ClientForm.CloseEvent.class, e -> closeEditor());

        VerticalLayout content = new VerticalLayout();
        content.add(grid, clientForm);
        content.expand(clientForm);
        content.addClassName("content");
        content.setSizeFull();
        add(getLinks(), getToolBar(), content);
        updateList();
        closeEditor();
    }


    private void saveClient(ClientForm.SaveEvent event) {
        clientService.save(event.getClient());
        updateList();
        closeEditor();
    }

    private void deleteClient(ClientForm.DeleteEvent event) {
        clientService.delete(event.getClient());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("client-grid");
        grid.setSizeFull();
        grid.removeColumnByKey("bank");
        grid.setColumns("firstName", "lastName", "phone", "email", "passport");
        grid.addColumn(client -> {
            Bank bank = client.getBank();
            return bank == null ? "-" : bank.getName();
        }).setHeader("Bank");
        grid.getColumns().forEach(clientColumn -> clientColumn.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editClient(event.getValue()));
    }

    public void editClient(Client client) {
        if (client == null) {
            closeEditor();
        } else {
            clientForm.setClient(client);
            clientForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        clientForm.setClient(null);
        clientForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(clientService.findAll());
    }

    private HorizontalLayout getToolBar() {
        Button addClientButton = new Button("Add client");
        addClientButton.addClickListener(click -> addClient());

        HorizontalLayout toolbar = new HorizontalLayout(addClientButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private HorizontalLayout getLinks() {
        RouterLink banksListLink = new RouterLink("Banks list", BankListView.class);
        RouterLink creditsListLink = new RouterLink("Credits list", CreditListView.class);
        RouterLink offerListLink = new RouterLink("Credit offers list", CreditOfferListView.class);

        HorizontalLayout linkList = new HorizontalLayout(banksListLink, creditsListLink, offerListLink);
        linkList.addClassName("linkList");
        return linkList;
    }

    void addClient() {
        grid.asSingleSelect().clear();
        editClient(new Client());
        removeClassName("editing");
    }
}
