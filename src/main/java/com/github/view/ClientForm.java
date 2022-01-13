package com.github.view;

import com.github.entity.Bank;
import com.github.entity.Client;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ClientForm extends FormLayout {
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField phone = new TextField("Phone number");
    TextField email = new TextField("Email");
    TextField passport = new TextField("Passport number");
    ComboBox<Bank> bank = new ComboBox<>("Bank");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Client> binder = new BeanValidationBinder<>(Client.class);
    private Client client;

    public ClientForm(List<Bank> banks) {
        addClassName("client-form");
        setSizeFull();
        binder.bindInstanceFields(this);
        bank.setItems(banks);
        bank.setItemLabelGenerator(Bank::getName);
        firstName.setRequired(true);
        lastName.setRequired(true);
        phone.setRequired(true);
        email.setRequired(true);
        passport.setRequired(true);
        bank.setRequired(true);
        binder.forField(phone)
                .withValidator(new RegexpValidator("Only 0-9 allowed", "\\d+"))
                .bind(Client::getPhone, Client::setPhone);
        binder.forField(passport)
                .withValidator(new RegexpValidator("Only 0-9 allowed", "\\d+"))
                .bind(Client::getPassport, Client::setPassport);
        binder.forField(email)
                .withValidator(new RegexpValidator("Invalid email", "^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$"))
                .bind(Client::getEmail, Client::setEmail);

        add(firstName,
                lastName,
                phone,
                email,
                passport,
                bank,
                createButtonsLayout());
    }

    public void setClient(Client client) {
        this.client = client;
        binder.readBean(client);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, client)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(client);
            fireEvent(new SaveEvent(this, client));
        } catch (ValidationException e) {
            Notification notification = new Notification("All fields are required!", 1000*3, Notification.Position.MIDDLE);
            notification.open();
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class ClientFormEvent extends ComponentEvent<ClientForm> {
        private final Client client;

        public ClientFormEvent(ClientForm source, Client client) {
            super(source, false);
            this.client = client;
        }

        public Client getClient() {
            return client;
        }
    }

    public static class SaveEvent extends ClientFormEvent {
        SaveEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }

    public static class DeleteEvent extends ClientFormEvent {
        DeleteEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }

    public static class CloseEvent extends ClientFormEvent {
        CloseEvent(ClientForm source) {
            super(source, null);
        }
    }
}