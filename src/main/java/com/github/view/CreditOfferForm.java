package com.github.view;

import com.github.entity.Client;
import com.github.entity.Credit;
import com.github.entity.CreditOffer;
import com.github.entity.PaymentSchedule;
import com.github.service.CreditService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import java.util.Objects;

public class CreditOfferForm extends FormLayout {
    ComboBox<Client> client = new ComboBox<>("Client");
    ComboBox<Credit> credit = new ComboBox<>("Credit");
    IntegerField creditSum = new IntegerField("Credit sum");
    IntegerField creditTerm = new IntegerField("Credit term in months");
    Grid<PaymentSchedule> paymentGrid = new Grid<>(PaymentSchedule.class);
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<CreditOffer> binder = new BeanValidationBinder<>(CreditOffer.class);
    private CreditOffer offer;

    public CreditOfferForm(List<Client> clients, CreditService creditService) {
        addClassName("credit-offer-form");
        setSizeFull();
        binder.bindInstanceFields(this);
        binder.forField(client)
                .withValidator(Objects::nonNull, "Client cannot be null")
                .bind(CreditOffer::getClient, CreditOffer::setClient);
        binder.forField(credit)
                .withValidator(Objects::nonNull, "Credit cannot be null")
                .bind(CreditOffer::getCredit, CreditOffer::setCredit);
        binder.forField(creditSum)
                .withValidator(Objects::nonNull, "Credit sum cannot be null")
                .withValidator(integer -> integer > 0, "Credit sum cannot be 0 or lower")
                .withValidator(integer -> integer <= credit.getValue().getLimit(), "Credit sum cannot exceed credit limit")
                .bind(CreditOffer::getCreditSum, CreditOffer::setCreditSum);
        binder.forField(creditTerm)
                .withValidator(Objects::nonNull, "Credit term cannot be null")
                .withValidator(integer -> integer > 0, "Credit term cannot be 0 or lower")
                .bind(CreditOffer::getCreditTerm, CreditOffer::setCreditTerm);
        client.setItems(clients);
        client.setItemLabelGenerator(Client::getDisplayName);
        client.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<Client>, Client>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<Client>, Client> event) {
                Client client = event.getValue();
                credit.setItems(creditService.findByBank(client.getBank()));
            }
        });
        credit.setItemLabelGenerator(Credit::getCreditLimit);
        paymentGrid.setColumns("paymentDate", "paymentSum", "creditBodySum", "creditPercentSum");
        add(client,
                credit,
                creditSum,
                creditTerm,
                paymentGrid,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, offer)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }


    public void setCreditOffer(CreditOffer offer) {
        this.offer = offer;
        binder.readBean(offer);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(offer);
            fireEvent(new SaveEvent(this, offer));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener
            (Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class OfferFormEvent extends ComponentEvent<CreditOfferForm> {
        private final CreditOffer offer;

        public OfferFormEvent(CreditOfferForm source, CreditOffer offer) {
            super(source, false);
            this.offer = offer;
        }

        public CreditOffer getOffer() {
            return offer;
        }
    }

    public static class SaveEvent extends OfferFormEvent {
        SaveEvent(CreditOfferForm source, CreditOffer offer) {
            super(source, offer);
        }
    }

    public static class DeleteEvent extends OfferFormEvent {
        DeleteEvent(CreditOfferForm source, CreditOffer offer) {
            super(source, offer);
        }
    }

    public static class CloseEvent extends OfferFormEvent {
        CloseEvent(CreditOfferForm source) {
            super(source, null);
        }
    }
}
