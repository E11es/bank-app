package com.github.view;

import com.github.entity.Bank;
import com.github.entity.Client;
import com.github.entity.Credit;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;


//Создаю окно макета формы FormLayout
public class BankForm extends FormLayout {
    private Bank bank;
    //Поле для имени банка
    TextField name = new TextField("Name");
    //Создание сетки клиента и сетки кредита
    Grid<Client> clientGrid = new Grid<>(Client.class);
    Grid<Credit> creditGrid = new Grid<>(Credit.class);
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    //Binder позволяет определять, как привязываются данные к полям в интерфейсе пользователя
    Binder<Bank> binder = new BeanValidationBinder<>(Bank.class);

    public void setBank(Bank bank){
        this.bank=bank;
        //Обновляю значение в каждом компоненте связанного поля
        binder.readBean(bank);
    }

    //Указываю, как заполняется BankForm (конструктор)
    public BankForm(List<Client> clients, List<Credit> credits) {
        addClassName("bank-form");
        setSizeFull();
        //Я нахожу соответсвующие поля в Client и Credit и "подставляю" их в таблицу?
        //binder.bindInstanceFields(this);
        clientGrid.setItems(clients);
        clientGrid.setColumns("firstName", "lastName", "phone", "email", "passport");
        creditGrid.setItems(credits);
        creditGrid.setColumns("limit", "interestRate");
        add(name, createButtonsLayout(), clientGrid, creditGrid);
    }

    private HorizontalLayout createButtonsLayout(){
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, bank)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(bank);
            fireEvent(new SaveEvent(this, bank));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class BankFormEvent extends ComponentEvent<BankForm> {
        private Bank bank;

        public BankFormEvent(BankForm source, Bank bank) {
            super(source, false);
            this.bank = bank;
        }

        public Bank getBank() {
            return bank;
        }
    }

    public static class SaveEvent extends BankFormEvent {
        SaveEvent(BankForm source, Bank bank) {
            super(source, bank);
        }
    }

    public static class DeleteEvent extends BankFormEvent {
        DeleteEvent(BankForm source, Bank bank) {
            super(source, bank);
        }
    }

    public static class CloseEvent extends BankFormEvent {
        CloseEvent(BankForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener
            (Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

