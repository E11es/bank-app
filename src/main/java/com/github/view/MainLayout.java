package com.github.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;


@Route(value = "app")
public class MainLayout extends VerticalLayout {
    public MainLayout() {
        RouterLink banksListLink = new RouterLink("Banks list", BankListView.class);
        RouterLink creditsListLink = new RouterLink("Credits list", CreditListView.class);
        RouterLink clientsListLink = new RouterLink("Clients list", ClientListView.class);
        RouterLink offerListLink = new RouterLink("Credit offers list", CreditOfferListView.class);
        setSizeFull();
        add(banksListLink, clientsListLink, creditsListLink, offerListLink);
        setPadding(true);
    }
}