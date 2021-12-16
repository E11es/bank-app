package com.github.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;


//@CssImport("styles.css")
@Route(value = "app")
public class MainLayout extends VerticalLayout {
    public MainLayout() {
        //Link bankLink = new Link("To Banks", new ExternalResource("http://localhost:8080/app"));
        RouterLink banksListLink = new RouterLink("Banks list", BankListView.class);
      //  banksListLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink creditsListLink = new RouterLink("Credits list", CreditListView.class);
     //   creditsListLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink clientsListLink = new RouterLink("Clients list", ClientListView.class);
     //   clientsListLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink offerListLink = new RouterLink("Credit offers list", CreditOfferListView.class);
     //   offerListLink.setHighlightCondition(HighlightConditions.sameLocation());
        setSizeFull();
        add(clientsListLink, creditsListLink, banksListLink, offerListLink);
        setPadding(true);
    }
}