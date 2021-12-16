package com.github.view;

import com.github.service.ClientService;
import com.github.service.CreditOfferService;
import com.github.service.CreditService;
import com.github.service.PaymentScheduleService;
import com.vaadin.flow.component.button.Button;
import com.github.entity.CreditOffer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "creditOffers")
@PageTitle("Credit offers | Application")
public class CreditOfferListView extends VerticalLayout {
    private final Grid<CreditOffer> grid = new Grid<>(CreditOffer.class);
    private final CreditOfferForm offerForm;
    private final CreditOfferService offerService;
    private final PaymentScheduleService paymentScheduleService;

    public CreditOfferListView(CreditOfferService offerService, PaymentScheduleService paymentService, ClientService clientService, CreditService creditService) {
        this.offerService = offerService;
        this.paymentScheduleService=paymentService;
        setSizeFull();
        configureGrid();
        addClassName("credit-offer-list-view");
        offerForm = new CreditOfferForm(clientService.findAll(), creditService.findAll(), paymentService, offerService);
        offerForm.addListener(CreditOfferForm.SaveEvent.class, this::saveCreditOffer);
        offerForm.addListener(CreditOfferForm.DeleteEvent.class, this::deleteCreditOffer);
        offerForm.addListener(CreditOfferForm.CloseEvent.class, e -> closeEditor());

        VerticalLayout content = new VerticalLayout();
        content.add(grid, offerForm);
        content.expand(offerForm);
        content.addClassName("content");
        content.setSizeFull();
        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void saveCreditOffer(CreditOfferForm.SaveEvent event) {
        offerService.save(event.getOffer());
        paymentScheduleService.generatePaymentSchedule(event.getOffer());
        closeEditor();
        updateList();
    }

    private void deleteCreditOffer(CreditOfferForm.DeleteEvent event) {
        offerService.delete(event.getOffer());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassName("credit-offer-grid");
        grid.removeAllColumns();
        grid.addColumn((ValueProvider<CreditOffer, Object>) offer -> offer.getClient().getDisplayName())
                .setHeader("Client")
                .setSortable(true);
        grid.addColumn((ValueProvider<CreditOffer, Object>) offer -> offer.getCredit().getLimit())
                .setHeader("Credit limit")
                .setSortable(true);
        grid.addColumn((ValueProvider<CreditOffer, Object>) CreditOffer::getCreditSum)
                .setHeader("Credit Sum")
                .setSortable(true);
        grid.addColumn((ValueProvider<CreditOffer, Object>) CreditOffer::getCreditTerm)
                .setHeader("Credit Term")
                .setSortable(true);
        grid.asSingleSelect().addValueChangeListener(event -> editCreditOffer(event.getValue()));
    }

    public void editCreditOffer(CreditOffer offer) {
        if (offer == null) {
            closeEditor();
        } else {
            offerForm.setCreditOffer(offer);
            offerForm.paymentGrid.setItems(offerService.getPaymentsScheduleByOffer(offer));
            offerForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        offerForm.setCreditOffer(null);
        offerForm.setVisible(false);
        grid.asSingleSelect().clear();
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(offerService.findAll());
    }

    private VerticalLayout getToolBar() {
        Button addCreditOfferButton = new Button("Add credit offer");
        addCreditOfferButton.addClickListener(click -> addCreditOffer());

        VerticalLayout toolbar = new VerticalLayout(addCreditOfferButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    void addCreditOffer() {
        grid.asSingleSelect().clear();
        editCreditOffer(new CreditOffer());
        removeClassName("editing");
    }
}
